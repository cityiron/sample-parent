package com.dbb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.util.List;
import java.util.Map;

/**
 * 补偿线程
 *
 * @author tc
 * @date 2019-09-30
 */
public class CompensateThread implements Runnable {

    private Logger logger = LoggerFactory.getLogger(CompensateThread.class);

    private FileWatchedListener listener;

    /**
     * 外面传入的fullPath，对应内部的parentPath
     */
    private Path fullPath;
    private Map<Path, List<Path>> createHistory;

    public CompensateThread(final FileWatchedListener listener, final Path fullPath,
                            final Map<Path, List<Path>> createHistory) {
        this.listener = listener;
        this.fullPath = fullPath;
        this.createHistory = createHistory;
    }

    @Override
    public void run() {
        File[] files = fullPath.toFile().listFiles();
        if (files != null) {
            for (File file : files) {
                Path path = file.toPath();
                if (createHistory.get(fullPath).contains(path)) {
                    continue;
                }
                createHistory.get(fullPath).add(path);
                if (logger.isDebugEnabled()) {
                    logger.debug("compensate file -- parentPath is : {}, fullPath is : {} ", fullPath, path);
                }
                listener.onCreated(new CompensateWatchEvent(path), path, path.getFileName());
            }
        }
    }

    class CompensateWatchEvent implements WatchEvent {

        private Path path;

        public CompensateWatchEvent(Path path) {
            this.path = path;
        }

        @Override
        public Kind kind() {
            return new CompensateKind("ENTRY_CREATE", Path.class);
        }

        @Override
        public int count() {
            return 1;
        }

        @Override
        public Object context() {
            return path;
        }

    }

    class CompensateKind<T> implements WatchEvent.Kind {

        private final String name;
        private final Class<T> type;

        CompensateKind(String name, Class<T> type) {
            this.name = name;
            this.type = type;
        }

        @Override
        public String name() {
            return name;
        }

        @Override
        public Class type() {
            return type;
        }

        @Override
        public String toString() {
            return name();
        }
    }

}
