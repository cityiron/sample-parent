package com.dbb.hyjal.boot.loader;

import com.dbb.hyjal.boot.loader.archive.Archive;
import com.dbb.hyjal.boot.loader.archive.JarFileArchive;
import com.dbb.hyjal.boot.loader.util.ClassLoaderUtils;
import org.junit.Test;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author tc
 * @date 2019-10-10
 */
public class JarLauncherTest {

    @Test
    public void archivedJarCommonBizFatJar() throws Exception {
        String path = "";
        File jarRoot = new File(path);
        try (JarFileArchive archive = new JarFileArchive(jarRoot)) {
            JarLauncher launcher = new JarLauncher(archive);
            List<Archive> classPathArchives = launcher.getClassPathArchives();

            // getClass().getClassLoader() appClassLoader
            LaunchedURLClassLoader launchedURLClassLoader = new LaunchedURLClassLoader(
                ClassLoaderUtils.getUrls(getClass().getClassLoader(), classPathArchives),
                getClass().getClassLoader().getParent());

            Class<?> startClass = launchedURLClassLoader.loadClass("");

            launchedURLClassLoader.addThisToParentClassLoader(getClass().getClassLoader());

            startClass = launchedURLClassLoader.loadClass("");

            for (Archive classPathArchive : classPathArchives) {
                classPathArchive.close();
            }
        }
    }

}
