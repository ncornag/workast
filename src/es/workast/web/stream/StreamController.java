package es.workast.web.stream;

import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import es.workast.core.render.RenderEngine;
import es.workast.model.person.Person;
import es.workast.service.activity.ActivityManager;
import es.workast.service.person.PersonManager;

/**
 * @author Nicolás Cornaglia
 */
@Controller
@RequestMapping("/data/stream")
public class StreamController {

    // ---------- Properties

    public static final String POLL_INTERVAL_KEY = "streamManager.pollInterval";

    private Long pollInterval = 0L;

    // ---------- Services

    private final Log logger = LogFactory.getLog(getClass());

    @Autowired
    private PersonManager personManager;
    @Autowired
    private ActivityManager activityManager;
    @Autowired
    private RenderEngine renderEngine;

    private Properties properties;

    // ---------- Methods

    @PostConstruct
    public void init() {
        if (pollInterval == 0) {
            pollInterval = new Long(getProperties().getProperty(POLL_INTERVAL_KEY));
        }
    }

    // ---------- Rest URLs

    @RequestMapping("/mystream")
    public String myStream(StreamFilter streamFilter, BindingResult result, Model model) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("Service called: stream/mystream/" + streamFilter.getType());
        }
        Person currentPerson = personManager.getCurrentPerson();
        Stream stream = new Stream(currentPerson, activityManager.getPersonStreamActivities(currentPerson, streamFilter), getPollInterval());

        model.addAttribute("stream", stream);
        return "json/stream/stream.json";
    }

    @RequestMapping("/global")
    public String globalStream(StreamFilter streamFilter, BindingResult result, Model model) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("Service called: stream/global/" + streamFilter.getType());
        }
        Person currentPerson = personManager.getCurrentPerson();
        Stream stream = new Stream(currentPerson, activityManager.getGlobalActivities(currentPerson, streamFilter), getPollInterval());

        model.addAttribute("stream", stream);
        return "json/stream/stream.json";
    }

    @RequestMapping("/group/{id}")
    public String groupStream(@PathVariable("id") Long groupId, StreamFilter streamFilter, BindingResult result, Model model) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("Service called: stream/group/" + groupId + "/" + streamFilter.getType());
        }
        Person currentPerson = personManager.getCurrentPerson();
        Stream stream = new Stream(currentPerson, activityManager.getGroupActivities(currentPerson, groupId, streamFilter), getPollInterval());

        model.addAttribute("stream", stream);
        return "json/stream/stream.json";
    }

    @RequestMapping("/profile/{id}")
    public String getPersonStream(@PathVariable("id") Long personId, StreamFilter streamFilter, BindingResult result, Model model) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("Service called: stream/person/" + streamFilter.getType());
        }
        Person person = personManager.getPerson(personId);
        Person currentPerson = personManager.getCurrentPerson();
        Stream stream = new Stream(person, activityManager.getProfileStreamActivities(person, currentPerson, streamFilter), getPollInterval());

        model.addAttribute("stream", stream);
        return "json/stream/stream.json";
    }

    // ---------- Accessors

    @Resource(name = "appProperties")
    public void setAppProperties(Properties appProperties) {
        properties = appProperties;
    }

    public RenderEngine getRenderEngine() {
        return renderEngine;
    }

    public void setRenderEngine(RenderEngine renderEngine) {
        this.renderEngine = renderEngine;
    }

    public ActivityManager getActivityManager() {
        return activityManager;
    }

    public void setActivityManager(ActivityManager activityManager) {
        this.activityManager = activityManager;
    }

    public PersonManager getPersonManager() {
        return personManager;
    }

    public void setPersonManager(PersonManager personManager) {
        this.personManager = personManager;
    }

    public Long getPollInterval() {
        return pollInterval;
    }

    public void setPollInterval(Long pollInterval) {
        this.pollInterval = pollInterval;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

}