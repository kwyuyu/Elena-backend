## Prerequisites

* Before run the application, the Geo data should be created into database, [link](https://github.com/kwyuyu/Elena-backend/tree/master/create_graph).

* If you are using in-memory graph, you need to convert Geo data from Mongodb to **.json**, also, put this **.json** into resource folder and set the file path in **application.yml**.

	```
	mongoexport --collection=graph --db=routing --jsonFormat=relaxed --	out=graph.json --pretty --jsonArray
	```


## Configuration setting

Only need to choose one way to access geo data (in memory, mongodb).

```
elena:
  graph:
    inmemory:
      filePath: "graph.json"

    mongodb:
      database: routing
      port: 27017
      host: localhost
      username: user
      password: password
      maxPoolSize: 100
```

## Run the application

```
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```