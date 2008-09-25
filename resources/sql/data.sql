-- USE ${db.schema};

INSERT INTO Person (id, name, password, email, hasPicture, created, sendCommentsMail, sendDiggestMail) VALUES (7, 'Admin', '2425fc5226ce8eb3692f0785cb0911a531277188', 'admin@workast.com', 0, '2008-09-12T12:20:00Z', 1, 1);

INSERT INTO Person (id, name, lastName1, lastName2, password, email, title, area, city, birthday, hasPicture, created, sendCommentsMail, sendDiggestMail) VALUES (1, 'Elena',   'Campos',    'Gómez',     '7a98dff73f69f5331fbf536a316e98a1e609f198', 'ecampos@workast.com',  'Marketing Assistant', 'Marketing', 'Valencia', '1968-07-17', 0, '2008-09-12T12:20:00Z', 1, 1);
INSERT INTO Person (id, name, lastName1, lastName2, password, email, title, area, city, birthday, hasPicture, created, sendCommentsMail, sendDiggestMail) VALUES (2, 'Nicolás', 'Cornaglia', 'Schlieman', 'efd8448bd8791945b0b1e3a46da81b9bd1b3985d', 'ncornag@workast.com',  'Senior Consultant',   'TS',        'Valencia', '1969-05-21', 0, '2008-09-12T12:20:00Z', 1, 1);
INSERT INTO Person (id, name, lastName1, lastName2, password, email, title, area, city, birthday, hasPicture, created, sendCommentsMail, sendDiggestMail) VALUES (3, 'Lucho',   'Giavedoni', '',          '7a7cc879ebdf9e8aa8a58b43520d30828fe5c5c0', 'lgiavedo@workast.com', 'Consultant',          'TS',        'Córdoba',  '1983-05-12', 0, '2008-09-12T12:20:00Z', 1, 1);

INSERT INTO Authority (personId, authority) VALUES (2, 'ROLE_ADMIN');

INSERT INTO Followers (personId, followerId) VALUES (1, 2);
INSERT INTO Followers (personId, followerId) VALUES (2, 3);

INSERT INTO Groups (id, name, created, private, listed, adminId) VALUES (1, 'My Company',       			'2008-09-10T00:00:00Z', false, true, 7);
INSERT INTO Groups (id, name, created, private, listed, adminId) VALUES (2, 'Friends of Chuck Norris',	'2008-09-10T00:00:00Z', false, true, 2);
INSERT INTO Groups (id, name, created, private, listed, adminId) VALUES (3, 'Valencia',         			'2008-09-10T00:00:00Z', false, true, 2);
INSERT INTO Groups (id, name, created, private, listed, adminId) VALUES (4, 'Project X25',      			'2008-09-10T00:00:00Z', false, true, 3);
INSERT INTO Groups (id, name, created, private, listed, adminId) VALUES (5, 'INALM',            			'2008-09-10T00:00:00Z', false, true, 1);
INSERT INTO Groups (id, name, created, private, listed, adminId) VALUES (6, 'Private Project',  			'2008-10-10T00:00:00Z', true, true, 2);
INSERT INTO Groups (id, name, created, private, listed, adminId) VALUES (7, 'Private & Secret Project', 	'2008-11-10T00:00:00Z', true, false, 2);

INSERT INTO GroupPerson (groupId, personId) VALUES (2, 2);
INSERT INTO GroupPerson (groupId, personId) VALUES (2, 3);
INSERT INTO GroupPerson (groupId, personId) VALUES (3, 1);
INSERT INTO GroupPerson (groupId, personId) VALUES (3, 2);
INSERT INTO GroupPerson (groupId, personId) VALUES (5, 2);

INSERT INTO Tag (id, name) VALUES(1, 'personal');
INSERT INTO Tag (id, name) VALUES(2, 'note');
INSERT INTO Tag (id, name) VALUES(3, 'dev');
INSERT INTO Tag (id, name) VALUES(4, 'grails');
INSERT INTO Tag (id, name) VALUES(5, 'tutorial');

INSERT INTO Activity (id, groupId, personId, personPrivate, personDeleted, activityType, postedTime, updatedTime, message, renderedMessage) VALUES (1, 1, 1, false, false, 'STATUS', '2009-01-01T10:00:00Z', '2009-01-01T10:00:00Z', 'reading a presentation', 'reading a presentation');
INSERT INTO Activity (id, groupId, personId, personPrivate, personDeleted, activityType, postedTime, updatedTime, message, renderedMessage) VALUES (2, 1, 2, false, false, 'STATUS', '2009-01-01T10:30:00Z', '2009-01-01T10:30:00Z', 'working on a proposal', 'working on a proposal');
UPDATE Person SET currentActivityId=2 WHERE id=2;

INSERT INTO Activity (id, groupId, personId, personPrivate, personDeleted, activityType, postedTime, updatedTime, message, renderedMessage) VALUES (3, 1, 1, false, false, 'STATUS', '2009-01-01T12:15:00Z', '2009-01-01T12:30:00Z', 'studying how to make a presentation more web 2.0', 'studying how to make a presentation more web 2.0');
INSERT INTO ActivityTag (id, activityId, personId, tagId) VALUES(1, 3, 1, 1);
INSERT INTO ActivityTag (id, activityId, personId, tagId) VALUES(2, 3, 1, 2);
INSERT INTO ActivityTag (id, activityId, personId, tagId) VALUES(3, 3, 2, 3);
INSERT INTO Activity (id, groupId, personId, personPrivate, personDeleted, activityType, postedTime, updatedTime, message, renderedMessage) VALUES (4, 1, 1, false, false, 'STATUS', '2009-01-01T12:20:00Z', '2009-01-01T12:20:00Z', '[http://www.google.com A web searcher] Well, its a pretty much more than a search site...', '<a class="externallink" href="http://www.google.com">A web searcher</a> Well, its a pretty much more than a search site...');
INSERT INTO Activity (id, groupId, personId, personPrivate, personDeleted, activityType, postedTime, updatedTime, message, renderedMessage) VALUES (5, 2, 2, false, false, 'STATUS', '2009-01-02T10:02:00Z', '2009-01-02T10:02:00Z', 'Chuck Norris can kill two stones with one bird.', 'Chuck Norris can kill two stones with one bird.');
INSERT INTO Activity (id, groupId, personId, personPrivate, personDeleted, activityType, postedTime, updatedTime, message, renderedMessage) VALUES (6, 1, 3, false, false, 'STATUS', '2009-01-02T11:00:00Z', '2009-01-02T11:00:00Z', 'about to start the persons DAO', 'about to start the persons DAO');
INSERT INTO Activity (id, groupId, personId, personPrivate, personDeleted, activityType, inReplyToId, parentId, postedTime, updatedTime, message, renderedMessage) VALUES (7, 1, 2, false, false, 'COMMENT', 3, 3, '2009-01-02T11:30:00Z', '2009-01-02T11:30:00Z', 'Try to put bigger fonts', 'Try to put bigger fonts');
INSERT INTO Activity (id, groupId, personId, personPrivate, personDeleted, activityType, inReplyToId, parentId, postedTime, updatedTime, message, renderedMessage) VALUES (8, 1, 3, false, false, 'COMMENT', 3, 3, '2009-01-02T12:30:00Z', '2009-01-02T12:30:00Z', 'And rounded corners...', 'And rounded corners...');

COMMIT;