package es.workast.activity.type;

import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;

import es.workast.activity.ActivityType;

/**
 * TODO Documentar
 * 
 * @author Nicolás Cornaglia
 */
public abstract class BaseActivityType implements ActivityType {

    public static String TITLE_KEY = "title";
    public static String BODY_KEY = "body";
    public static String ICON_KEY = "icon";

    // ---------- Properties

    private static final long serialVersionUID = 1L;

    // ---------- Services

    @Resource(name = "templates")
    private Properties templates;

    // ---------- Methods

    /**
     * @see es.workast.activity.ActivityType#getBodyTemplate()
     */
    @Override
    public String getTitleTemplate() {
        return templates.getProperty(getType().toLowerCase() + "." + TITLE_KEY);
    }

    /**
     * @see es.workast.activity.ActivityType#getBodyTemplate(java.util.Map)
     */
    @Override
    public String getBodyTemplate(Map<String, ? extends Object> params) {
        return templates.getProperty(getType().toLowerCase() + "." + BODY_KEY);
    }

    /**
     * @see es.workast.activity.ActivityType#getIconImageUrl()
     */
    @Override
    public String getIconImage() {
        return templates.getProperty(getType().toLowerCase() + "." + ICON_KEY);
    }

    // ---------- Accessors

    public Properties getTemplates() {
        return templates;
    }

    public void setTemplates(Properties templates) {
        this.templates = templates;
    }

}
