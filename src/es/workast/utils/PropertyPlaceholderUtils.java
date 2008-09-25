package es.workast.utils;

import java.util.Properties;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

/**
 * @author Nicolás Cornaglia
 */
public class PropertyPlaceholderUtils extends PropertyPlaceholderConfigurer {

    /**
     * @param strVal
     * @param props
     * @param visitedPlaceholders
     * @return
     */
    public static String resolve(String strVal, Properties props) {
        PropertyPlaceholderUtils p = new PropertyPlaceholderUtils();
        return p.resolveInternal(strVal, props);
    }

    private String resolveInternal(String strVal, Properties props) {
        return resolvePlaceholder(strVal, props);
    }

}
