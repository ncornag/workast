package es.workast.stream;

import java.util.List;

import es.workast.activity.Activity;

/**
 * TODO Documentar
 * 
 * @author Nicol�s Cornaglia
 */
public interface StreamManager {

    public abstract List<Activity> getStream(Long personId) throws Exception;

}