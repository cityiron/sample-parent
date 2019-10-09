package com.dbb;

import java.nio.file.Path;
import java.nio.file.WatchEvent;

/**
 * 文件监听类，做业务扩展
 *
 * @author tc
 * @date 2019-09-29
 */
public interface FileWatchedListener {

    void onCreated(WatchEvent<Path> watchEvent, Path fullPath, Path filePath);

    void onDeleted(WatchEvent<Path> watchEvent, Path fullPath, Path filePath);

    void onModified(WatchEvent<Path> watchEvent, Path fullPath, Path filePath);

    void onOverflowed(WatchEvent<Path> watchEvent, Path fullPath, Path filePath);

}
