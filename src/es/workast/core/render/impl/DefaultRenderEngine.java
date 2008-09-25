package es.workast.core.render.impl;

import info.bliki.wiki.model.WikiModel;

import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.velocity.VelocityEngineUtils;

import es.workast.core.render.RenderEngine;

/**
 * TODO Documentar
 * 
 * @author Nicolás Cornaglia
 */
@Service("renderEngine")
public class DefaultRenderEngine implements RenderEngine {

    // ---------- Properties

    private WikiModel wikiModel;

    @Autowired
    private VelocityEngine velocityEngine;

    // ---------- Methods

    @PostConstruct
    public void init() throws Exception {
        wikiModel = new WikiModel("${image}", "${title}");
    }

    /**
     * @see es.workast.core.render.RenderEngine#render(java.lang.String, java.util.Map)
     */
    public String render(String template, Map<String, Object> params) throws Exception {
        // Make Context
        VelocityContext velocityContext = new VelocityContext(params);
        // Render 
        Writer out = new StringWriter();
        Velocity.evaluate(velocityContext, out, null, template);
        return out.toString();
    }

    /**
     * @see es.workast.core.render.RenderEngine#render(java.lang.String, java.lang.Object[])
     */
    public String render(String template, Object... args) throws Exception {
        if (args.length % 2 != 0) {
            throw new IllegalArgumentException("Only even 'args' please");
        }
        Map<String, Object> params = new HashMap<String, Object>();
        int i = 0;
        while (i < args.length) {
            params.put((String) args[i++], args[i++]);
        }
        return render(template, params);
    }

    /**
     * @see es.workast.core.render.RenderEngine#wikiRender(java.lang.String)
     */
    public String wikiRender(String wiki) {
        String htmlStr = wikiModel.render(wiki);
        if (htmlStr.startsWith("\n")) {
            htmlStr = htmlStr.substring(1);
        }
        if (htmlStr.startsWith("<p>") && htmlStr.endsWith("</p>")) {
            htmlStr = htmlStr.substring(3, htmlStr.length() - 4);
        }
        return htmlStr;
    }

    /**
     * @see es.workast.core.render.RenderEngine#wikiRenderWithStyles(java.lang.String)
     */
    public String wikiRenderWithStyles(String wiki) throws Exception {
        // Render
        String htmlStr = wikiRender(wiki);

        // Make velocity context
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("htmlStr", htmlStr);

        return VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "wikiPreview.vm", params);
    }

    // ---------- Accessors

}
