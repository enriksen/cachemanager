# cachemanager

Cache Manager reads data from DataBase (H2 embebbed) and load them to a cache (HazelCast).

Cachemanager uses SpringBoot to run as service. Don't need server application.

It uses HazelCast to implement a cache cluster.


## How to run

1. download
2. mvn install
3. java -jar cachemanager\target\cachemanager-0.1.0.jar
4. Ready!
 
## How it works
* First app initilize embebbed Tomcat and H2 database. 
* Load DDL (schema.sql) to H2 and load data (data.sql).
* Finish launching the app on port 8181
* Then the app is ready to create cache nodes and load data.


## Services

All operations can be invoked by GET request:

[Cache]
* *http://localhost:8181/cachemanager/cache/add* : Creates cluster instance
* *http://localhost:8181/cachemanager/cache/add?n=X* : Creates X cluster instances
* *http://localhost:8181/cachemanager/cache/remove* : Delete cluster instance
* *http://localhost:8181/cachemanager/cache/shutdown* : Delete all cluster instances
* *http://localhost:8181/cachemanager/cache/status* : Show active cluster instances

[Loader]
* *http://localhost:8181/cachemanager/loader/run* : Load all data from DB to cache
* *http://localhost:8181/cachemanager/loader/run?t=X* : Use X threads to load all data from DB to cache 
