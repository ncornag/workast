// CREATE DATABASE workast DEFAULT CHARACTER SET utf8 DEFAULT COLLATE utf8_general_ci;

CREATE TABLE AttachmentFile (
    id bigint NOT NULL PRIMARY KEY AUTO_INCREMENT,
    activityId bigint NOT NULL,
    created VARCHAR(24) NOT NULL,
	name VARCHAR(75) NOT NULL,
	contentType VARCHAR(25) NOT NULL,
	path VARCHAR(16) NOT NULL
)
CREATE INDEX ix_att_act ON AttachmentFile (activityId);

CREATE TABLE Person (
    id bigint NOT NULL PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(75) NOT NULL,
    created VARCHAR(24) NOT NULL,
    lastName1 VARCHAR(75),
    lastName2 VARCHAR(75),
	email VARCHAR(75) NOT NULL,
    password VARCHAR(50) NOT NULL,
	currentActivityId bigint,
	title VARCHAR(75),
	area VARCHAR(75),
	city VARCHAR(75),
	birthday DATE,
	hasPicture boolean,
	sendCommentsMail boolean,
	sendDiggestMail boolean
);
CREATE UNIQUE INDEX ix_person_email ON Person (email);

CREATE TABLE Authority (
    personId bigint NOT NULL,
    authority VARCHAR(50) NOT NULL,
    FOREIGN KEY (personId) REFERENCES Person (id)
);
CREATE UNIQUE INDEX ix_auth_per_auth ON Authority (personId, authority);

CREATE TABLE Groups (
    id bigint NOT NULL PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(75) NOT NULL,
    created VARCHAR(24) NOT NULL,
	private boolean NOT NULL,
	listed boolean NOT NULL,
	adminId bigint NOT NULL
);

CREATE TABLE GroupPerson (
    groupId bigint NOT NULL,
    personId bigint NOT NULL,
    FOREIGN KEY (groupId) REFERENCES Groups (id),
    FOREIGN KEY (personId) REFERENCES Person (id)
);

CREATE TABLE Followers (
    personId bigint NOT NULL,
    followerId bigint NOT NULL,
    PRIMARY KEY(personId, followerId),
    FOREIGN KEY (personId) REFERENCES Person (id),
    FOREIGN KEY (followerId) REFERENCES Person (id)
);

CREATE TABLE Activity (
    id bigint NOT NULL PRIMARY KEY AUTO_INCREMENT,
    groupId bigint NOT NULL,
    personId bigint NOT NULL,
    personPrivate boolean NOT NULL,
    personDeleted boolean NOT NULL,
    activityType varchar(10) NOT NULL,
    postedTime varchar(24) NOT NULL,
    updatedTime varchar(24) NOT NULL,
	inReplyToId bigint,
	parentId bigint,
	message varchar(4096) NOT NULL,
	renderedMessage varchar(8172) NOT NULL,
	data varchar(1024),
    FOREIGN KEY (personId) REFERENCES Person (id),
    FOREIGN KEY (groupId) REFERENCES Groups (id),
    FOREIGN KEY (inReplyToId) REFERENCES Activity (id),
    FOREIGN KEY (parentId) REFERENCES Activity (id)
);
CREATE INDEX ix_activity_updatedTime ON Activity (updatedTime);

ALTER TABLE Person
	ADD CONSTRAINT fk_curr_act FOREIGN KEY (currentActivityId) REFERENCES Activity (id);

CREATE TABLE Tag (
    id bigint NOT NULL PRIMARY KEY AUTO_INCREMENT,
    name varchar(20) NOT NULL
);

CREATE TABLE ActivityTag (
    id bigint NOT NULL PRIMARY KEY AUTO_INCREMENT,
    activityId bigint NOT NULL,
    personId bigint NOT NULL,
    tagId bigint NOT NULL,
    FOREIGN KEY (activityId) REFERENCES Activity (id),
    FOREIGN KEY (personId) REFERENCES Person (id),
    FOREIGN KEY (tagId) REFERENCES Tag (id)
);
CREATE INDEX ix_tag_act_per ON ActivityTag (personId, activityId);

CREATE TABLE PendingPassword (
    id bigint NOT NULL PRIMARY KEY AUTO_INCREMENT,
    uid VARCHAR(100) NOT NULL,
    personID bigint NOT NULL,
    insertedDate DATE NOT NULL,
    FOREIGN KEY (personID) REFERENCES Person (id)
);

COMMIT;