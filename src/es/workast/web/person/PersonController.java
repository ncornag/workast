package es.workast.web.person;

import java.io.File;
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
import org.springframework.security.providers.encoding.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import es.workast.core.json.JsonResponse;
import es.workast.model.group.Group;
import es.workast.model.person.Person;
import es.workast.service.fileupload.FileManager;
import es.workast.service.group.GroupManager;
import es.workast.service.group.impl.GroupNotFoundException;
import es.workast.service.person.PersonManager;
import es.workast.service.person.PersonNetGraphManager;
import es.workast.service.person.impl.PersonNotFoundException;
import es.workast.service.tag.TagManager;
import es.workast.utils.FileUtils;
import es.workast.utils.mail.SendMailException;

/**
 *
 * @author Sergio R. Gianazza
 * @author Nicolás Cornaglia
 */
@Controller
public class PersonController {

    // ---------- Services

    private final Log logger = LogFactory.getLog(getClass());

    @Autowired
    private PersonManager personManager;
    @Autowired
    private GroupManager groupManager;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private FileManager fileManager;
    @Autowired
    private TagManager tagManager;
    @Autowired
    private PersonNetGraphManager personNetGraphManager;

    private Validator validator;

    // ---------- Methods

    @PostConstruct
    public void init() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    // ---------- Rest URLs

    /**
     * @param uid
     * @param model
     * @return
     */
    @RequestMapping("/password/recoverPassword/{uid}")
    public String newPasswordHandler(@PathVariable("uid") String uid, Map<String, Object> model) {
        model.put("uid", uid);
        return "recoverLostPassword";
    }

    /**
     * Gets the current User
     * 
     * @return
     */
    @RequestMapping("/data/person/current")
    public String getCurrentPerson(Model model) {
        if (logger.isDebugEnabled()) {
            logger.debug("Service called [GET]: person/current");
        }
        model.addAttribute("person", personManager.getCurrentPerson());
        return "json/person/person.json";
    }

    /**
     * Creates a new Person
     * 
     * @param dto
     * @return
     */
    @RequestMapping(value = "/data/person/new", method = RequestMethod.POST)
    public String createPerson(PersonFormDTO dto, BindingResult result, Model model) {
        if (logger.isDebugEnabled()) {
            logger.debug("Service called [POST]: person/new");
        }

        JsonResponse response = new JsonResponse();
        model.addAttribute("response", response);

        // Validate fields
        Set<ConstraintViolation<PersonFormDTO>> constraintViolations = validator.validate(dto);
        response.addFieldErrors(constraintViolations);

        if (!dto.getPassword().trim().equals(dto.getPasswordAgain().trim())) {
            response.addFieldError("passwordAgain", PersonManager.PASSWORD_MISMATCH);
        }
        if (personManager.getPersonByUsername(dto.getEmail()) != null) {
            response.addFieldError("email", PersonManager.EMAIL_ALREADY_USED);
        }
        // Return if errors
        if (response.hasErrors()) {
            return "json/response.json";
        }

        // Create & bind
        Person person = new Person();
        try {
            BeanUtils.copyProperties(person, dto);
        } catch (Exception e) {
            response.addError(PersonManager.BIND_ERROR);
        }
        // Return if errors
        if (response.hasErrors()) {
            return "json/response.json";
        }

        personManager.createPerson(person);

        response.addData("id", person.getId().toString());

        return "json/response.json";
    }

    /**
     * Saves the current person form
     * 
     * @param dto
     * @param result
     * @param model
     * @return
     */
    @RequestMapping(value = "/data/person/save", method = RequestMethod.POST)
    public String saveCurrentPerson(PersonFormDTO dto, BindingResult result, Model model) {
        if (logger.isDebugEnabled()) {
            logger.debug("Service called [POST]: person/save");
        }

        Person person = personManager.getCurrentPerson();
        JsonResponse response = new JsonResponse();
        model.addAttribute("response", response);

        // Validate fields
        Set<ConstraintViolation<PersonFormDTO>> constraintViolations = validator.validate(dto);
        response.addFieldErrors(constraintViolations);

        if (StringUtils.hasText(dto.getPassword())) {
            Set<ConstraintViolation<PersonFormDTO>> constraintViolationsPassword = validator.validate(dto, PasswordGroup.class);
            response.addFieldErrors(constraintViolationsPassword);
            if (!passwordEncoder.encodePassword(dto.getCurrentPassword(), person.getId()).equals(person.getPassword())) {
                response.addFieldError("currentPassword", PersonManager.CURRENT_PASSWORD_MISMATCH);
            } else {
                if (!dto.getPassword().equals(dto.getPasswordAgain())) {
                    response.addFieldError("passwordAgain", PersonManager.PASSWORD_MISMATCH);
                } else {
                    dto.setPassword(passwordEncoder.encodePassword(dto.getPassword(), person.getId()));
                }
            }
        } else {
            dto.setPassword(person.getPassword());
        }
        // Return if errors
        if (response.hasErrors()) {
            return "json/response.json";
        }

        // Picture
        boolean newPic = !"".equals(dto.getPictureId());
        if (newPic) {
            File waitingPicFile = fileManager.getPendingFile(dto.getPictureId());
            File picFile = FileUtils.moveFile(waitingPicFile, personManager.getProfilePicturesPath(), "profile_" + person.getId() + ".jpg");
            if (picFile == null) {
                response.addError(FileManager.FILE_SAVE_ERROR);
            }
            person.setHasPicture(true);
        }

        // Bind
        dto.setEmail(person.getEmail());
        try {
            BeanUtils.copyProperties(person, dto);
        } catch (Exception e) {
            response.addError(PersonManager.BIND_ERROR);
        }

        // Return if errors
        if (response.hasErrors()) {
            return "json/response.json";
        }

        // Save
        personManager.savePerson(person);
        if (newPic) {
            fileManager.removePendingFile(dto.getPictureId());
        }

        return "json/response.json";
    }

    /**
     * Gets the collection of followed by the Person identified by "id"
     * 
     * @param id
     * @return
     */
    @RequestMapping("/data/person/{id}/following")
    public String getFollowing(@PathVariable("id") Long id, Model model) {
        if (logger.isDebugEnabled()) {
            logger.debug("Service called [GET]: person/" + id + "/following");
        }
        model.addAttribute("persons", personManager.getPerson(id).getFollowing());
        return "json/person/persons.json";
    }

    /**
     * Gets the collection of followers of the Person identified by "id"
     * 
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("/data/person/{id}/followers")
    public String getFollowers(@PathVariable("id") Long id, Model model) {
        if (logger.isDebugEnabled()) {
            logger.debug("Service called [GET]: person/" + id + "/followers");
        }
        model.addAttribute("persons", personManager.getPerson(id).getFollowers());
        return "json/person/persons.json";
    }

    /**
     * Returns the Person activity statistic
     * 
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("/data/person/{id}/statistics")
    public String getStatistics(@PathVariable("id") Long id, Model model) {
        if (logger.isDebugEnabled()) {
            logger.debug("Service called [GET]: person/" + id + "/statistics");
        }
        model.addAttribute("personStatistic", personManager.getStatistics(personManager.getPerson(id)));
        return "json/person/statistics.json";
    }

    /**
     * Returns the connections net of a {@link Person}
     * 
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("/data/person/{id}/net")
    public String getPersonNet(@PathVariable("id") Long id, Model model) {
        if (logger.isDebugEnabled()) {
            logger.debug("Service called: [GET]: person/net/" + id);
        }
        model.addAttribute("net", personNetGraphManager.getPersonNet(personManager.getPerson(id)));
        return "json/person/net.json";
    }

    /**
     * Returns tags collection for the current {@link Person}
     * 
     * @param tagPrefix
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/data/person/tags/mytags")
    public String getCurrentPersonTags(@RequestParam("q") String tagPrefix, Model model) {
        if (logger.isDebugEnabled()) {
            logger.debug("Service called: [GET] person/tags/mytags [" + tagPrefix + "]");
        }
        model.addAttribute("tags", tagManager.getTagsByPerson(personManager.getCurrentPerson(), tagPrefix));
        return "json/person/tags.json";
    }

    /**
     * Returns a collection of group statistics for the current {@link Person}
     * 
     * @return
     * @throws Exception
     */
    @RequestMapping("/data/person/current/groups")
    public String getCurrentPersonGroups(Model model) {
        if (logger.isDebugEnabled()) {
            logger.debug("Service called:  [GET] person/current/groups");
        }
        model.addAttribute("groups", personManager.getCurrentPerson().getGroups());
        return "json/person/groups.json";
    }

    /**
     * Joins the current {@link Person} to a {@link Group}
     * 
     * @param id
     * @return
     */
    @RequestMapping(value = "/data/person/join/{id}", method = RequestMethod.POST)
    public String joinGroup(@PathVariable("id") Long id, Model model) {
        if (logger.isDebugEnabled()) {
            logger.debug("Service called:  [POST] join/" + id);
        }
        JsonResponse response = new JsonResponse();
        model.addAttribute("response", response);

        try {
            personManager.joinGroup(personManager.getCurrentPerson(), groupManager.getGroup(id));
        } catch (PersonNotFoundException e) {
            response.addError(PersonManager.PERSONNOTFOUND);
        } catch (GroupNotFoundException e) {
            response.addError(GroupManager.GROUPNOTFOUND);
        }

        return "json/response.json";
    }

    /**
     * Unjoins the current {@link Person} from a {@link Group}
     * 
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = "/data/person/leave/{id}", method = RequestMethod.POST)
    public String leaveGroup(@PathVariable("id") Long id, Model model) {
        if (logger.isDebugEnabled()) {
            logger.debug("Service called [POST]: person/leave/" + id);
        }
        JsonResponse response = new JsonResponse();
        model.addAttribute("response", response);

        try {
            personManager.leaveGroup(personManager.getCurrentPerson(), groupManager.getGroup(id));
        } catch (PersonNotFoundException e) {
            response.addError(PersonManager.PERSONNOTFOUND);
        } catch (GroupNotFoundException e) {
            response.addError(GroupManager.GROUPNOTFOUND);
        }

        return "json/response.json";
    }

    /**
     * @param id
     * @return
     */
    @RequestMapping(value = "/data/person/follow/{id}", method = RequestMethod.POST)
    public String follow(@PathVariable("id") Long id, Model model) {
        if (logger.isDebugEnabled()) {
            logger.debug("Service called: [POST]: person/follow/" + id);
        }
        JsonResponse response = new JsonResponse();
        model.addAttribute("response", response);

        try {
            personManager.follow(personManager.getCurrentPerson(), personManager.getPerson(id));
        } catch (PersonNotFoundException e) {
            response.addError(PersonManager.PERSONNOTFOUND);
        }
        return "json/response.json";
    }

    /**
     * @param id
     * @return
     */
    @RequestMapping(value = "/data/person/unfollow/{id}", method = RequestMethod.POST)
    public String unfollow(@PathVariable("id") Long id, Model model) {
        if (logger.isDebugEnabled()) {
            logger.debug("Service called: [POST]: person/unfollow/" + id);
        }
        JsonResponse response = new JsonResponse();
        model.addAttribute("response", response);

        try {
            personManager.unfollow(personManager.getCurrentPerson(), personManager.getPerson(id));
        } catch (PersonNotFoundException e) {
            response.addError(PersonManager.PERSONNOTFOUND);
        }
        return "json/response.json";
    }

    /**
     * @param username
     * @param model
     * @return
     */
    @RequestMapping(value = "/data/person/password/regeneratePassword", method = RequestMethod.POST)
    public String regeneratePassword(String username, Model model) {
        if (logger.isDebugEnabled()) {
            logger.debug("Service called [POST]: person/password/regeneratePassword");
        }
        JsonResponse response = new JsonResponse();
        model.addAttribute("response", response);

        Person person = personManager.getPersonByUsername(username);
        if (person == null) {
            response.addFieldError("username", "Invalid username"); //TODO: I18n esto!
            return "json/response.json";
        }
        try {
            personManager.regeneratePassword(person);
        } catch (SendMailException e) {
            response.addError(e.getLocalizedMessage()); //TODO: I18n esto!
            return "json/response.json";
        }

        return "json/response.json";
    }

    @RequestMapping(value = "/data/person/password/saveRecoveredPassword", method = RequestMethod.POST)
    public String saveRecoveredPassword(String uid, String password, String passwordAgain, Model model) {
        if (logger.isDebugEnabled()) {
            logger.debug("Service called [POST]: person/password/saveRecoveredPassword");
        }
        JsonResponse response = new JsonResponse();
        model.addAttribute("response", response);

        if (uid == null || "".equals(uid.trim())) {
            response.addError("Invalid access"); //TODO I18N
        }
        if (StringUtils.hasText(password)) {
            if (password.length() < 5) { //TODO properties!
                response.addError(PersonManager.PASSWORD_LENGHT);
            }
            if (!password.equals(passwordAgain)) {
                response.addError(PersonManager.PASSWORD_MISMATCH);
            }
        } else {
            response.addError(PersonManager.EMPTY_FIELD);
        }
        Person person = personManager.getPersonByPendingUID(uid);

        if (person == null) {
            response.addError("Invalid uid"); //TODO I18n
        }

        // Return if errors
        if (response.hasErrors()) {
            return "json/response.json";
        }

        personManager.saveRecoveredPassword(person, password);

        return "json/response.json";
    }

    /**
     * Gets the Person identified by "id"
     * 
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("/data/person/{id}/get")
    public String getPerson(@PathVariable("id") Long id, Model model) {
        if (logger.isDebugEnabled()) {
            logger.debug("Service called [GET]: person/" + id + "/get");
        }
        model.addAttribute("person", personManager.getPerson(id));
        return "json/person/person.json";
    }
}
