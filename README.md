# Java JDBC

##Goal

###In this task you suppose to fill GenericDAO class so:
* *establishDbConnection* method of *GenericDAO* should register the JDBC driver and open a connection to database

    DB_URL: "jdbc:h2:mem:jdbc_task_db"

    DRIVER: "org.h2.Driver"

* *closeDbConnection* method of *GenericDAO* should close connection
* *getAllFishNames* method should execute a query on a database then extract data from result set
 so it returns *Array* of *Strings* of all *names* from *FISH* table
* *getFishInAquarium* should also return *Strings* of *names* but only *FISH* that are in any aquarium
* *findOldestFish* should return *name* of oldest fish in whole *FISH* table
* *getFishAgeByName* should return *age* of fish that name was passed by *name* parameter

###Data structure

####FISH Table

column | type
--- | :-:
fish_id | INTEGER
name | VARCHAR
age | INTEGER
aquarium_id | INTEGER

####AQUARIUMS Table

column | type
--- | :--:
aquarium_id | INTEGER
location | VARCHAR
volume | INTEGER

**You are allowed to modify only files in src/main/ directory**

##To verify your solution

    mvn verify
    
or 

    mvn clean verify
    
or to run tests faster

    mvn verify -P -realskill
