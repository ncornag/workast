package es.workast.utils;

import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

/**
 * @author Modified: Nicolás Cornaglia
 */
public class InitializingDataSourceFactoryBean extends AbstractFactoryBean {

    private Resource[] resourceInitScripts = new Resource[] {};
    private Resource[] resourceDestroyScripts = new Resource[] {};
    private DataSource dataSource;
    private String flagSchema;
    private String flagTable;
    private boolean initializeDatabase = false;
    private boolean initialized = false;
    private String info;
    private Map<String, Object> parameters;

    @javax.annotation.Resource(name = "appProperties")
    private Properties properties;

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(dataSource);
        super.afterPropertiesSet();
    }

    @Override
    protected Object createInstance() {
        if (!initialized) {
            if (initializeDatabase() || mustInitialize(dataSource)) {
                logger.info("Initializing datasource [" + getInfo() + "]");
                try {
                    DatabaseUtils.executeScripts(dataSource, resourceDestroyScripts, parameters, true, properties);
                } catch (Exception e) {
                    logger.warn("Could not execute destroy scripts", e);
                }
                DatabaseUtils.executeScripts(dataSource, resourceInitScripts, parameters, false, properties);
            }
            initialized = true;
            logger.info("Using datasource [" + getInfo() + "]");
        }
        return dataSource;
    }

    private boolean mustInitialize(DataSource dataSource) {
        if (flagTable != null) {
            return !DatabaseUtils.tableExists(dataSource, flagSchema, flagTable);
        } else {
            return false;
        }
    }

    @Override
    public Class<DataSource> getObjectType() {
        return DataSource.class;
    }

    public void setInitScripts(String initScripts) {
        resourceInitScripts = ResourceUtils.getStringAsResources(initScripts);
    }

    public void setInitScripts(String[] initScripts) {
        resourceInitScripts = ResourceUtils.getStringsAsResources(initScripts);
    }

    public void setDestroyScripts(String destroyScripts) {
        resourceDestroyScripts = ResourceUtils.getStringAsResources(destroyScripts);
    }

    public void setDestroyScripts(String[] destroyScripts) {
        resourceDestroyScripts = ResourceUtils.getStringsAsResources(destroyScripts);
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setFlagTable(String flagTable) {
        if (flagTable.indexOf(".") == -1) {
            this.flagSchema = null;
            this.flagTable = flagTable;
        } else {
            String[] values = flagTable.split("\\.");
            this.flagSchema = values[0];
            this.flagTable = values[1];
        }
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }

    public boolean initializeDatabase() {
        return initializeDatabase;
    }

    public void setInitializeDatabase(boolean initializeDatabase) {
        this.initializeDatabase = initializeDatabase;
    }

    /**
     * @param properties the properties to set
     */
    public void setProperties(Properties properties) {
        this.properties = properties;
    }

}
