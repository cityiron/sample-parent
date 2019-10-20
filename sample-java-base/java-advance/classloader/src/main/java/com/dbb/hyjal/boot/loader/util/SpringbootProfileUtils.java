package com.dbb.hyjal.boot.loader.util;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.core.env.AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME;

/**
 * @author tc
 * @date 2019-10-10
 */
public class SpringbootProfileUtils {

    private static Map<String, String> profileMap = new HashMap<>();

    static {
        profileMap.put("jiuding.jar", "jiuding");
    }

    protected final static String PROFILE = ACTIVE_PROFILES_PROPERTY_NAME;

    public static void setSpringProfileSystemProperty(String profile) {
        System.setProperty(PROFILE, profileMap.get(profile));
    }

}
