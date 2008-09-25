package es.workast.service.fileupload;

import java.io.File;

import org.springframework.web.multipart.MultipartFile;

import es.workast.service.fileupload.impl.FileManagerException;

/**
 * TODO Documentar
 * 
 * @author Nicolás Cornaglia
 */
public interface FileManager {

    String FILE_SAVE_ERROR = "Couldn't save file.";

    /**
     * Manage the persistent {@link File}
     * 
     * @param file
     * @param name
     * @param contentType
     * @return
     * @throws FileManagerException
     */
    void manageAttachment(FileDetails fileDetails) throws FileManagerException;

    /**
     * Manage the {@link File}, deleting it from disk if evicted
     * 
     * @param file
     * @param contentType 
     * @param name 
     * @return
     */
    String managePendingFile(File file, String name, String contentType) throws FileManagerException;

    /**
     * @param key
     * @return
     */
    File getAttachment(String key);

    /**
     * @param file
     * @return
     * @throws FileManagerException
     */
    String managePendingFile(MultipartFile file) throws FileManagerException;

    /**
     * Obtains the {@link File} without removing it
     * 
     * @param key
     * @return
     */
    File getPendingFile(String key);

    /**
     * Obtains the {@link File} details without removing it
     * 
     * @param key
     * @return
     */
    FileDetails getPendingFileDetails(String key);

    /**
     * Unmanages the pending {@link File} and removes from disk
     * 
     * @param key
     * @return
     */
    boolean removePendingFile(String key);

    /**
     * Obtains the pending files path
     * 
     * @return
     */
    String getPendingFilesPath();

    /**
     * Obtains the attachments path
     * 
     * @return
     */
    String getAttachmentsPath();

}