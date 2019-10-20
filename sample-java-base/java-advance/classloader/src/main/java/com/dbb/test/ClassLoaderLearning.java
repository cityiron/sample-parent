package com.dbb.test;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author tc
 * @date 2019-09-29
 */
public class ClassLoaderLearning {

    public static void main(String[] args) throws Exception {
        //loadFromJar();
        loadTest();
    }

    /**
     * jar:file:/Users/tc/Documents/software/apache-maven-3.0.5/repo/com/taobao/pandora/taobao-hsf
     * .sar-container/2018-08-release-final/taobao-hsf.sar-container-2018-08-release-final.jar!/lib/logger.api-0.1.4
     * .jar!/ file:/Users/tc/Documents/software/apache-maven-3.0.5/repo/com/gongdao/middleware/common-service/1.0
     * .0-SNAPSHOT/common-service-1.0.0-SNAPSHOT.jar
     *
     * @throws Exception
     */
    public static void loadFromJar() throws Exception {
        String url
            = "/Users/tc/Documents/software/apache-maven-3.0.5/repo/com/gongdao/middleware/common-service/1.0"
            + ".0-SNAPSHOT/common-service-1.0.0-SNAPSHOT.jar";

        JarFile jarFile = new JarFile(url);
        String file = url + "!/";
        URL runnerUrl = new URL("jar", "", -1, file);
        URL[] urls = new URL[] {runnerUrl};

        URLClassLoader cl = new URLClassLoader(urls, Thread.currentThread().getClass().getClassLoader());
        Class<?> runnerClass = cl.loadClass("com.gongdao.middleware.common.BackendRunner");

        Object o = runnerClass.newInstance();
    }

    public static void loadFromClass() {

    }

    public static void loadFromDirectory() {

    }

    public static void loadTest() {
        String path = "/Users/tc/Documents/workspace/gongdao/feilaifeng/second-software/target/second-software-1.0.0-SNAPSHOT.jar";//外部jar包的路径
        Set<Class<?>> classes = new LinkedHashSet<Class<?>>();//所有的Class对象
        Map<Class<?>, Annotation[]> classAnnotationMap = new HashMap<Class<?>, Annotation[]>();//每个Class对象上的注释对象
        Map<Class<?>, Map<Method, Annotation[]>> classMethodAnnoMap
            = new HashMap<Class<?>, Map<Method, Annotation[]>>();//每个Class对象中每个方法上的注释对象
        try {
            JarFile jarFile = new JarFile(new File(path));
            URL url = new URL("file:" + path);
            ClassLoader loader = new URLClassLoader(new URL[] {url});//自己定义的classLoader类，把外部路径也加到load路径里，使系统去该路经load对象
            Enumeration<JarEntry> es = jarFile.entries();
            while (es.hasMoreElements()) {
                JarEntry jarEntry = (JarEntry)es.nextElement();
                String name = jarEntry.getName();
                if (name != null && name.endsWith(".class")) {//只解析了.class文件，没有解析里面的jar包
                    //默认去系统已经定义的路径查找对象，针对外部jar包不能用
                    //Class<?> c = Thread.currentThread().getContextClassLoader().loadClass(name.replace("/", ".")
                    // .substring(0,name.length() - 6));
                    Class<?> c = loader.loadClass(
                        name.replace("/", ".").substring(0, name.length() - 6));//自己定义的loader路径可以找到
                    System.out.println(c);
                    classes.add(c);
                    Annotation[] classAnnos = c.getDeclaredAnnotations();
                    classAnnotationMap.put(c, classAnnos);
                    Method[] classMethods = c.getDeclaredMethods();
                    Map<Method, Annotation[]> methodAnnoMap = new HashMap<Method, Annotation[]>();
                    for (int i = 0; i < classMethods.length; i++) {
                        Annotation[] a = classMethods[i].getDeclaredAnnotations();
                        methodAnnoMap.put(classMethods[i], a);
                    }
                    classMethodAnnoMap.put(c, methodAnnoMap);
                }
            }
            System.out.println(classes.size());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

}
