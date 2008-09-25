package es.workast.activity.type;

import org.springframework.stereotype.Component;

/**
 * TODO Documentar
 * 
 * @author Nicolás Cornaglia
 */
@Component
public class ReaderActivityType extends BaseActivityType {

    private static final long serialVersionUID = 1L;

    public static String TYPE = "READER";

    /**
     * @see es.workast.activity.ActivityType#getType()
     */
    @Override
    public String getType() {
        return TYPE;
    }

}
