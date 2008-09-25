package es.workast.model.activity.type.status;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import es.workast.model.activity.ActivityData;
import es.workast.service.fileupload.FileDetails;

/**
 * TODO Documentar
 * 
 * @author Nicolás Cornaglia
 */
@XmlRootElement(name = "data")
@XmlAccessorType(XmlAccessType.NONE)
public class StatusActivityData implements ActivityData, Serializable {

    // ---------- Properties

    private static final long serialVersionUID = 1L;

    @XmlElement
    private String type = StatusActivityType.TYPE;

    @XmlElement
    private List<Attachment> attachments = new ArrayList<Attachment>();

    // ---------- Constructors

    public StatusActivityData() {
    }

    public StatusActivityData(List<FileDetails> fileDetails) {
        for (FileDetails details : fileDetails) {
            attachments.add(new Attachment(details));
        }
    }

    @XmlRootElement(name = "attachment")
    @XmlAccessorType(XmlAccessType.NONE)
    public static class Attachment implements FileDetails, Serializable {

        private static final long serialVersionUID = -2675842289729592816L;

        @XmlElement
        private String id;
        @XmlElement
        private String name;
        @XmlElement
        private long size;
        @XmlElement
        private String contentType;

        public Attachment() {
        }

        public Attachment(FileDetails details) {
            this.id = details.getId();
            this.name = details.getName();
            this.contentType = details.getContentType();
            this.size = details.getFile().length();
        }

        /**
         * @see es.workast.service.fileupload.FileDetails#getContentType()
         */
        public String getContentType() {
            return contentType;
        }

        /**
         * @see es.workast.service.fileupload.FileDetails#getFile()
         */
        public File getFile() {
            return null;
        }

        /**
         * @see es.workast.service.fileupload.FileDetails#getId()
         */
        public String getId() {
            return id;
        }

        /**
         * @see es.workast.service.fileupload.FileDetails#getName()
         */
        public String getName() {
            return name;
        }

        /**
         * @see es.workast.service.fileupload.FileDetails#getSize()
         */
        public long getSize() {
            return size;
        }
    }

    // ---------- Accessors

    /**
     * Get the attachments as a Map
     * 
     * @return
     */
    public Map<String, Attachment> getAttachmentsAsMap() {
        Map<String, Attachment> result = new HashMap<String, Attachment>();
        for (Attachment attachment : attachments) {
            result.put(attachment.getId(), attachment);
        }
        return result;
    }

    /**
     * @see es.workast.model.activity.ActivityData#getType()
     */
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }

}
