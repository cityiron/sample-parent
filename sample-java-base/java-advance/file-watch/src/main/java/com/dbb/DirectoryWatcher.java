package com.dbb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

/**
 * 一个类监听一个根目录
 *
 * @author tc
 * @date 2019-09-29
 */
public class DirectoryWatcher {

    private Logger logger = LoggerFactory.getLogger(DirectoryWatcher.class);

    private final WatchService watcher;
    private final Map<WatchKey, Path> keys = new HashMap<>();
    private final Set<Path> paths = new HashSet<>();
    private Path rootPath;
    private boolean filter = true;
    private boolean macEnv = true;

    private final Map<Path, List<Path>> createHistory = new HashMap();

    private final ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS,
        new LinkedBlockingQueue<Runnable>());

    private final ThreadPoolExecutor executorCompensate = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS,
        new LinkedBlockingQueue<Runnable>());

    public DirectoryWatcher() throws IOException {
        this.watcher = FileSystems.getDefault().newWatchService();
    }

    public DirectoryWatcher(boolean enableFilter) throws IOException {
        this.watcher = FileSystems.getDefault().newWatchService();
        this.filter = enableFilter;
    }

    /**
     * 注册单个目录
     *
     * @param dirPath
     * @throws IOException
     */
    public void register(String dirPath) throws IOException {
        this.register(Paths.get(dirPath));
    }

    /**
     * 注册单个目录
     *
     * @param dir
     * @throws IOException
     */
    public void register(Path dir) throws IOException {
        WatchKey key = dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
        keys.put(key, dir);
        if (filter) {
            paths.add(dir);
        }
    }

    /**
     * 递归注册目录
     *
     * @param dirPath
     * @throws IOException
     */
    public void registerAll(String dirPath) throws IOException {
        this.registerAll(Paths.get(dirPath));
    }

    /**
     * 递归注册目录
     *
     * @param start
     * @throws IOException
     */
    public void registerAll(final Path start) throws IOException {
        rootPath = start;
        Files.walkFileTree(start, new SimpleFileVisitor<Path>() {

            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                register(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    /**
     * 事件监控
     *
     * @param listener
     */
    public void processEvents(final FileWatchedListener listener) {
        executor.execute(() -> {

            for (; ; ) {

                WatchKey key;
                try {
                    // waiting
                    key = watcher.take();
                } catch (InterruptedException x) {
                    return;
                }

                Path dir = keys.get(key);
                if (dir == null) {
                    logger.warn("WatchKey not recognized!!");
                    continue;
                }

                for (WatchEvent<?> event : key.pollEvents()) {
                    WatchEvent.Kind<?> kind = event.kind();

                    WatchEvent<Path> ev = (WatchEvent<Path>)event;
                    Path filePath = ev.context();
                    Path fullPath = dir.resolve(filePath);

                    if (kind == OVERFLOW) {
                        listener.onOverflowed(ev, fullPath, filePath);
                        continue;
                    }

                    if (macEnv && ".DS_Store".equals(filePath.getFileName().toString())) {
                        // ignore mac .DS_Store
                        continue;
                    }

                    if (kind == ENTRY_CREATE) {
                        listener.onCreated(ev, fullPath, filePath);
                        boolean isDirectory = fullPath.toFile().isDirectory();
                        if (isDirectory) {
                            try {
                                createHistory.put(fullPath, new ArrayList<>());
                                executorCompensate.execute(new CompensateThread(listener, fullPath, createHistory));
                                register(fullPath);
                            } catch (IOException e) {
                                logger.error("Register new directory error", e);
                            }
                        } else {
                            if (createHistory.containsKey(filePath.getParent())) {
                                createHistory.get(filePath.getParent()).add(fullPath);
                            }
                        }
                    }

                    if (event.kind() == ENTRY_DELETE) {
                        if (filter && isMiddleDirectory(fullPath)) {
                            createHistory.remove(fullPath);
                            paths.remove(fullPath);
                        }
                        listener.onDeleted(ev, fullPath, filePath);
                    }

                    if (event.kind() == ENTRY_MODIFY) {
                        if (filter && isMiddleDirectory(fullPath)) {
                            // 中间层目录变化，需要捕获到新增的内容
                            continue;
                        } else {
                            listener.onModified(ev, fullPath, filePath);
                        }
                    }

                    if (logger.isDebugEnabled()) {
                        logger.debug(
                            "current event key unix path : {}, event is : {}, fullPath is : {}, filePath is : {} ",
                            key.watchable(), ev.kind().name(), fullPath, filePath);
                    }
                }

                // reset 如果目录不存在了，返回false
                boolean valid = key.reset();
                if (!valid) {
                    Path rmPath = keys.remove(key);
                    if (filter) {
                        paths.remove(rmPath);
                    }
                    if (keys.isEmpty()) {
                        break;
                    }
                }
            }
        });
    }

    private boolean isMiddleDirectory(Path fullPath) {
        if (paths.contains(rootPath)) {
            paths.remove(rootPath);
        }
        return paths.contains(fullPath);
    }

    /**
     * 销毁监控
     *
     * @throws IOException
     */
    public void destroy() throws IOException {
        watcher.close();
    }

}
