package es.workast.utils.mail;

import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.velocity.VelocityEngineUtils;

/**
 * Wrapper sobre JavaMailSenderImpl de Spring<br/>
 * <pre>
 * <b>Usage</b>
 *  
 * Plain Text:
 *      MimeMessageHelper helper = mailManager.createMimeMessageHelper();
 *      helper.setSubject("Workast mail service test");
 *      helper.setTo("ncornagl@workast.com");
 *      helper.setText("<html><body>Correo <b>Plano</b><body><html>");
 *      mailManager.send(helper);
 *  
 * Html:
 *      helper.setText("<html><body>Correo <b>Plano</b><body><html>", true);
 * 
 * Attachment:
 *      MimeMessageHelper helper = mailManager.createMimeMessageHelper(true);
 *      ...
 *      helper.addAttachment("mydoc.doc", new ClassPathResource("mydoc.doc"));
 * 
 * Attachment inline:
 *      MimeMessageHelper helper = mailManager.createMimeMessageHelper(true);
 *      ...
 *      helper.setText("<html><body>Mail <b>Inline</b>:<img src='cid:identifier1234'><body><html>", true);
 *      helper.addInline("identifier1234", new ClassPathResource("mydoc.doc"));
 *
 * With template (Velocity):
 *      Map model = new HashMap();
 *      model.put("userData", "userVariableData");
 *      helper.setText(mailManager.mergeTemplateIntoString("template.vm", model));
 *
 * </pre>
 *  
 * @author Nicolás Cornaglia
 * @see JavaMailSenderImpl
 */
public class MailManagerImpl extends JavaMailSenderImpl implements MailManager {

    // ---------- Services

    @Autowired
    private VelocityEngine velocityEngine;

    // ---------- Methods

    @SuppressWarnings("unchecked")
    public String mergeTemplateIntoString(String templateLocation, Map model) {
        return VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, templateLocation, model);
    }

    @SuppressWarnings("unchecked")
    public String mergeTemplateIntoString(String templateLocation, String encoding, Map model) {
        return VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, templateLocation, encoding, model);
    }

    public void send(MimeMessageHelper messageHelper) {
        send(messageHelper.getMimeMessage());
    }

    // MessageHelpers creation

    public MimeMessageHelper createMimeMessageHelper() throws MessagingException {
        return createMimeMessageHelper(false, null);
    }

    public MimeMessageHelper createMimeMessageHelper(boolean isMultipart) throws MessagingException {
        return createMimeMessageHelper(isMultipart, null);
    }

    public MimeMessageHelper createMimeMessageHelper(boolean isMultipart, String encoding) throws MessagingException {
        MimeMessage message = createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, isMultipart, encoding);
        return helper;
    }

    public MimeMessageHelper createMimeMessageHelper(String encoding) throws MessagingException {
        MimeMessage message = createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, encoding);
        return helper;
    }

    public MimeMessageHelper createMimeMessageHelper(int multipart) throws MessagingException {
        return createMimeMessageHelper(multipart, null);
    }

    public MimeMessageHelper createMimeMessageHelper(int multipart, String encoding) throws MessagingException {
        MimeMessage message = createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, multipart, encoding);
        return helper;
    }

    // ---------- Accessors

    /**
     * @return the velocityEngine
     */
    public VelocityEngine getVelocityEngine() {
        return velocityEngine;
    }

    /**
     * @param velocityEngine the velocityEngine to set
     */
    public void setVelocityEngine(VelocityEngine velocityEngine) {
        this.velocityEngine = velocityEngine;
    }

}
