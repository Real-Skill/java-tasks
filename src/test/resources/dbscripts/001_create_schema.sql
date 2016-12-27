CREATE TABLE AQUARIUMS(aquarium_id INTEGER not NULL, location VARCHAR(255), volume INTEGER, PRIMARY KEY ( aquarium_id ));
CREATE TABLE FISH(fish_id INTEGER not NULL, name VARCHAR(255),
 age INTEGER,
 aquarium_id INTEGER,
 PRIMARY KEY (fish_id),
 FOREIGN KEY (aquarium_id) REFERENCES AQUARIUMS(aquarium_id));
