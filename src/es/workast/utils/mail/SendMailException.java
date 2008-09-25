package es.workast.utils.mail;

import es.workast.utils.WorkastException;

/**
 * TODO Documentar
 * 
 * @author Nicolás Cornaglia
 */
public class SendMailException extends WorkastException {

    private static final long serialVersionUID = 1L;

    public SendMailException(Throwable cause) {
        super(cause);
    }
}
