import psycopg2
import argparse
import requests
import pymongo
import time
import logging
import geopy
import certifi
import ssl


class MongodbDatabase(object):
	def __init__(self):
		self.client = None
		self.db = None
		self.collection = None

	def __init__(self, host, port, database, collection):
		self.client = pymongo.MongoClient(f"mongodb://{host}:{port}/")
		self.db = self.client[database]
		self.collection = self.db[collection]

	def insert_one(self, obj):
		return self.collection.insert_one(obj).inserted_id

	def insert(self, objs):
		return self.collection.isnert_many(objs).inserted_ids

	def find_one(self, query = None, visibility = None):
		if query is None:
			query = {}
		if visibility is None:
			visibility = {}
		visibility['_id'] = 0
		return self.collection.find_one(query, visibility)

	def find(self, query=None, visibility=None):
		if query is None:
			query = {}
		if visibility is None:
			visibility = {}
		visibility['_id'] = 0
		return self.collection.find(query, visibility)

	def update_one(self, query, new_val):
		self.collection.update_one(query, new_val)

	def update_many(self, query, new_val):
		self.collection.update_many(query, new_val)

	def find_one_and_update(self, query, new_val):
		self.collection.find_one_and_update(query, new_val)


class PostgresDatabase(object):
	def __init__(self):
		self.conn = None
		self.curs = None

	def __init__(self, host, port, user, database):
		self.connect(host, port, user, database)

	def connect(self, host, port, user, database):
		self.conn = psycopg2.connect(host=host, port=port, user=user, database=database)
		self.curs = self.conn.cursor()

	def execute(self, query):
		self.curs.execute(query)
		if query.split(' ')[0] != 'select':
			return None
		return self.curs.fetchall()

	def execute_with_generator(self, query):
		self.curs.execute(query)
		count = self.curs.rowcount
		for i in range(count):
			yield (i+1, count, self.curs.fetchone())

	def commit(self):
		self.conn.commit()

	def close(self):
		self.curs.close()
		self.conn.close()

	def __del__(self):
		self.close()



class GraphConverter(object):
	def __init__(self, mongodbDB, nodesDB, waysDB):
		self.mongodbDB = mongodbDB
		self.nodesDB = nodesDB
		self.waysDB = waysDB
		self.elevation_url = 'http://localhost:8081/api/v1/lookup'

		ctx = ssl.create_default_context(cafile=certifi.where())
		geopy.geocoders.options.default_ssl_context = ctx
		self.geo_locator = geopy.Nominatim(user_agent='find_address', timeout=None)

	def get_elevation(self, lon, lat):
		data = requests.get(self.elevation_url, {'locations': f'{lat},{lon}'}).json()
		return data['results'][0]['elevation'] if data else None

	def get_node_name(self, osm_id):
		node_name = self.nodesDB.execute(f"select name from osm_nodes where osm_id = {osm_id};")
		return node_name[0][0]

	def get_address(self, lon, lat):
		return self.geo_locator.reverse(f"{lat}, {lon}").address

	def convert_graph(self, log, start_from = -1):
		if log:
			logging.basicConfig(filename='create_graph_log.txt', level=logging.INFO)
			logging.info('start...')
			logging.info('generate ways...')

		ways = self.waysDB.execute_with_generator("select osm_id, cost, reverse_cost, source_osm, x1, y1, target_osm, x2, y2 from ways")
		count = 0
		start = time.time()
		for i, count, (way_id, cost, reverse_cost, source_id, source_lon, source_lat, target_id, target_lon, target_lat) in ways:
			if i < start_from:
				continue

			source_node = self.mongodbDB.find_one({'id': source_id})
			if source_node is None:
				self.mongodbDB.insert_one({'id': source_id, 'name': self.get_node_name(source_id), 'ele': self.get_elevation(source_lon, source_lat), 'location': {'type': 'Point', 'coordinates': [source_lon, source_lat]}, 'neighbors': [], 'address': self.get_address(source_lon, source_lat)})
			self.mongodbDB.update_one({'id': source_id}, {'$push': {'neighbors': {'nei': target_id, 'cost': cost}}})

			target_node = self.mongodbDB.find_one({'id': target_id})
			if target_node is None:
				self.mongodbDB.insert_one({'id': target_id, 'name': self.get_node_name(target_id), 'ele': self.get_elevation(target_lon, target_lat), 'location': {'type': 'Point', 'coordinates': [target_lon, target_lat]}, 'neighbors': [], 'address': self.get_address(target_lon, target_lat)})
			self.mongodbDB.update_one({'id': target_id}, {'$push': {'neighbors': {'nei': source_id, 'cost': reverse_cost}}})

			if log:
				logging.info(f"[{i}/{count}]... {time.time() - start} sec")

		if log:
			logging.info(f"[{count}/{count}]... complete {time.time() - start} sec")


def main(args):
	mongodbDB = MongodbDatabase(args.psql[0], args.psql[1], args.psql[2], args.psql[3])
	nodesDB = PostgresDatabase(args.mongodb[0], args.mongodb[1], args.mongodb[2], args.mongodb[3])
	waysDB = PostgresDatabase(args.mongodb[0], args.mongodb[1], args.mongodb[2], args.mongodb[3])
	converter = GraphConverter(mongodbDB, nodesDB, waysDB)
	converter.convert_graph(args.logging)





if __name__ == '__main__':
	parser = argparse.ArgumentParser(formatter_class=argparse.ArgumentDefaultsHelpFormatter)
	parser.add_argument('--psql', default=['localhost', 5432, 'postgres', 'routing'], nargs=4, help='postgres connection setting: (host port user database)')
	parser.add_argument('--mongodb', default=['localhost', 27017, 'routing', 'graph'], nargs=4, help='mongodb connection setting: (host port database collection)')
	parser.add_argument('--logging', default=True, nargs='?', type=(lambda s: False if s.lower() == 'false' else True), help='active logging')
	args = parser.parse_args()

	args.psql[1] = int(args.psql[1])
	args.mongodb[1] = int(args.mongodb[1])

	main(args)








