package com.tmax.hyperauth.authenticator;

import lombok.extern.slf4j.Slf4j;
import org.keycloak.models.AuthenticatorConfigModel;
import org.keycloak.models.UserModel;

import java.util.List;

@Slf4j
public class AuthenticatorUtil {
    public static String getAttributeValue(UserModel user, String attributeName) {
        String result = null;
        List<String> values = user.getAttribute(attributeName);
        if(values != null && values.size() > 0) {
            result = values.get(0);
        }
        return result;
    }

    public static String getConfigString(AuthenticatorConfigModel config, String configName) {
        return getConfigString(config, configName, null);
    }

    public static String getConfigString(AuthenticatorConfigModel config, String configName, String defaultValue) {
        String value = defaultValue;
        if (config != null && config.getConfig() != null) {
            // Get value
            value = config.getConfig().get(configName);
        }
        return value;
    }

    public static Long getConfigLong(AuthenticatorConfigModel config, String configName) {
        return getConfigLong(config, configName, null);
    }


    public static Long getConfigLong(AuthenticatorConfigModel config, String configName, Long defaultValue) {
        Long value = defaultValue;
        if (config != null && config.getConfig() != null) {
            // Get value
            Object obj = config.getConfig().get(configName);
            try {
                value = Long.valueOf((String) obj); // s --> ms
            } catch (NumberFormatException nfe) {
                log.error("NumberFormatException : Can not convert " + obj + " to a number.");
                log.error("Error Occurs!!", nfe);

            }
        }
        return value;
    }

    public static int getConfigInt(AuthenticatorConfigModel config, String configName, int defaultValue) {
        int value = defaultValue;
        if (config != null && config.getConfig() != null) {
            // Get value
            Object obj = config.getConfig().get(configName);
            try {
                value = Integer.valueOf((String) obj); // s --> ms
            } catch (NumberFormatException nfe) {
                log.error("NumberFormatException : Can not convert " + obj + " to a number int.");
                log.error("Error Occurs!!", nfe);
            }
        }
        return value;
    }
}
