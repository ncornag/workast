package es.workast.model.activity.type.status;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import es.workast.core.json.JsonManager;
import es.workast.core.render.RenderEngine;
import es.workast.model.activity.Activity;
import es.workast.model.activity.ActivityData;
import es.workast.model.activity.type.ActivityType;
import es.workast.model.person.Person;
import es.workast.service.fileupload.FileDetails;
import es.workast.service.fileupload.FileManager;
import es.workast.service.group.GroupManager;
import es.workast.web.activity.StatusActivityFormDTO;

/**
 * TODO Documentar
 * 
 * @author Nicolás Cornaglia
 */
@Component
public class StatusActivityTypeImpl implements StatusActivityType, ActivityType {

    // ---------- Properties

    private static final long serialVersionUID = 1L;

    // ---------- Services

    @Autowired
    private FileManager fileManager;
    @Autowired
    private GroupManager groupManager;
    @Autowired
    private RenderEngine renderEngine;
    @Autowired
    @Qualifier("active")
    private JsonManager jsonManager;

    // ---------- Methods

    /**
     * @see es.workast.model.activity.type.ActivityType#getType()
     */
    public String getType() {
        return TYPE;
    }

    /**
     * @see es.workast.model.activity.type.ActivityType#getActivity(es.workast.model.person.Person, java.lang.Object)
     */
    public Activity getActivity(Person person, Object objectDTO) {

        StatusActivityFormDTO dto = (StatusActivityFormDTO) objectDTO;

        Activity activity = new Activity(person, groupManager.getGroup(dto.getGroupId()), StatusActivityType.TYPE);

        activity.setMessage(dto.getMessage());
        activity.setRenderedMessage(renderEngine.wikiRender(dto.getMessage()));

        // Attachs
        if (dto.getAttachmentIds() != null && dto.getAttachmentIds().size() > 0) {
            List<FileDetails> details = new ArrayList<FileDetails>();
            // Put file info (including pending file size) in ActivityData
            for (String id : dto.getAttachmentIds()) {
                details.add(fileManager.getPendingFileDetails(id));
            }
            activity.setActivityData(new StatusActivityData(details));
            // Move pending files to attachments path FIXME: Do it on Activity Save, not here...
            for (String id : dto.getAttachmentIds()) {
                fileManager.manageAttachment(fileManager.getPendingFileDetails(id));
            }
        }
        return activity;
    }

    /**
     * @see es.workast.model.activity.type.ActivityType#getData(java.lang.String)
     */
    public ActivityData getData(String data) {
        return jsonManager.unMarshall(data, StatusActivityData.class);
    };

    /**
     * @see es.workast.model.activity.type.ActivityType#getData(es.workast.model.activity.ActivityData)
     */
    public String getData(ActivityData data) {
        return jsonManager.marshall(data);
    }

}
