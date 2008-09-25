package es.workast.activity.type;

import org.springframework.stereotype.Component;

/**
 * TODO Documentar
 * 
 * @author Nicol�s Cornaglia
 */
@Component
public class NoteActivityType extends BaseActivityType {

    private static final long serialVersionUID = 1L;

    public static String TYPE = "NOTE";

    /**
     * @see es.workast.activity.ActivityType#getType()
     */
    @Override
    public String getType() {
        return TYPE;
    }

}