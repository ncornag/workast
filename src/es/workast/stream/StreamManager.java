package es.workast.stream;

import java.util.List;

import es.workast.activity.Activity;

/**
 * TODO Documentar
 * 
 * @author Nicolás Cornaglia
 */
public interface StreamManager {

    public abstract List<Activity> getStream(Long personId) throws Exception;

}