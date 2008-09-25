package es.workast.service.fileupload.impl;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import es.workast.service.fileupload.FileDetails;
import es.workast.service.fileupload.FileManager;
import es.workast.utils.FileUtils;
import es.workast.utils.PropertyUtils;

/**
 * Central File Management of attachments, with TTL
 * TODO: Delete expired files
 * 
 * @author Nicolás Cornaglia
 */
@Service("fileManager")
public class FileManagerImpl implements FileManager {

    // ---------- Properties

    public static final String PENDINGFILESPATH = "fileManager.pendingFiles.path";
    public static final String PENDINGFILESTTL = "fileManager.pendingFiles.ttl";
    public static final String ATTACHMENTSPATH = "fileManager.attachments.path";

    private long ttl = 0;
    private String pendingFilesPath;
    private String attachmentsPath;
    private ConcurrentMap<String, PendingFile> files = new ConcurrentHashMap<String, PendingFile>();

    // ---------- Services

    private final Log logger = LogFactory.getLog(getClass());

    @Autowired
    private PropertyUtils propertyUtils; // Not directly nedded, but as a way of dependency (we need it in the init method)

    // ---------- Methods (Initialization)

    @PostConstruct
    public void init() {
        File pendingFilesDir = new File(getPendingFilesPath());
        FileSystemUtils.deleteRecursively(pendingFilesDir);
        pendingFilesDir.mkdirs();
    }

    // ---------- Methods (Attachments)

    /**
     * @see es.workast.service.fileupload.FileManager#manageAttachment(es.workast.service.fileupload.FileDetails)
     */
    public void manageAttachment(FileDetails fileDetails) throws FileManagerException {
        FileUtils.moveFile(fileDetails.getFile(), getAttachmentsPath(), fileDetails.getId());
        removePendingFile(fileDetails.getId());
    }

    public File getAttachment(String id) {
        return new File(getAttachmentsPath() + FileUtils.FILESEP + id);
    }

    // ---------- Methods (Pending Files)

    /**
     * @see es.workast.service.fileupload.FileManager#managePendingFile(java.io.File)
     */
    public String managePendingFile(File file, String name, String contentType) throws FileManagerException {
        File newFile = FileUtils.moveFile(file.getAbsolutePath(), getPendingFilesPath());
        if (newFile == null) {
            throw new FileManagerException(FileManagerException.DEFAULTERRORKEY, file.getAbsolutePath());
        }

        String uuid = UUID.randomUUID().toString();

        files.put(newFile.getName(), new PendingFile(uuid, newFile, name, contentType));
        return uuid;
    }

    public String managePendingFile(MultipartFile file) throws FileManagerException {
        String uuid = UUID.randomUUID().toString();
        File newFile;
        try {
            newFile = new File(getPendingFilesPath() + FileUtils.FILESEP + uuid);
            file.transferTo(newFile);
        } catch (IllegalStateException e) {
            throw new FileManagerException(FileManagerException.DEFAULTERRORKEY, file.getName());
        } catch (IOException e) {
            throw new FileManagerException(FileManagerException.DEFAULTERRORKEY, file.getName());
        }

        files.put(uuid, new PendingFile(uuid, newFile, file.getOriginalFilename(), file.getContentType()));
        return uuid;
    }

    /**
     * @see es.workast.service.fileupload.FileManager#getPendingFile(java.lang.String)
     */
    public File getPendingFile(String key) {
        return files.get(key).getFile();
    }

    /**
     * @see es.workast.service.fileupload.FileManager#getPendingFileDetails(java.lang.String)
     */
    public FileDetails getPendingFileDetails(String key) {
        return (FileDetails) files.get(key);
    }

    /**
     * @see es.workast.service.fileupload.FileManager#removePendingFile(java.lang.String)
     */
    public boolean removePendingFile(String key) {
        File file = files.remove(key).getFile();
        boolean response = true;
        if (file.exists()) {
            response = file.delete();
            if (!response) {
                if (logger.isWarnEnabled()) {
                    logger.warn("Couldn't remove file " + file.getAbsolutePath());
                }
            }
        }
        return response;
    }

    /**
     * Removes the evicted files, when current time < creation+TTL
     */
    public void evictFiles() {
        long currentTime = System.currentTimeMillis();
        for (String key : files.keySet()) {
            PendingFile pendingFile = files.get(key);
            if (pendingFile.getExpires() < currentTime) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Evicting [" + key + "]");
                }
                removePendingFile(key);
            }
        }
    }

    // ---------- Accessors

    public String getPendingFilesPath() {
        if (pendingFilesPath == null) {
            pendingFilesPath = PropertyUtils.getProperty(PENDINGFILESPATH);
            File pendingFilesDir = new File(pendingFilesPath);
            if (!pendingFilesDir.exists()) {
                pendingFilesDir.mkdirs();
            }
        }
        return pendingFilesPath;
    }

    public void setPendingFilesPath(String pendingFilesPath) {
        this.pendingFilesPath = pendingFilesPath;
    }

    public long getTtl() {
        if (ttl == 0) {
            ttl = Long.valueOf(PropertyUtils.getProperty(PENDINGFILESTTL)).longValue();
        }
        return ttl;
    }

    public void setTtl(long ttl) {
        this.ttl = ttl;
    }

    public String getAttachmentsPath() {
        if (attachmentsPath == null) {
            attachmentsPath = PropertyUtils.getProperty(ATTACHMENTSPATH);
            File attachmentsDir = new File(attachmentsPath);
            if (!attachmentsDir.exists()) {
                attachmentsDir.mkdirs();
            }
        }
        return attachmentsPath;
    }

    public void setAttachmentsPath(String attachmentsPath) {
        this.attachmentsPath = attachmentsPath;
    }

    /**
     * Internal class to manage pending files
     * 
     * @author Nicolás Cornaglia
     */
    class PendingFile implements FileDetails {

        private String id;
        private File file;
        private String name;

        private String contentType;
        private long expires = System.currentTimeMillis() + getTtl();

        public PendingFile(String id, File file, String name, String contentType) {
            this.id = id;
            this.file = file;
            this.name = name;
            this.contentType = contentType;
        }

        public long getExpires() {
            return expires;
        }

        /**
         * @see es.workast.service.fileupload.FileDetails#getId()
         */
        public String getId() {
            return id;
        }

        /**
         * @see es.workast.service.fileupload.FileDetails#getFile()
         */
        public File getFile() {
            return file;
        }

        /**
         * @see es.workast.service.fileupload.FileDetails#getName()
         */
        public String getName() {
            return name;
        }

        /**
         * @see es.workast.service.fileupload.FileDetails#getContentType()
         */
        public String getContentType() {
            return contentType;
        }

        /**
         * @see es.workast.service.fileupload.FileDetails#getSize()
         */
        public long getSize() {
            return file.length();
        }

    }

}
