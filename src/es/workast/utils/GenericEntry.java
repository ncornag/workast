package es.workast.utils;


/**
 * TODO Documentar
 * 
 * @author Nicol�s Cornaglia
 */
public class GenericEntry {

    public GenericEntry() {
    }

    public GenericEntry(String key, String value) {
        this.key = key;
        this.value = value;
    }

    private String key;

    private String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
