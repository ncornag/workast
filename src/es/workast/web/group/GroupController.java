package es.workast.web.group;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import es.workast.core.json.JsonResponse;
import es.workast.model.group.Group;
import es.workast.service.group.GroupManager;

@Controller
public class GroupController {

    // ---------- Services

    private final Log logger = LogFactory.getLog(getClass());

    @Autowired
    public GroupManager groupManager;

    private Validator validator;

    // ---------- Methods

    @PostConstruct
    public void init() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    // ---------- URLs

    @RequestMapping(value = "/group/{groupId}")
    public String showHandler(@PathVariable("groupId") Long groupId, @RequestParam(value = "type", required = false) String type, Model model) {
        Map<String, String> results = new HashMap<String, String>();
        if (type != null) {
            results.put("type", "{x:'" + type + "'}");
        }
        results.put("groupId", "{x:" + groupId.toString() + "}");
        model.addAttribute("model", results);

        return "group/show";
    }

    @RequestMapping(value = "/group/list")
    public String listHandler() {
        return "group/list";
    }

    @RequestMapping(value = "/group/new")
    public String newHandler() {
        return "group/new";
    }

    /**
     * @return
     */
    @RequestMapping("/data/group/list")
    public String getGroupsByName(@RequestParam("name") String name, Model model) {
        if (logger.isDebugEnabled()) {
            logger.debug("Service called [GET]: group/list");
        }
        model.addAttribute("groups", groupManager.getGroupsByName(name));
        return "json/group/groups.json";
    }

    /**
     * Gets a Group Form, validates it and creates the new Group
     * 
     * @return
     */
    @RequestMapping(value = "/data/group/new", method = RequestMethod.POST)
    public String createGroup(GroupFormDTO dto, BindingResult result, Model model) {
        if (logger.isDebugEnabled()) {
            logger.debug("Service called [POST]: group/new");
        }
        JsonResponse response = new JsonResponse();
        model.addAttribute("response", response);

        // Validate Fields
        Set<ConstraintViolation<GroupFormDTO>> constraintViolations = validator.validate(dto);
        response.addFieldErrors(constraintViolations);

        if (groupManager.getGroupsByName(dto.getName().trim().toUpperCase()).size() > 0) {
            response.addFieldError("name", "Name already in use."); //FIXME: I18n
        }
        if (response.hasErrors()) {
            return "json/response.json";
        }

        // Create & bind
        Group group = new Group();
        try {
            BeanUtils.copyProperties(group, dto);
        } catch (Exception e) {
            response.addError(GroupManager.BIND_ERROR);
        }

        // Return if errors
        if (response.hasErrors()) {
            return "json/response.json";
        }

        // Not implemented yet: listed & private
        group.setListed(Boolean.TRUE);
        group.setPriv(Boolean.FALSE);

        groupManager.createGroup(group);

        response.addData("id", group.getId().toString());

        return "json/response.json";
    }

    /**
     * Gets the Group identified by "id"
     * 
     * @param id
     * @return
     */
    @RequestMapping(value = "/data/group/{id}/get", method = RequestMethod.GET)
    public String getGroup(@PathVariable("id") Long id, Model model) {
        if (logger.isDebugEnabled()) {
            logger.debug("Service called [GET]: group/" + id + "/get");
        }
        model.addAttribute("group", groupManager.getGroup(id));
        return "json/group/group.json";
    }
}