package com.dbb.hyjal.boot.loader.util;

/**
 * @author tc
 * @date 2019-10-10
 */
public class HyjalLocationUtils {

    protected final static String LOCATION = "hyjal.location";

    protected final static String EXCLUTE_JAR_PATTERN = "hyjal.exclude";

    private final static String DEFAULT_PATH;

    static {
        DEFAULT_PATH = System.getProperty("user.dir") + "/lib";
    }

    public static void setHyjalLocationSystemProperty() {
        if (System.getProperty(LOCATION) == null) {
            System.setProperty(LOCATION, DEFAULT_PATH);
        }
    }

}
