package es.workast.core.json.jettison;

import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.Validator;
import javax.xml.namespace.QName;

import org.codehaus.jettison.mapped.Configuration;
import org.codehaus.jettison.mapped.MappedNamespaceConvention;

@SuppressWarnings("deprecation")
public class JettisonMappedContext extends JAXBContext {

    private JAXBContext context;
    private MappedNamespaceConvention convention;

    public JettisonMappedContext(Class... classes) {
        Configuration config = new Configuration(new HashMap<String, String>(), new ArrayList<QName>(), new ArrayList<QName>());
        convention = new MappedNamespaceConvention(config);

        try {
            context = JAXBContext.newInstance(classes);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Unmarshaller createUnmarshaller() throws JAXBException {
        return new JettisonMappedUnmarshaller(context, convention);
    }

    @Override
    public Marshaller createMarshaller() throws JAXBException {
        return new JettisonMappedMarshaller(context, convention);
    }

    @Override
    public Validator createValidator() throws JAXBException {
        return context.createValidator();
    }

}