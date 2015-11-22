# cachemanager

Cache Manager is a proof of concept (PoC) to test the functionality of Hazelcast cluster cache as a SOA application.

It reads data from DataBase (H2 embebbed) and load it to a cache (HazelCast).

Cachemanager uses SpringBoot to run as service. Don't need application server.

Includes 3 modules:

1. Cluster cache admin: to create and destroy cache nodes.
2. Cache loader: to load data from DB to Cache.
3. Cache viewer: to search data in cache.

### System requeriments
- Java 1.7+
- Apache Maven 3.x+


## How to run

1. download source or git clone https://github.com/enriksen/cachemanager.git
2. mvn install
3. java -jar target\cachemanager-0.1.0.jar
4. Ready!
 
## How it works
* First app initilize embebbed Tomcat and H2 database. 
* Load DDL (schema.sql) to H2 and load data in DB (data.sql, initially 150k reg).
* Finish launching the app on port 8181
* Then the app is ready to create cache nodes and load data.
* It uses Hibernate-JPA for DB access.
* HazelCast client to read/write in cache cluster.
* Thymeleaf + Datatables for View Layer.
* CacheManager divides the load work on N threads (for deafult 3) to be prepared to operate with large amounts of data.
* Include API REST and Return JSON response for interoperability with other services.


## Main EndPoints

All operations can be invoked by GET request:

[Cache cluster Admin]
* **http://localhost:8181/cachemanager/cache/add** : Creates cluster instance
* **http://localhost:8181/cachemanager/cache/add?n=X** : Creates X cluster instances (no limits, warning with jvm Memory!)
* **http://localhost:8181/cachemanager/cache/remove** : Delete cluster instance
* **http://localhost:8181/cachemanager/cache/shutdown** : Delete all cluster instances
* **http://localhost:8181/cachemanager/cache/status** : Show active cluster instances

[Loader]
* **http://localhost:8181/cachemanager/loader/run** : Load all data from DB to cache (By default uses 3 threads)
* **http://localhost:8181/cachemanager/loader/run?t=X** : Use X threads to load all data from DB to cache 

[Search]
* **http://localhost:8181/cachemanager/search** : Show HTML Search Form
* **http://localhost:8181/cachemanager/search/users**: Search in cache and return JSON data
* **http://localhost:8181/cachemanager/search/users?name=XXX&phone=XXX&company=XXX&iban=XXX**: Search with filters


## Other Endpoints

[Monitoring]
* **http://localhost:8181/cachemanager/beans** : Displays a complete list of all the Spring beans in the application.
* **http://localhost:8181/cachemanager/dump** : Performs a thread dump.
* **http://localhost:8181/cachemanager/health** : Shows application health information
* **http://localhost:8181/cachemanager/metrics** : Shows 'metrics' information for the current application.
* **http://localhost:8181/cachemanager/mappings** : Displays a collated list of all @RequestMapping paths.

[H2 console]
* **http://localhost:8181/cachemanager/h2-console** : Displays H2 console to admin DataBase


## Quickstart

1. Start CacheManager
```
java -jar cachemanager-0.1.0.jar
```

2. Initialize 3 cache nodes
```
http://localhost:8181/cachemanager/cache/add?n=3
```

3. Run loader
```
http://localhost:8181/cachemanager/loader/run
```

4. Open Html Search Form
```
http://localhost:8181/cachemanager/search
```

5. If prefer JSON response for WS integration
```
http://localhost:8181/cachemanager/search/users?company=Google
```
