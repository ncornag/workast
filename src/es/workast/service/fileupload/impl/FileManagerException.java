package es.workast.service.fileupload.impl;

import es.workast.utils.WorkastException;

/**
 * TODO Documentar
 * 
 * @author Nicolás Cornaglia
 */
public class FileManagerException extends WorkastException {

    public static final String DEFAULTERRORKEY = "fileManager.error";

    private static final long serialVersionUID = 1L;

    public FileManagerException(String messageKey, Object... messageArgs) {
        super(messageKey, messageArgs);
    }

}
