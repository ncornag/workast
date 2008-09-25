package es.workast.utils;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.core.Constants;
import org.springframework.util.ObjectUtils;
import org.springframework.util.PropertyPlaceholderHelper;
import org.springframework.util.StringValueResolver;
import org.springframework.util.PropertyPlaceholderHelper.PlaceholderResolver;

/**
 * @author Nicolás Cornaglia
 */
public class RecursivePropertiesFactoryBean extends PropertyPlaceholderConfigurer implements FactoryBean, InitializingBean {

    private boolean singleton = true;
    private Object singletonInstance;
    private Properties mergedProps;

    private boolean ignoreUnresolvablePlaceholders = false;
    private int systemPropertiesMode = SYSTEM_PROPERTIES_MODE_FALLBACK;
    private String nullValue;
    private static final Constants constants = new Constants(PropertyPlaceholderConfigurer.class);
    private StringValueResolver valueResolver;

    public final void setSingleton(boolean singleton) {
        this.singleton = singleton;
    }

    public final boolean isSingleton() {
        return this.singleton;
    }

    public final void afterPropertiesSet() throws IOException {
        if (this.singleton) {
            this.singletonInstance = createInstance();
        }
    }

    public final Object getObject() throws IOException {
        if (this.singleton) {
            return this.singletonInstance;
        } else {
            return createInstance();
        }
    }

    public Class getObjectType() {
        return Properties.class;
    }

    /**
     * @see org.springframework.beans.factory.config.PropertyPlaceholderConfigurer#setIgnoreUnresolvablePlaceholders(boolean)
     */
    @Override
    public void setIgnoreUnresolvablePlaceholders(boolean ignoreUnresolvablePlaceholders) {
        this.ignoreUnresolvablePlaceholders = ignoreUnresolvablePlaceholders;
    }

    /**
     * @see org.springframework.beans.factory.config.PropertyPlaceholderConfigurer#setSystemPropertiesModeName(java.lang.String)
     */
    @Override
    public void setSystemPropertiesModeName(String constantName) throws IllegalArgumentException {
        this.systemPropertiesMode = constants.asNumber(constantName).intValue();
    }

    /**
     * @see org.springframework.beans.factory.config.PropertyResourceConfigurer#postProcessBeanFactory(org.springframework.beans.factory.config.ConfigurableListableBeanFactory)
     */
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

        valueResolver = new PlaceholderResolvingStringValueResolver(mergedProps);

        convertProperties(mergedProps);

        for (Enumeration e = mergedProps.keys(); e.hasMoreElements(); /**/) {
            String key = (String) e.nextElement();
            String value = mergedProps.getProperty(key);
            String newValue = resolveStringValue(value);
            if (!ObjectUtils.nullSafeEquals(value, newValue)) {
                mergedProps.setProperty(key, newValue);
            }
        }

        return mergedProps;
    }

    protected String resolveStringValue(String strVal) {
        if (this.valueResolver == null) {
            throw new IllegalStateException("No StringValueResolver specified - pass a resolver "
                    + "object into the constructor or override the 'resolveStringValue' method");
        }
        String resolvedValue = this.valueResolver.resolveStringValue(strVal);
        // Return original String if not modified.
        return (strVal.equals(resolvedValue) ? strVal : resolvedValue);
    }

    private class PlaceholderResolvingStringValueResolver implements StringValueResolver {

        private final PropertyPlaceholderHelper helper;

        private final PlaceholderResolver resolver;

        public PlaceholderResolvingStringValueResolver(Properties props) {
            this.helper = new PropertyPlaceholderHelper(PropertyPlaceholderConfigurer.DEFAULT_PLACEHOLDER_PREFIX,
                    PropertyPlaceholderConfigurer.DEFAULT_PLACEHOLDER_SUFFIX, PropertyPlaceholderConfigurer.DEFAULT_VALUE_SEPARATOR,
                    ignoreUnresolvablePlaceholders);
            this.resolver = new PropertyPlaceholderConfigurerResolver(props);
        }

        public String resolveStringValue(String strVal) throws BeansException {
            String value = this.helper.replacePlaceholders(strVal, this.resolver);
            return (value.equals(nullValue) ? null : value);
        }
    }

    private class PropertyPlaceholderConfigurerResolver implements PlaceholderResolver {

        private final Properties props;

        private PropertyPlaceholderConfigurerResolver(Properties props) {
            this.props = props;
        }

        public String resolvePlaceholder(String placeholderName) {
            return RecursivePropertiesFactoryBean.this.resolvePlaceholder(placeholderName, props, systemPropertiesMode);
        }
    }

}
