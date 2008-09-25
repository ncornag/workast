USE mysql;

DROP DATABASE IF EXISTS workast;
CREATE DATABASE workast CHARACTER SET utf8;

GRANT ALL ON workast.* TO 'workast'@'%' IDENTIFIED BY 'workast';

USE workast;

CREATE TABLE Person (
    id bigint NOT NULL PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(75) NOT NULL,
    lastName1 VARCHAR(75),
    lastName2 VARCHAR(75),
    username VARCHAR(50) NOT NULL,
    password VARCHAR(50) NOT NULL
);
CREATE UNIQUE INDEX ix_person_username ON person (username);

CREATE TABLE Authority (
    personId bigint NOT NULL,
    authority VARCHAR(50) NOT NULL,
    FOREIGN KEY (personId) REFERENCES Person (id)
);
CREATE UNIQUE INDEX ix_auth_username ON Authority (personId, authority);

CREATE TABLE Groups (
    id bigint NOT NULL PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(75)
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
    activityType varchar(10) NOT NULL,
    postedTime timestamp NOT NULL,
    FOREIGN KEY (personId) REFERENCES Person (id),
    FOREIGN KEY (groupId) REFERENCES Groups (id)
);

CREATE TABLE ActivityParams (
    activityId bigint NOT NULL,
    paramKey varchar(15) NOT NULL,
    paramValue varchar(4096) NOT NULL, 
    FOREIGN KEY (activityId) REFERENCES Activity (id)
);

COMMIT;