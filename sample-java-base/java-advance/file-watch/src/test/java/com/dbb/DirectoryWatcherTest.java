package com.dbb;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.util.concurrent.atomic.AtomicInteger;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

/**
 * @author tc
 * @date 2019-09-29
 */
public class DirectoryWatcherTest {

    @Test
    public void test10() throws IOException {
        AtomicInteger count = new AtomicInteger(1);

        DirectoryWatcher directoryWatcher = new DirectoryWatcher();
        directoryWatcher.registerAll("/Users/tc/Documents/gongdao/pandora");
        directoryWatcher.processEvents(new FileWatchedListener() {

            @Override
            public void onCreated(WatchEvent<Path> watchEvent, Path fullPath, Path filePath) {
                System.out.println(String
                    .format("created -- event : %s, filePath : %s, fullPath : %s", watchEvent.kind().name(), filePath,
                        fullPath));
            }

            @Override
            public void onDeleted(WatchEvent<Path> watchEvent, Path fullPath, Path filePath) {
                System.out.println(String
                    .format("deleted -- event : %s, filePath : %s, fullPath : %s", watchEvent.kind().name(), filePath,
                        fullPath));
            }

            @Override
            public void onModified(WatchEvent<Path> watchEvent, Path fullPath, Path filePath) {
                System.out.println(String
                    .format("modified -- event : %s, filePath : %s, fullPath : %s", watchEvent.kind().name(), filePath,
                        fullPath));
            }

            @Override
            public void onOverflowed(WatchEvent<Path> watchEvent, Path fullPath, Path filePath) {
                System.out.println(String
                    .format("overflowed -- event : %s, filePath : %s, fullPath : %s", watchEvent.kind().name(),
                        filePath,
                        fullPath));
            }

        });
        System.in.read();
    }

    @Test
    public void test20() {
        String p1 = "/Users/tc/Documents/gongdao/pandora";
        String p2 = "/Users/tc/Documents/gongdao/pandora/alitomcat-env-setup.sh";
        Path path = Paths.get(p1);
        Path path2 = Paths.get(p2);
        System.out.println(path.toString());
        System.out.println(path2.toString());
    }

}