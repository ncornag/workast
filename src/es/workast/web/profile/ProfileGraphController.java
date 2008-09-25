package es.workast.web.profile;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/profilegraph")
public class ProfileGraphController {

    // ---------- Methods

    @RequestMapping("/net")
    public String netGraphHandler(@RequestParam("personId") Long personId, Map<String, Object> model) {
        model.put("model", buildJSONResult(personId));
        return "profilegraph/net";
    }

    private Map<String, String> buildJSONResult(Long personId) {
        Map<String, String> results = new HashMap<String, String>();
        results.put("personId", "{x:" + personId.toString() + "}");
        return results;
    }

}