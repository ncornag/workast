package es.workast.web.tag;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import es.workast.model.person.Person;
import es.workast.service.person.PersonManager;
import es.workast.service.tag.TagManager;

/**
 * TODO Documentar
 * 
 * @author Nicolás Cornaglia
 */
@Controller
@RequestMapping("/data/tag")
public class TagController {

    // ---------- Services

    private final Log logger = LogFactory.getLog(getClass());

    @Autowired
    private PersonManager personManager;

    @Autowired
    private TagManager tagManager;

    // ---------- URLs

    /**
     * Get current Person tag statistics 
     * 
     * @param person
     * @return
     */
    @RequestMapping("/mycloud")
    public String getCurrentPersonTagStatistics(Model model) {
        if (logger.isDebugEnabled()) {
            logger.debug("Service called: tag/mycloud");
        }
        Person person = getPersonManager().getCurrentPerson();
        model.addAttribute("tags", getTagManager().getTagStatisticsByPerson(person));
        return "json/tag/cloud.json";
    }

    /**
     * Get all tag statistics 
     * 
     * @param person
     * @return
     */
    @RequestMapping("/cloud")
    public String getTagStatistics(Model model) {
        if (logger.isDebugEnabled()) {
            logger.debug("Service called: tag/cloud");
        }
        model.addAttribute("tags", getTagManager().getTagStatistics());
        return "json/tag/cloud.json";
    }

    // ---------- Accessors

    public PersonManager getPersonManager() {
        return personManager;
    }

    public void setPersonManager(PersonManager personManager) {
        this.personManager = personManager;
    }

    /**
     * @return the tagManager
     */
    public TagManager getTagManager() {
        return tagManager;
    }

    /**
     * @param tagManager the tagManager to set
     */
    public void setTagManager(TagManager tagManager) {
        this.tagManager = tagManager;
    }

}
