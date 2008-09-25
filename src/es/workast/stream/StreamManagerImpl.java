package es.workast.stream;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.workast.activity.Activity;
import es.workast.activity.ActivityManager;
import es.workast.activity.ActivityType;
import es.workast.person.Person;
import es.workast.person.PersonManager;
import es.workast.render.RenderEngine;
import es.workast.utils.SecurityUtils;

/**
 * TODO Documentar
 * 
 * @author Nicolás Cornaglia
 */
@Service("streamManager")
@Path("stream")
public class StreamManagerImpl implements StreamManager {

    // ---------- Properties

    // ---------- Services

    private final Log logger = LogFactory.getLog(getClass());

    @Autowired
    private PersonManager personManager;
    @Autowired
    private ActivityManager activityManager;
    @Autowired
    private RenderEngine renderEngine;

    // ---------- Methods

    /**
     * @see es.workast.stream.StreamManager#getStream(java.lang.Long)
     */
    public List<Activity> getStream(Long groupId) throws Exception {

        String username = SecurityUtils.getCurrentUser().getUsername();
        Person currentPerson = personManager.getByUsername(username);

        // Get stream
        List<Activity> stream = activityManager.getActivities(currentPerson.getId(), groupId, 0, 50);

        // Format each Activity
        for (Activity activity : stream) {
            if (!activity.wasRendered()) {
                // Get Type
                ActivityType activityType = activityManager.getActivityType(activity.getType());
                if (activityType == null) {
                    logger.error("ActivityType [" + activityType + "] not found for Activity [" + activity.getId() + "]");
                    break;
                }

                activity.render(renderEngine, activityType);
            }
        }
        return stream;
    }

    @GET
    @Path("/{id}")
    @Produces("application/json")
    public Stream getStreamJson(@PathParam("id") Long groupId) throws Exception {
        Stream stream = new Stream();
        stream.setStream(getStream(groupId));
        return stream;
    }

    // ---------- Accessors

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

}
