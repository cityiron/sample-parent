package com.dbb.hyjal.boot.loader.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static com.dbb.hyjal.boot.loader.util.HyjalLocationUtils.EXCLUTE_JAR_PATTERN;
import static com.dbb.hyjal.boot.loader.util.HyjalLocationUtils.LOCATION;

/**
 * @author tc
 * @date 2019-10-10
 */
public class JarUtils {

    public static List<File> getFatJars() {
        String path = System.getProperty(LOCATION);

        File file = new File(path);

        if (!file.exists() || !file.isDirectory()) {
            throw new IllegalArgumentException(String
                .format("Loader jars path is not success, please check system properties [ hyjal.location ] : %s",
                    path));
        }

        File[] files = file.listFiles();

        return cleanNotJars(files);
    }

    private static List<File> cleanNotJars(File[] files) {
        List<File> result = new ArrayList<>();

        for (File file : files) {
            if (file.isFile() && file.getPath().endsWith(".jar")) {
                if (!exclude(file)) {
                    result.add(file);
                }
            }
        }

        return result;
    }

    private static boolean exclude(File file) {
        return exclude(file.getName());
    }

    private static boolean exclude(String name) {
        String pattern = System.getProperty(EXCLUTE_JAR_PATTERN);

        if (pattern == null) {
            return false;
        }

        int i = pattern.indexOf(" ");

        if (i > 0) {
            String[] patterns = pattern.split(" ");

            boolean b = false;

            for (String p : patterns) {
                b = Pattern.matches(p, name);

                if (b) {
                    break;
                }
            }

            return b;
        } else {
            return Pattern.matches(pattern, name);
        }
    }

    public static String getJarName(String fileName) {
        int i = fileName.lastIndexOf(".");
        if (i > 0) {
            return fileName.substring(0, i);
        }

        return fileName;
    }

}
