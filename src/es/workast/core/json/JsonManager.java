package es.workast.core.json;


/**
 * TODO Documentar
 * 
 * @author Nicolás Cornaglia
 */
public interface JsonManager {

    /**
     * @param instance
     * @return
     */
    public abstract String marshall(Object instance);

    /**
     * @param <T2>
     * @param json
     * @param type
     * @return
     */
    public abstract <T2> T2 unMarshall(String json, Class<T2> type);

}