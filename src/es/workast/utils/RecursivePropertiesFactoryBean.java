package es.workast.utils;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

/**
 * @author Nicolás Cornaglia
 */
public class RecursivePropertiesFactoryBean extends PropertyPlaceholderConfigurer implements FactoryBean, InitializingBean {

    private boolean singleton = true;

    private Object singletonInstance;

    private Properties mergedProps;

    /**
     * Set whether a shared 'singleton' Properties instance should be
     * created, or rather a new Properties instance on each request.
     * <p>Default is "true" (a shared singleton).
     */
    public final void setSingleton(boolean singleton) {
        this.singleton = singleton;
    }

    @Override
    public final boolean isSingleton() {
        return this.singleton;
    }

    @Override
    public final void afterPropertiesSet() throws IOException {
        if (this.singleton) {
            this.singletonInstance = createInstance();
        }
    }

    @Override
    public final Object getObject() throws IOException {
        if (this.singleton) {
            return this.singletonInstance;
        } else {
            return createInstance();
        }
    }

    @Override
    public Class getObjectType() {
        return Properties.class;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        processProperties(beanFactory, mergedProps);
    }

    /**
     * Load properties & resolve placeholders
     * 
     * @return
     * @throws IOException
     */
    protected Object createInstance() throws IOException {

        mergedProps = mergeProperties();

        // Convert the merged properties, if necessary.
        convertProperties(mergedProps);

        for (Enumeration e = mergedProps.keys(); e.hasMoreElements(); /**/) {
            String key = (String) e.nextElement();
            String value = mergedProps.getProperty(key);
            String newValue = parseStringValue(value, mergedProps, new HashSet());
            //System.out.println(key + "[" + value + "]=" + newValue);
            mergedProps.put(key, newValue);
        }

        return mergedProps;
    }

}
