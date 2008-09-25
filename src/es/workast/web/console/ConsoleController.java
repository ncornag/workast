package es.workast.web.console;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;

import org.hibernate.SessionFactory;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.metadata.CollectionMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.ConfigurableWebApplicationContext;

/**
 * @author Nicolás Cornaglia
 */
@Controller
@RequestMapping("/console")
public class ConsoleController {

    @Autowired(required = false)
    private ServletContext servletContext;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private SessionFactory sessionFactory;

    @RequestMapping("/evictSecondLevelCache")
    public String evictSecondLevelCache(Map<String, Object> model) {
        evictSecondLevelCache(sessionFactory);
        return "stream/global";
    }

    @RequestMapping("/refreshContext")
    public String refreshContext(Map<String, Object> model) {
        ((ConfigurableWebApplicationContext) applicationContext).refresh();
        return "stream/global";
    }

    @SuppressWarnings("unchecked")
    @RequestMapping("/activeUsers")
    public void activeActiveUsers(Map<String, Object> model) {
        Set<UserDetails> users = (Set<UserDetails>) servletContext.getAttribute(UserCounterListener.USERS_KEY);
        if (users != null) {
            model.put("users", new ArrayList<UserDetails>(users));
        } else {
            model.put("users", new ArrayList<UserDetails>());
        }
    }

    @SuppressWarnings("unchecked")
    public void evictSecondLevelCache(SessionFactory factory) {
        Map<String, CollectionMetadata> roleMap = factory.getAllCollectionMetadata();
        for (String roleName : roleMap.keySet()) {
            factory.evictCollection(roleName);
        }

        Map<String, ClassMetadata> entityMap = factory.getAllClassMetadata();
        for (String entityName : entityMap.keySet()) {
            factory.evictEntity(entityName);
        }

        factory.evictQueries();
    }

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
}
