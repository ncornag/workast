package es.workast.activity.xmpp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import es.workast.model.activity.Activity;
import es.workast.model.person.Person;
import es.workast.service.activity.broker.ActivitySubscriber;
import es.workast.service.activity.cache.ActivityAcceptStrategy;
import es.workast.service.activity.cache.FollowersStrategy;
import es.workast.service.activity.cache.GroupsStrategy;
import es.workast.service.person.PersonManager;
import es.workast.web.console.ConsoleController;

/**
 * @author Nicolás Cornaglia
 */
@Service
public class Broadcaster implements ActivitySubscriber {

    // ---------- Properties
    private Map<String, Person> connectedUsers = new HashMap<String, Person>();
    private List<ActivityAcceptStrategy> activityAcceptStrategies = new ArrayList<ActivityAcceptStrategy>();

    // ---------- Services

    private final Log logger = LogFactory.getLog(getClass());

    @Autowired
    private PersonManager personManager;
    @Autowired
    private ConsoleController consoleController; // FIXME Just kidding! Leave no trace of this when implement xmpp server

    // ---------- Methods

    @PostConstruct
    public void init() {
        activityAcceptStrategies.add(new FollowersStrategy());
        activityAcceptStrategies.add(new GroupsStrategy());
    }

    /**
     * @see es.workast.service.activity.broker.ActivitySubscriber#acceptActivity(es.workast.model.activity.Activity)
     */
    public void acceptActivity(Activity activity) {
        Map<String, Object> model = new HashMap<String, Object>();
        consoleController.activeActiveUsers(model);
        List<UserDetails> users = (List<UserDetails>) model.get("users");

        for (UserDetails details : users) {
            boolean accepted = false;
            for (ActivityAcceptStrategy activityAcceptStrategy : activityAcceptStrategies) {
                Person person = connectedUsers.get(details.getUsername());
                if (person == null) {
                    person = personManager.getPersonByUsername(details.getUsername());
                    connectedUsers.put(details.getUsername(), person);
                }
                if (activityAcceptStrategy.accept(person, activity)) {
                    accepted = true;
                    break;
                }
            }
            if (accepted) {
                logger.debug("---> Broadcasting from [" + activity.getPerson().getName() + "] to [" + details.getUsername() + "]: "
                        + activity.getRenderedMessage());
            }
        }

    }
}
