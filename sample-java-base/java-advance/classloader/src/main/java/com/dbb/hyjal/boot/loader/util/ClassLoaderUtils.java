package com.dbb.hyjal.boot.loader.util;

import com.dbb.hyjal.boot.loader.archive.Archive;

import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;

/**
 * @author tc
 * @date 2019-10-11
 */
public class ClassLoaderUtils {

    @SuppressWarnings({"restriction", "unchecked"})
    public static URL[] getUrls(ClassLoader classLoader) {
        if (classLoader instanceof URLClassLoader) {
            return ((URLClassLoader)classLoader).getURLs();
        }

        // jdk9
        if (classLoader.getClass().getName().startsWith("jdk.internal.loader.ClassLoaders$")) {
            try {
                Field field = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
                field.setAccessible(true);
                sun.misc.Unsafe unsafe = (sun.misc.Unsafe)field.get(null);

                // jdk.internal.loader.ClassLoaders.AppClassLoader.ucp
                Field ucpField = classLoader.getClass().getDeclaredField("ucp");
                long ucpFieldOffset = unsafe.objectFieldOffset(ucpField);
                Object ucpObject = unsafe.getObject(classLoader, ucpFieldOffset);

                // jdk.internal.loader.URLClassPath.path
                Field pathField = ucpField.getType().getDeclaredField("path");
                long pathFieldOffset = unsafe.objectFieldOffset(pathField);
                ArrayList<URL> path = (ArrayList<URL>)unsafe.getObject(ucpObject, pathFieldOffset);

                return path.toArray(new URL[path.size()]);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    /***
     *
     * @param clazz
     * @return URL
     */
    public static URL locateURL(Class<?> clazz) {
        if (null == clazz) {
            return null;
        }
        ProtectionDomain pd = clazz.getProtectionDomain();
        if (null == pd) {
            return null;
        }
        CodeSource cs = pd.getCodeSource();
        if (null == cs) {
            return null;
        }

        return cs.getLocation();
    }

    public static URL[] getUrls(ClassLoader classLoader, List<Archive> archives) throws Exception {
        List<URL> result = new ArrayList<>();

        URL[] urls = getUrls(classLoader);

        for (URL url : urls) {
            result.add(url);
        }

        for (Archive pathArchive : archives) {
            result.add(pathArchive.getUrl());
        }

        return result.toArray(new URL[result.size()]);
    }

    public static URL[] getUrls(List<Archive> archives) throws Exception {
        List<URL> result = new ArrayList<>();

        for (Archive pathArchive : archives) {
            result.add(pathArchive.getUrl());
        }

        return result.toArray(new URL[result.size()]);
    }

}
