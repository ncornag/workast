package es.workast.core.json.jackson;

import java.io.StringWriter;

import javax.annotation.PostConstruct;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import es.workast.core.json.JsonManager;
import es.workast.core.json.JsonUnMarshallException;

/**
 * TODO Documentar
 * 
 * @author Nicolás Cornaglia
 */
@Component
@Qualifier("inactive")
public class JacksonJsonManager implements JsonManager {

    ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally

    @PostConstruct
    public void init() {
    }

    /**
     * @see es.workast.core.json.JsonManager#marshall(java.lang.Object)
     */
    public String marshall(Object instance) {
        StringWriter writer = new StringWriter();
        try {
            mapper.writeValue(writer, instance);
        } catch (Exception e) {
            throw new JsonUnMarshallException(e);
        }
        return writer.toString();
    }

    /**
     * @see es.workast.core.json.JsonManager#unMarshall(java.lang.String, java.lang.Class)
     */
    public <T2> T2 unMarshall(String json, Class<T2> type) {
        T2 result = null;
        try {
            result = mapper.readValue(json, type);
        } catch (Exception e) {
            throw new JsonUnMarshallException(e);
        }
        return result;
    }
}
