## Create graph to mongodb from osmn

### Elevation data
* Using [open-elevation](https://github.com/Jorl17/open-elevation).
* Steps:
	* Download [strm](http://srtm.csi.cgiar.org/srtmdata/) data
	* Create strm dataset 
	
	```
	docker run -t -i -v {path_to_strm_data}/srtm_data:/code/data openelevation/open-elevation /code/create-tiles.sh /code/data/srtm_12_05.tif 5 5
	```
	
	* Run the server if it is the first time

	```
	docker run -t -i -v {path_to_strm_data}/srtm_data:/code/data -p 8081:8080 openelevation/open-elevation
	```
	
	* Query example
	
	```
	curl http://localhost:8080/api/v1/lookup?locations=24.162522,120.649242
	```
 
### Prerequisites
* Name the collection name as **graph**

### Usage
``` 
-h, --help            show this help message and exit
  --host HOST           mongodb host (default: localhost)
  --port PORT           mongodb port (default: 27017)
  --database DATABASE   mongodb database (default: routing)
  --collection COLLECTION
                        mongodb collection (default: graph)
  --bbox BBOX BBOX BBOX BBOX
                        bounding box: north, south, east, west (default: None)
  --logging [LOGGING]   active logging (default: True)
```

### Example
```
python3 convert_graph_from_osmnx.py --bbox 37.781233 37.760753 -122.428663 -122.454557
```

## Create graph to mongodb from osm

### Elevation data
* Using [open-elevation](https://github.com/Jorl17/open-elevation).
* Steps:
	* Download [strm](http://srtm.csi.cgiar.org/srtmdata/) data
	* Create strm dataset 
	
	```
	docker run -t -i -v {path_to_strm_data}/srtm_data:/code/data openelevation/open-elevation /code/create-tiles.sh /code/data/srtm_12_05.tif 5 5
	```
	
	* Run the server if it is the first time

	```
	docker run -t -i -v {path_to_strm_data}/srtm_data:/code/data -p 8081:8080 openelevation/open-elevation
	```
	
	* Query example
	
	```
	curl http://localhost:8080/api/v1/lookup?locations=24.162522,120.649242
	```


### Prerequisites
* Download .osm file from [OpenStreetMap](https://wiki.openstreetmap.org/wiki/Planet.osm). Extract .osm to smaller pieces if needed (ex. using [osmsis](https://wiki.openstreetmap.org/wiki/Osmosis))
* Convert .som to Postgres database by using [osm2pgrouting](http://pgrouting.org/docs/tools/osm2pgrouting.html)
* Name the collection name as **graph**


### Usage
```
-h, --help            show this help message and exit
  --psql PSQL PSQL PSQL PSQL
                        postgres connection setting: (host port user database)
                        (default: ['localhost', 5432, 'postgres', 'routing'])
  --mongodb MONGODB MONGODB MONGODB MONGODB
                        mongodb connection setting: (host port database
                        collection) (default: ['localhost', 27017, 'routing',
                        'graph'])
  --logging [LOGGING]   active logging (default: True)
```

### Example
```
python3 convert_graph_from_psql.py -psql {connection setting} -mongodb {connection setting}
```
