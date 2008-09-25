package es.workast.web.activity;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import es.workast.core.json.JsonResponse;
import es.workast.model.activity.Activity;
import es.workast.model.activity.type.ActivityType;
import es.workast.model.activity.type.comment.CommentActivityType;
import es.workast.model.person.Person;
import es.workast.model.tag.Tag;
import es.workast.service.activity.ActivityManager;
import es.workast.service.activity.ActivityTypeManager;
import es.workast.service.person.PersonManager;
import es.workast.service.tag.TagManager;

/**
 * @author Nicolás Cornaglia
 */
@Controller
@RequestMapping("/data/activity")
public class ActivityController {

    private static final String ACTIVITYNOTFOUND = "Activity not found.";
    private static final String INVALIDTAGNAME = "Invalid tag name.";
    private static final String TAGNOTFOUND = "Tag not found.";

    // ---------- Services

    private final Log logger = LogFactory.getLog(getClass());

    @Autowired
    private ActivityTypeManager activityTypeManager;
    @Autowired
    private ActivityManager activityManager;
    @Autowired
    private PersonManager personManager;
    @Autowired
    private TagManager tagManager;

    // ---------- Rest URLs

    @RequestMapping(value = "/{id}/comment", method = RequestMethod.POST)
    public String addActivityComment(@PathVariable("id") Long parentId, CommentActivityFormDTO dto, BindingResult result, Model model) {
        if (logger.isDebugEnabled()) {
            logger.debug("Service called: [POST] activity/comment");
        }

        JsonResponse response = new JsonResponse();
        model.addAttribute("response", response);

        dto.setActivityId(parentId);
        Activity activity = activityManager.getActivity(parentId);
        if (activity == null) {
            response.addError(ACTIVITYNOTFOUND); // i18n
            return "json/response.json";
        }

        Person person = personManager.getCurrentPerson();
        ActivityType commentActivityType = getActivityTypeManager().getActivityType(CommentActivityType.TYPE);
        Activity commentActivity = commentActivityType.getActivity(person, dto);

        activityManager.saveActivity(commentActivity, false);

        return "json/response.json";
    }

    @RequestMapping(value = "/{id}/tag", method = RequestMethod.POST)
    public String addActivityTag(@PathVariable("id") Long activityId, @RequestParam("name") String name, Model model) {
        if (logger.isDebugEnabled()) {
            logger.debug("Service called: [POST] activity/" + activityId + "/tag");
        }

        JsonResponse response = new JsonResponse();
        model.addAttribute("response", response);

        String tagName = name.trim();
        if (!StringUtils.hasText(tagName)) {
            response.addError(INVALIDTAGNAME); // i18n
            return "json/response.json";
        }

        Activity activity = activityManager.getActivity(activityId);
        if (activity == null) {
            response.addError(ACTIVITYNOTFOUND); // i18n
            return "json/response.json";
        }

        activityManager.addActivityTag(activity, tagName);

        return "json/response.json";
    }

    @RequestMapping(value = "/{id}/tag/{name}", method = RequestMethod.DELETE)
    public String removeActivityTag(@PathVariable("id") Long activityId, @PathVariable("name") String name, Model model) {
        if (logger.isDebugEnabled()) {
            logger.debug("Service called: [DELETE] activity/" + activityId + "/tag/" + name);
        }

        JsonResponse response = new JsonResponse();
        model.addAttribute("response", response);

        Tag tag = tagManager.getTagByName(name);
        if (tag == null) {
            response.addError(TAGNOTFOUND); // i18n
            return "json/response.json";
        }

        Activity activity = activityManager.getActivity(activityId);
        activityManager.removeActivityTag(activity, tag);

        return "json/response.json";
    }

    // ---------- Accessors

    public PersonManager getPersonManager() {
        return personManager;
    }

    public void setPersonManager(PersonManager personManager) {
        this.personManager = personManager;
    }

    public ActivityTypeManager getActivityTypeManager() {
        return activityTypeManager;
    }

    public void setActivityTypeManager(ActivityTypeManager activityTypeManager) {
        this.activityTypeManager = activityTypeManager;
    }

    public ActivityManager getActivityManager() {
        return activityManager;
    }

    public void setActivityManager(ActivityManager activityManager) {
        this.activityManager = activityManager;
    }

    public TagManager getTagManager() {
        return tagManager;
    }

    public void setTagManager(TagManager tagManager) {
        this.tagManager = tagManager;
    }

}
