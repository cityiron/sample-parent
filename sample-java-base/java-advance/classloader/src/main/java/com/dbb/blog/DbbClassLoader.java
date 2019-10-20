package com.dbb.blog;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * @author tc
 * @date 2019-10-11
 */
public class DbbClassLoader extends ClassLoader {

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        String root
            = "/Users/tc/Documents/workspace/github/sample-parent/sample-java-base/java-advance/classloader/target"
            + "/classes/com/dbb/blog/";
        String file = root + name.substring(name.lastIndexOf(".") + 1) + ".class";
        try (InputStream is = new FileInputStream(file);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            int buffSize = 1024;
            byte[] buffer = new byte[buffSize];

            int length;
            while ((length = is.read(buffer)) != -1) {
                baos.write(buffer, 0, length);
            }

            byte[] bytes = baos.toByteArray();
            return defineClass(name, bytes, 0, bytes.length);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}


