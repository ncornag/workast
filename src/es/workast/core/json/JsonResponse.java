package es.workast.core.json;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;

import es.workast.utils.GenericEntry;

/**
 * TODO Documentar
 * 
 * @author Nicolás Cornaglia
 */
public class JsonResponse {

    public final static JsonResponse OK = new JsonResponse(JsonStatus.OK);

    // ---------- Properties

    private String contentType = "application/json";

    private JsonStatus status;

    private Collection<GenericEntry> data;

    private Collection<GenericEntry> fieldErrors;

    private Collection<String> errors;

    // fieldErrors Holder
    private Map<String, GenericEntry> fieldErrorsMap;

    // ---------- Constructors

    public JsonResponse() {
        setStatus(JsonStatus.OK);
    }

    public JsonResponse(JsonStatus status) {
        setStatus(status);
    }

    public static JsonResponse ok() {
        return new JsonResponse(JsonStatus.OK);
    }

    public static JsonResponse error() {
        return new JsonResponse(JsonStatus.ERROR);
    }

    public static JsonResponse error(String value) {
        return new JsonResponse(JsonStatus.ERROR).addError(value);
    }

    // ---------- Methods

    public JsonResponse addData(String key, String value) {
        getData().add(new GenericEntry(key, value));
        return this;
    }

    public JsonResponse addError(String value) {
        setStatus(JsonStatus.ERROR);
        getErrors().add(value);
        return this;
    }

    public JsonResponse addFieldError(String key, String value) {
        setStatus(JsonStatus.ERROR);
        GenericEntry entry = getFieldErrorsMap().get(key);
        if (entry == null) {
            entry = new GenericEntry(key, value);
            getFieldErrorsMap().put(key, entry);
        } else {
            entry.setValue(entry.getValue() + ", " + value);
        }
        getFieldErrors().add(entry);
        return this;
    }

    public <T> JsonResponse addFieldErrors(Set<ConstraintViolation<T>> constraintViolations) {
        if (constraintViolations.size() > 0) {
            setStatus(JsonStatus.ERROR);
            for (ConstraintViolation<?> constraintViolation : constraintViolations) {
                addFieldError(constraintViolation.getPropertyPath().toString(), constraintViolation.getMessage());
            }
        }
        return this;
    }

    public boolean hasErrors() {
        return status == JsonStatus.ERROR;
    }

    // ---------- Accessors

    public JsonStatus getStatus() {
        return status;
    }

    public void setStatus(JsonStatus status) {
        this.status = status;
    }

    public Collection<GenericEntry> getData() {
        if (data == null) {
            data = new ArrayList<GenericEntry>();
        }
        return data;
    }

    public void setData(Collection<GenericEntry> data) {
        this.data = data;
    }

    public Collection<GenericEntry> getFieldErrors() {
        if (fieldErrors == null) {
            fieldErrors = new ArrayList<GenericEntry>();
        }
        return fieldErrors;
    }

    public Map<String, GenericEntry> getFieldErrorsMap() {
        if (fieldErrorsMap == null) {
            fieldErrorsMap = new HashMap<String, GenericEntry>();
        }
        return fieldErrorsMap;
    }

    public void setFieldErrors(Collection<GenericEntry> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }

    public Collection<String> getErrors() {
        if (errors == null) {
            errors = new ArrayList<String>();
        }
        return errors;
    }

    public void setErrors(Collection<String> errors) {
        this.errors = errors;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

}
