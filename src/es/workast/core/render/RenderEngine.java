package es.workast.core.render;

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
    String render(String template, Map<String, Object> params) throws Exception;

    /**
     * @param template
     * @param args
     * @return
     * @throws Exception
     */
    String render(String template, Object... args) throws Exception;

    /**
     * Transforms wiki text into html
     * 
     * @param wiki
     * @return
     * @throws Exception
     */
    String wikiRender(String wiki);

    /**
     * Transforms wiki text into html and apply styles
     * 
     * @param wiki
     * @return
     * @throws Exception
     */
    String wikiRenderWithStyles(String wiki) throws Exception;

}