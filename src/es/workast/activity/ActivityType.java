package es.workast.activity;

import java.util.Map;

/**
 * TODO Documentar
 * 
 * @author Nicolás Cornaglia
 */
public interface ActivityType {

    /**
     * @return
     */
    public String getType();

    /**
     * @return
     */
    public String getTitleTemplate();

    /**
     * @return
     */
    public String getBodyTemplate(Map<String, ? extends Object> params);

    /**
     * @return
     */
    public String getIconImage();

}