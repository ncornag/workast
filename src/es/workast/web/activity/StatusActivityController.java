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
import es.workast.model.activity.type.status.StatusActivityType;
import es.workast.model.person.Person;

/**
 * @author Nicolás Cornaglia
 */
@Controller
@RequestMapping("/data/statusactivity")
public class StatusActivityController extends ActivityController {

    // ---------- Services

    private final Log logger = LogFactory.getLog(getClass());

    // ---------- Rest URLs

    @RequestMapping(value = "/clearstatus", method = RequestMethod.POST)
    public String clearStatus(Model model) {
        if (logger.isDebugEnabled()) {
            logger.debug("Service called: [POST] activity/clearstatus");
        }
        getActivityManager().clearStatus();

        model.addAttribute("response", JsonResponse.OK);
        return "json/response.json";
    }

    @RequestMapping(value = "/status", method = RequestMethod.POST)
    public String addStatusActivity(StatusActivityFormDTO dto, Model model) {
        if (logger.isDebugEnabled()) {
            logger.debug("Service called: [POST] activity/status");
        }

        JsonResponse response = new JsonResponse();
        model.addAttribute("response", response);

        // FIXME: Validate with Hibernate Validation
        if (!StringUtils.hasText(dto.getMessage())) {
            response.addFieldError("message", "Este campo es obligatorio"); //FIXME: I18n
            return "json/response.json";
        }

        Person person = getPersonManager().getCurrentPerson();
        ActivityType statusActivityType = getActivityTypeManager().getActivityType(StatusActivityType.TYPE);
        Activity activity = statusActivityType.getActivity(person, dto);

        getActivityManager().saveActivity(activity, (dto.getCurrent() != null && "yes".equals(dto.getCurrent())));

        return "json/response.json";
    }

}
