package es.workast.core.json.jettison;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;
import java.util.WeakHashMap;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import es.workast.core.json.JsonManager;
import es.workast.utils.WorkastException;

/**
 * TODO Documentar
 * 
 * @author Nicolás Cornaglia
 */
@Component
@Qualifier("active")
public class JettisonJsonManager implements JsonManager {

    static Map<Class, JAXBContext> jaxbContexts = new WeakHashMap<Class, JAXBContext>();

    @PostConstruct
    public void init() {
    }

    /**
     * @see es.workast.core.json.JsonManager#marshall(java.lang.Object)
     */
    public String marshall(Object instance) {
        JettisonMappedContext context = new JettisonMappedContext(instance.getClass());
        StringWriter writer = new StringWriter();
        try {
            Marshaller marshaller = context.createMarshaller();
            marshaller.marshal(instance, writer);
        } catch (JAXBException e) {
            throw new WorkastException(e);
        }
        return writer.toString();
    }

    /**
     * @see es.workast.core.json.JsonManager#unMarshall(java.lang.String, java.lang.Class)
     */
    @SuppressWarnings("unchecked")
    public <T2> T2 unMarshall(String json, Class<T2> type) {
        JettisonMappedContext context = new JettisonMappedContext(type);
        T2 result = null;
        try {
            Unmarshaller unmarshaller = context.createUnmarshaller();
            result = (T2) unmarshaller.unmarshal(new StringReader(json));
        } catch (JAXBException e) {
            throw new WorkastException(e);
        }
        return result;
    }

    /**
     * @param type
     * @return
     * @throws JAXBException
     */
    private JAXBContext getJAXBContext(Class type) throws JAXBException {
        synchronized (jaxbContexts) {
            JAXBContext context = jaxbContexts.get(type);
            if (context == null) {
                context = JAXBContext.newInstance(type);
                jaxbContexts.put(type, context);
            }
            return context;
        }
    }
}
