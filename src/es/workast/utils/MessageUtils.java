package es.workast.utils;

import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.context.support.AbstractMessageSource;
import org.springframework.context.support.MessageSourceSupport;
import org.springframework.stereotype.Component;

/**
 * @author Nicolás Cornaglia
 */
@Component
public class MessageUtils {

    public static final java.util.Locale DEFAULT_LOCALE = new Locale("es", "ES");
    private static MessageSourceSupport messageSource;

    @Resource(name = "messageSource")
    private MessageSourceSupport source;

    /**
     * Hidden constructor
     */
    public MessageUtils() {
    }

    @PostConstruct
    public void initialize() {
        messageSource = source;
    }

    /**
     * @return the messageSource
     */
    public static AbstractMessageSource getMessageSource() {
        return (AbstractMessageSource) messageSource;
    }

    /**
     * @param source the source to set
     */
    public void setSource(MessageSourceSupport source) {
        this.source = source;
    }

}
