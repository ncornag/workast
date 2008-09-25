package es.workast.web.render;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.workast.core.render.RenderEngine;

/**
 *
 * @author Nicolás Cornaglia
 */
@Controller
@RequestMapping("/data/render")
public class RenderController {

    // ---------- Services

    @Autowired
    private RenderEngine renderEngine;

    /**
     * @see es.workast.core.render.RenderEngine#wikiRenderWithStyles(java.lang.String)
     */
    @RequestMapping("/wiki")
    public String wikiRenderWithStyles(@RequestParam("data") String data, Model model) throws Exception {
        model.addAttribute("data", renderEngine.wikiRenderWithStyles(data));
        return "render/render";
    }

}
