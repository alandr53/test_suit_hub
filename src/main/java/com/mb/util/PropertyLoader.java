package com.mb.util;

import java.io.File;
import java.io.FileReader;
import java.util.Properties;
import java.util.logging.Logger;

public class PropertyLoader {

    private static final Logger logger = Logger.getLogger(PropertyLoader.class.getName());
    private static Properties localProperties = null;
    private static final String localPropertiesPath = "config/local.properties";
    private static Properties hubProperties = null;
    private static final String hubPropertiesPath = "config/hub.properties";

    static {
        File localPropertiesFile = new File(localPropertiesPath);
        if (localPropertiesFile.exists()) {
            try {
                localProperties = new Properties();
                localProperties.load(new FileReader(localPropertiesFile));
                logger.info("Using local properties file: ".concat(localPropertiesPath));
            } catch (Exception e) {
                throw new RuntimeException("Failed to load ".concat(localPropertiesPath), e);
            }
        }
        File marketPropertiesFile = new File(hubPropertiesPath);
        if (marketPropertiesFile.exists()) {
            try {
                hubProperties = new Properties();
                hubProperties.load(new FileReader(marketPropertiesFile));
                logger.info("Using hub properties file: ".concat(hubPropertiesPath));
            } catch (Exception e) {
                throw new RuntimeException("Failed to load ".concat(hubPropertiesPath), e);
            }
        }
    }

    public static String getStringProperty(String key, String def) {
        // 1. if the property is available in the environment e.g. by overriding from Jenkins or
        // via the IDE -> this has always precedence
        String systemValue = System.getProperty(key);
        if (systemValue != null) {
            String logValue = getLogValue(key, systemValue);
            logger.info("Override property [".concat(key).concat("] from system properties with value [".concat(logValue).concat("]")));
            return systemValue.trim();
        }
        // 2. if not in environment we check if a local property file is available and if the key is in there
        if (localProperties != null) {
            if (localProperties.containsKey(key)) {
                String logValue = getLogValue(key, localProperties.getProperty(key));
                logger.info("Override property [".concat(key).concat("] from local properties file with value [".concat(logValue).concat("]")));
                return localProperties.getProperty(key).trim();
            }
        }
        // 3. when there is a hub properties file and contains this key we use that one
        if (hubProperties != null) {
            if (hubProperties.containsKey(key)) {
                String logValue = getLogValue(key, hubProperties.getProperty(key));
                logger.info("Override property [".concat(key).concat("] from hub properties file with value [".concat(logValue).concat("]")));
                return hubProperties.getProperty(key).trim();
            }
        }
        // 4. at last we use the default value from the code
        return System.getProperty(key, def).trim();
    }

    public static int getIntProperty(String key, String def) {
        try {
            return Integer.parseInt(getStringProperty(key, def));
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Property value is not a valid integer: ".concat(e.getMessage()));
        }
    }

    public static boolean getBooleanProperty(String key, String def) {
        return Boolean.parseBoolean(getStringProperty(key, def));
    }

    private static String getLogValue(String key, String value) {
        if (key.toLowerCase().contains("password") || key.toLowerCase().contains("token")) {
            return "**********";
        }
        return value.trim();
    }
}
