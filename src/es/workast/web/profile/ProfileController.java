package es.workast.web.profile;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.workast.service.person.PersonManager;

@Controller
public class ProfileController {

    // ---------- Services

    private final Log logger = LogFactory.getLog(getClass());

    @Autowired
    private PersonManager personManager;

    // ---------- URLs

    @RequestMapping("/profile/{personId}")
    public String showHandler(@PathVariable("personId") Long personId, Map<String, Object> model) {
        model.put("model", buildJSONResult(personId));
        return "profile/show";
    }

    @RequestMapping("/profile/{personId}/net")
    public String netHandler(@PathVariable("personId") Long personId, Map<String, Object> model) {
        model.put("model", buildJSONResult(personId));
        model.put("personId", personId);
        return "profile/net";
    }

    @RequestMapping("/profile/edit")
    public String editHandler(Map<String, Object> model) {
        model.put("model", buildJSONResult(personManager.getCurrentPerson().getId()));
        return "profile/edit";
    }

    /**
     * Hibernate query implemented. Must be changed to use Hibernate Search.
     * 
     * @param query
     * @param model
     * @return
     */
    @RequestMapping("/data/profile/search")
    public String searchHandler(@RequestParam("q") String query, Model model) {
        if (logger.isDebugEnabled()) {
            logger.debug("Service called [GET]: persons/search [" + query + "]");
        }
        model.addAttribute("persons", personManager.findPersons(query));
        return "json/profile/searchResults.json";

    }

    // ---------- Helpers

    private Map<String, String> buildJSONResult(Long personId) {
        Map<String, String> results = new HashMap<String, String>();
        results.put("profileId", "{x:" + personId.toString() + "}");
        return results;
    }

}