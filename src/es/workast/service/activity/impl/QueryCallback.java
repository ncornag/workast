package es.workast.service.activity.impl;

import java.util.List;

import es.workast.model.activity.Activity;
import es.workast.model.person.Person;
import es.workast.web.stream.StreamFilter;

public interface QueryCallback {

    List<Activity> doQuery(Person person, StreamFilter streamFilter, Person tagsPerson, Long groupId);

}