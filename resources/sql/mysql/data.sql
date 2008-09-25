USE workast;

INSERT INTO Person (id, name, lastName1, lastName2, username, password) VALUES (1, 'Elena', 'Campos', 'Gómez', 'elecampos', 'elecampos');
INSERT INTO Person (id, name, lastName1, lastName2, username, password) VALUES (2, 'Nicolás', 'Cornaglia', 'Schlieman', 'ncornag', 'ncornag');

INSERT INTO Authority (personId, authority) SELECT id, 'ROLE_ANONYMOUS' FROM Person;
INSERT INTO Authority (personId, authority) SELECT id, 'ROLE_USER' FROM Person;
INSERT INTO Authority (personId, authority) VALUES (2, 'ROLE_ADMIN');

INSERT INTO Groups (id, name) VALUES (1, 'Global');
INSERT INTO Groups (id, name) VALUES (2, 'Los amigos de JP');

INSERT INTO Followers (personId, followerId) VALUES (1, 2);

INSERT INTO Activity (id, groupId, personId, activityType, postedTime) VALUES (1, 1, 1, 'STATUS', '2008-09-11 10:00:00');
INSERT INTO ActivityParams (activityId, paramKey, paramValue) VALUES (1, 'status', 'está leyendo');

INSERT INTO Activity (id, groupId, personId, activityType, postedTime) VALUES (2, 1, 2, 'STATUS', '2008-09-11 10:30:00');
INSERT INTO ActivityParams (activityId, paramKey, paramValue) VALUES (2, 'status', 'está mirando la tele');

INSERT INTO Activity (id, groupId, personId, activityType, postedTime) VALUES (3, 1, 1, 'STATUS', '2008-09-12 12:15:00');
INSERT INTO ActivityParams (activityId, paramKey, paramValue) VALUES (3, 'status', 'se siente feliz');

INSERT INTO Activity (id, groupId, personId, activityType, postedTime) VALUES (4, 1, 1, 'LINK', '2008-09-12 12:20:00');
INSERT INTO ActivityParams (activityId, paramKey, paramValue) VALUES (4, 'linkTitle', 'Un buscador molón');
INSERT INTO ActivityParams (activityId, paramKey, paramValue) VALUES (4, 'linkUrl', 'http://www.google.com');
INSERT INTO ActivityParams (activityId, paramKey, paramValue) VALUES (4, 'notes', 'Bueno, es un poco mas que un buscador...');

INSERT INTO Activity (id, groupId, personId, activityType, postedTime) VALUES (5, 1, 2, 'JOINGROUP', '2008-09-13 05:30:00');
INSERT INTO ActivityParams (activityId, paramKey, paramValue) VALUES (5, 'groupName', 'Los amigos de JP');
INSERT INTO ActivityParams (activityId, paramKey, paramValue) VALUES (5, 'groupUrl', '/groups/1');

INSERT INTO Activity (id, groupId, personId, activityType, postedTime) VALUES (6, 1, 2, 'DELICIOUS', '2008-09-13 10:00:00');
INSERT INTO ActivityParams (activityId, paramKey, paramValue) VALUES (6, 'linkTitle', 'Mastering Grails: The Grails event model');
INSERT INTO ActivityParams (activityId, paramKey, paramValue) VALUES (6, 'linkUrl', 'http://www.ibm.com/developerworks/java/library/j-grails08128.html?ca=drs-');

INSERT INTO Activity (id, groupId, personId, activityType, postedTime) VALUES (7, 1, 2, 'READER', '2008-09-13 12:00:00');
INSERT INTO ActivityParams (activityId, paramKey, paramValue) VALUES (7, 'linkTitle', 'SpringSource Tool Suite Released');
INSERT INTO ActivityParams (activityId, paramKey, paramValue) VALUES (7, 'linkUrl', 'http://feeds.feedburner.com/~r/Interface21TeamBlog/~3/255168295/');

INSERT INTO Activity (id, groupId, personId, activityType, postedTime) VALUES (8, 2, 2, 'NOTE', '2008-09-13 10:00:00');
INSERT INTO ActivityParams (activityId, paramKey, paramValue) VALUES (8, 'notes', 'Este es un buen comentario<br/>que pasa de dos<br/>líneas');

-- select p.name, 
--         a.title
--   from person p,
--        activities a
--  where a.personId = p.id
--    and (p.id=1 or p.id in (select f.personId from followers f where f.followerId=1 ))
--  order by a.id;

COMMIT;