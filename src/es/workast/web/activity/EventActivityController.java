package es.workast.web.activity;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import es.workast.core.json.JsonResponse;
import es.workast.model.activity.Activity;
import es.workast.model.activity.type.ActivityType;
import es.workast.model.activity.type.event.EventActivityFormDTO;
import es.workast.model.activity.type.event.EventActivityType;
import es.workast.model.person.Person;

/**
 * @author Nicolás Cornaglia
 */
@Controller
@RequestMapping("/data/eventactivity")
public class EventActivityController extends ActivityController {

    // ---------- Services

    private final Log logger = LogFactory.getLog(getClass());

    // ---------- Rest URLs

    @RequestMapping(value = "/event", method = RequestMethod.POST)
    public String addEventActivity(EventActivityFormDTO dto, Model model) {
        if (logger.isDebugEnabled()) {
            logger.debug("Service called: [POST] activity/event");
        }

        JsonResponse response = new JsonResponse();
        model.addAttribute("response", response);

        // FIXME: Validate with Hibernate Validation
        if (!StringUtils.hasText(dto.getTitle())) {
            response.addFieldError("title", "Este campo es obligatorio"); //FIXME: I18n
            return "json/response.json";
        }

        Person person = getPersonManager().getCurrentPerson();
        ActivityType eventActivityType = getActivityTypeManager().getActivityType(EventActivityType.TYPE);
        Activity activity = eventActivityType.getActivity(person, dto);

        getActivityManager().saveActivity(activity, false);

        return "json/response.json";

    }

}
