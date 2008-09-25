package es.workast.render;

import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.stereotype.Service;


/**
 * TODO Documentar
 * 
 * @author Nicolás Cornaglia
 */
@Service("renderEngine")
public class VelocityRenderEngine implements RenderEngine {

    // ---------- Properties

    private static VelocityEngine velocityEngine = new VelocityEngine();

    // ---------- Methods
    /**
     * @see es.workast.render.RenderEngine#render(java.lang.String, java.util.Map)
     */
    public String render(String template, Map<String, ? extends Object> params) throws Exception {
        // Make Context
        VelocityContext velocityContext = new VelocityContext(params);
        // Render Title
        Writer out = new StringWriter();
        velocityEngine.evaluate(velocityContext, out, null, template);
        return out.toString();
    }

    public String render(String template, Object... args) throws Exception {
        if (args.length < 1 || (args.length % 2 != 0)) {
            throw new IllegalArgumentException("Only even 'args' please");
        }
        Map<String, Object> params = new HashMap<String, Object>();
        int i = 0;
        while (i < args.length) {
            params.put((String) args[i++], args[i++]);
        }
        return render(template, params);
    }

    // ---------- Accessors

}
