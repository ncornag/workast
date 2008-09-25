package es.workast.activity.type;

import org.springframework.stereotype.Component;

/**
 * TODO Documentar
 * 
 * @author Nicolás Cornaglia
 */
@Component
public class JoinGroupActivityType extends BaseActivityType {

    private static final long serialVersionUID = 1L;

    public static String TYPE = "JOINGROUP";

    /**
     * @see es.workast.activity.ActivityType#getType()
     */
    @Override
    public String getType() {
        return TYPE;
    }

}
