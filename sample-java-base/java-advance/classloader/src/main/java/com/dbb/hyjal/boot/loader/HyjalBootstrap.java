package com.dbb.hyjal.boot.loader;

import com.dbb.hyjal.boot.loader.archive.Archive;
import com.dbb.hyjal.boot.loader.archive.JarFileArchive;
import com.dbb.hyjal.boot.loader.util.ClassLoaderUtils;
import com.dbb.hyjal.boot.loader.util.HyjalLocationUtils;
import com.dbb.hyjal.boot.loader.util.JarUtils;
import com.dbb.hyjal.boot.loader.util.LogPrintUtils;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Properties;
import java.util.function.Consumer;

import static org.springframework.boot.context.config.ConfigFileApplicationListener.CONFIG_LOCATION_PROPERTY;

/**
 * @author tc
 * @date 2019-10-10
 */
public class HyjalBootstrap {

    /**
     * pandora method
     */
    private static Method initApplication;

    /**
     * 启动Hyjal。通常在应用的main函数的第一行调用。
     *
     * @param applicationBuilder parent
     * @param args               应用main函数的args
     */
    public static void run(SpringApplicationBuilder applicationBuilder, String[] args) {
        ClassLoader old = Thread.currentThread().getContextClassLoader();
        init(old);

        HyjalLocationUtils.setHyjalLocationSystemProperty();
        List<File> fatJars = JarUtils.getFatJars();

        LogPrintUtils.printJar(fatJars);

        sync(fatJars, old, applicationBuilder, args);
    }

    private static void init(ClassLoader classLoader) {
        applicationModelInit(classLoader);
    }

    private static void applicationModelInit(ClassLoader classLoader) {
        try {
            Class<?> applicationModelFactory = classLoader.loadClass("com.taobao.hsf.model.ApplicationModelFactory");
            // pandora container
            initApplication = applicationModelFactory.getDeclaredMethod("initApplication", String.class,
                ClassLoader.class, boolean.class);
            initApplication.setAccessible(true);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    private static void sync(List<File> fatJars, ClassLoader old, SpringApplicationBuilder applicationBuilder,
                             String[] args) {

        try {
            for (File file : fatJars) {
                try {
                    JarFileArchive archive = new JarFileArchive(file);
                    JarLauncher jarLauncher = new JarLauncher(archive);
                    List<Archive> classPathArchives = jarLauncher.getClassPathArchives();

                    LaunchedURLClassLoader launchedURLClassLoader = new LaunchedURLClassLoader(
                        ClassLoaderUtils.getUrls(classPathArchives),
                        old);

                    Thread.currentThread().setContextClassLoader(launchedURLClassLoader);

                    String mainClass = archive.getManifest().getMainAttributes().getValue("Start-Class");
                    Class<?> startClass = launchedURLClassLoader.loadClass(mainClass);

                    System.out.println(
                        String.format("fatJar *[%s]* start load", file.getName()));

                    String jarName = JarUtils.getJarName(file.getName());

                    // pandora container
                    initApplication.invoke(null, jarName, launchedURLClassLoader, true);

                    Properties properties = new Properties();
                    //properties.setProperty(CONFIG_LOCATION_PROPERTY, archive.getUrl().toString());
                    properties.setProperty("child.jar.name", jarName);

                    applicationBuilder.child(startClass).profiles(jarName)
                        .properties(properties)
                        .web(true).run(args);
                } catch (Exception e) {
                    System.out.println(
                        String.format("fatJar *[%s]* load fail", file.getName()));
                    throw e;
                } finally {
                    System.out.println(
                        String.format("fatJar *[%s]* load over", file.getName()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Thread.currentThread().setContextClassLoader(old);
        }
    }

    @Deprecated
    private static void async(List<File> fatJars, ClassLoader old, SpringApplicationBuilder applicationBuilder,
                              String[] args) {
        fatJars.parallelStream()
            .forEach(loadArchiveToURLs(file -> {
                try (JarFileArchive archive = new JarFileArchive(file)) {
                    JarLauncher jarLauncher = new JarLauncher(archive);
                    List<Archive> classPathArchives = jarLauncher.getClassPathArchives();

                    LaunchedURLClassLoader launchedURLClassLoader = new LaunchedURLClassLoader(
                        ClassLoaderUtils.getUrls(classPathArchives),
                        old);
                    //launchedURLClassLoader.addThisToParentClassLoader(systemClassLoader);

                    Thread.currentThread().setContextClassLoader(launchedURLClassLoader);

                    String mainClass = archive.getManifest().getMainAttributes().getValue("Start-Class");
                    Class<?> startClass = launchedURLClassLoader.loadClass(mainClass);

                    System.out.println(
                        String.format("HyjalBoot child module *[%s]* start load", file.getName()));

                    String jarName = JarUtils.getJarName(file.getName());

                    // pandora container
                    initApplication.invoke(null, jarName, launchedURLClassLoader, true);

                    Properties properties = new Properties();
                    properties.setProperty(CONFIG_LOCATION_PROPERTY, archive.getUrl().toString());

                    applicationBuilder.child(startClass).profiles(jarName).properties(properties)
                        .web(true).run(args);
                } finally {
                    Thread.currentThread().setContextClassLoader(old);
                }
            }));
    }

    private static <T> Consumer<T> loadArchiveToURLs(ConsumerException<T, Exception> c) {
        return
            r -> {
                try {
                    c.accept(r);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println(
                        String.format("HyjalBoot child module *[%s]* start load", r));
                }
            };
    }

}
