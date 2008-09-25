package es.workast.utils;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

/**
 * @author Nicolás Cornaglia
 */
public class ResourceUtils {

    private static final String CLASSPATHPREFIX = "classpath:";

    /**
     * Hidden constructor
     */
    private ResourceUtils() {
    }

    public static Resource[] getStringAsResources(String strings) {
        return getStringsAsResources(strings.split(","));
    }

    public static Resource[] getStringsAsResources(String[] strings) {
        Resource[] resources = null;
        if (strings == null) {
            resources = new Resource[] {};
        } else {
            resources = new Resource[strings.length];
            for (int i = 0; i < strings.length; i++) {
                if (strings[i].startsWith(CLASSPATHPREFIX)) {
                    resources[i] = new ClassPathResource(strings[i].substring(CLASSPATHPREFIX.length()));
                } else {
                    resources[i] = new FileSystemResource(strings[i]);
                }
            }
        }
        return resources;
    }
}
