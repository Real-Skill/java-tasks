# Java Collections, hashCode and equals

##Summary

Very simple task about collection that stores objects with own hashCode and equals method.

##Goal

Fill/override methods in classes that will make sure that:
* **Aquarium** collection stores fish, every fish is disquinshed by name, aquarium can not have two fish with same name
* **Aquarium** class suppose to extend correct collection that meets requiments mentioned above
* You should prevent possiblity of adding predator fish to non predator tank and otherwise by overriding method in **Aquarium** class
* **Fish** class suppose to have two methods overriden

**You are allowed to modify only files in src/main directory**

##To verify your solution

    mvn test
    
or 

    mvn clean test
    
or to run tests faster

    mvn test -P -realskill
