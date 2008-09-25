DROP TABLE PendingPassword IF EXISTS CASCADE;
DROP TABLE ActivityTag IF EXISTS CASCADE;
DROP TABLE Tag IF EXISTS CASCADE;
DROP TABLE Authority IF EXISTS CASCADE;
DROP TABLE Followers IF EXISTS CASCADE;
DROP TABLE GroupPerson IF EXISTS CASCADE;
ALTER TABLE Person DROP FOREIGN KEY fk_curr_act;
DROP TABLE Activity IF EXISTS CASCADE;
DROP TABLE Person IF EXISTS CASCADE;
DROP TABLE Groups IF EXISTS CASCADE;

DROP SEQUENCE ActivitySequence IF EXISTS CASCADE;
DROP SEQUENCE ActivityTagSequence IF EXISTS CASCADE;
DROP SEQUENCE GroupSequence IF EXISTS CASCADE;
DROP SEQUENCE PersonSequence IF EXISTS CASCADE;
DROP SEQUENCE TagSequence IF EXISTS CASCADE;
DROP SEQUENCE PendingPasswordSequence IF EXISTS CASCADE;