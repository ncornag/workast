package es.workast.core.json.jettison;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.attachment.AttachmentMarshaller;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Result;
import javax.xml.validation.Schema;

import org.codehaus.jettison.mapped.Configuration;
import org.codehaus.jettison.mapped.MappedNamespaceConvention;
import org.codehaus.jettison.mapped.MappedXMLStreamWriter;
import org.w3c.dom.Node;
import org.xml.sax.ContentHandler;

/**
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision$
 */
public class JettisonMappedMarshaller implements Marshaller {

    private Marshaller marshaller;
    protected String charset = "UTF-8";
    private MappedNamespaceConvention convention;

    public JettisonMappedMarshaller(JAXBContext context, Map<String, String> xmlToJSON, List<String> attributeMapping, List<String> ignoredElements)
            throws JAXBException {
        marshaller = context.createMarshaller();
        Configuration config = new Configuration(xmlToJSON, attributeMapping, ignoredElements);
        convention = new MappedNamespaceConvention(config);
    }

    public JettisonMappedMarshaller(JAXBContext context, MappedNamespaceConvention convention) throws JAXBException {
        marshaller = context.createMarshaller();
        this.convention = convention;
    }

    public void marshal(Object o, Result result) throws JAXBException {
        marshaller.marshal(o, result);
    }

    public void marshal(Object o, OutputStream outputStream) throws JAXBException {
        try {
            marshal(o, new OutputStreamWriter(outputStream, charset));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public void marshal(Object o, File file) throws JAXBException {
        try {
            marshal(o, new FileOutputStream(file));
        } catch (FileNotFoundException e) {
            throw new JAXBException(e);
        }
    }

    public void marshal(Object o, Writer writer) throws JAXBException {
        MappedXMLStreamWriter mapped = new MappedXMLStreamWriter(convention, writer);
        marshaller.marshal(o, mapped);
    }

    public void marshal(Object o, ContentHandler contentHandler) throws JAXBException {
        marshaller.marshal(o, contentHandler);
    }

    public void marshal(Object o, Node node) throws JAXBException {
        marshaller.marshal(o, node);
    }

    public void marshal(Object o, XMLStreamWriter xmlStreamWriter) throws JAXBException {
        marshaller.marshal(o, xmlStreamWriter);
    }

    public void marshal(Object o, XMLEventWriter xmlEventWriter) throws JAXBException {
        marshaller.marshal(o, xmlEventWriter);
    }

    public Node getNode(Object o) throws JAXBException {
        return marshaller.getNode(o);
    }

    public void setProperty(String s, Object o) throws PropertyException {
        marshaller.setProperty(s, o);
        if (s.equals(Marshaller.JAXB_ENCODING)) {
            charset = o.toString();
        }
    }

    public Object getProperty(String s) throws PropertyException {
        return marshaller.getProperty(s);
    }

    public void setEventHandler(ValidationEventHandler validationEventHandler) throws JAXBException {
        marshaller.setEventHandler(validationEventHandler);
    }

    public ValidationEventHandler getEventHandler() throws JAXBException {
        return marshaller.getEventHandler();
    }

    public void setAdapter(XmlAdapter xmlAdapter) {
        marshaller.setAdapter(xmlAdapter);
    }

    public <A extends XmlAdapter> void setAdapter(Class<A> aClass, A a) {
        marshaller.setAdapter(aClass, a);
    }

    public <A extends XmlAdapter> A getAdapter(Class<A> aClass) {
        return marshaller.getAdapter(aClass);
    }

    public void setAttachmentMarshaller(AttachmentMarshaller attachmentMarshaller) {
        marshaller.setAttachmentMarshaller(attachmentMarshaller);
    }

    public AttachmentMarshaller getAttachmentMarshaller() {
        return marshaller.getAttachmentMarshaller();
    }

    public void setSchema(Schema schema) {
        marshaller.setSchema(schema);
    }

    public Schema getSchema() {
        return marshaller.getSchema();
    }

    public void setListener(Listener listener) {
        marshaller.setListener(listener);
    }

    public Listener getListener() {
        return marshaller.getListener();
    }
}