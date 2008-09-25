package es.workast.service.fileupload;

import java.io.File;

/**
 * TODO Documentar
 * 
 * @author Nicolás Cornaglia
 */
public interface FileDetails {

    /**
     * @return
     */
    public String getId();

    /**
     * @return
     */
    public File getFile();

    /**
     * @return
     */
    public String getName();

    /**
     * @return
     */
    public String getContentType();

    /**
     * @return
     */
    public long getSize();

}
