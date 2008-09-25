package es.workast.render;

import java.util.Map;

/**
 * TODO Documentar
 * 
 * @author Nicolás Cornaglia
 */
public interface RenderEngine {

    /**
     * @param template
     * @param params
     * @return
     * @throws Exception
     */
    public abstract String render(String template, Map<String, ? extends Object> params) throws Exception;

    /**
     * @param template
     * @param args
     * @return
     * @throws Exception
     */
    public String render(String template, Object... args) throws Exception;

}