import osmnx as ox
import pymongo
import math
import requests
import certifi
import ssl
import geopy
import logging
import time
import argparse


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

	def update_one(self, query, new_val, upsert=False):
		self.collection.update_one(query, new_val, upsert)

	def update_many(self, query, new_val):
		self.collection.update_many(query, new_val)

	def find_one_and_update(self, query, new_val):
		self.collection.find_one_and_update(query, new_val)

	def create_index(self, key, index_type):
		self.collection.create_index((key, index_type))


class GraphConverter(object):
	def __init__(self, mongoDB):
		self.mongoDB = mongoDB
		self.elevation_url = 'http://localhost:8081/api/v1/lookup'

		ctx = ssl.create_default_context(cafile=certifi.where())
		geopy.geocoders.options.default_ssl_context = ctx
		self.geo_locator = geopy.Nominatim(user_agent='address', timeout=None)

	def get_elevation(self, lon, lat):
		data = requests.get(self.elevation_url, {'locations': f'{lat},{lon}'}).json()
		return data['results'][0]['elevation'] if data else None

	def get_address(self, lon, lat):
		return self.geo_locator.reverse(f"{lat}, {lon}").address

	def calcDistance(self, lon1, lat1, lon2, lat2):
		R = 6372800  # Earth radius in meters

		phi1, phi2 = math.radians(lat1), math.radians(lat2)
		dphi = math.radians(lat2 - lat1)
		dlambda = math.radians(lon2 - lon1)

		a = math.sin(dphi / 2) ** 2 + \
			math.cos(phi1) * math.cos(phi2) * math.sin(dlambda / 2) ** 2

		return 2 * R * math.atan2(math.sqrt(a), math.sqrt(1 - a))

	def add_one_document(self, node):
		self.mongoDB.update_one({'id': node['osmid']},
								{'$setOnInsert':
									 {'id': node['osmid'],
									  'ele': self.get_elevation(node['x'], node['y']),
									  'coordinate': [node['x'], node['y']],
									  'outGoingEdges': [],
									  'address': self.get_address(node['x'], node['y'])}}
								, True)

	def add_neighbors(self, node1, node2, dist):
		# node1 -> node2
		neighbors = self.get_neighbor_set(node1['osmid'])
		if node2['osmid'] not in neighbors:
			self.mongoDB.update_one({'id': node1['osmid']}, {'$push': {'outGoingEdges': {'from': node1['osmid'], 'to': node2['osmid'], 'cost': dist}}})

		# node2 -> node1
		neighbors = self.get_neighbor_set(node2['osmid'])
		if node1['osmid'] not in neighbors:
			self.mongoDB.update_one({'id': node2['osmid']}, {'$push': {'outGoingEdges': {'from': node2['osmid'], 'to': node1['osmid'], 'cost': dist}}})

	def get_neighbor_set(self, osm_id):
		return set([nei_obj['to'] for nei_obj in self.mongoDB.find_one({'id': osm_id}, {'outGoingEdges': 1})['outGoingEdges']])


	def convert_graph_from_osmnx(self, bbox, log, start_from = -1):
		if log:
			logging.basicConfig(filename='create_graph_log.txt', level=logging.INFO)
			logging.info('start...')

		G = ox.graph_from_bbox(bbox[0], bbox[1], bbox[2], bbox[3])
		all_nodes = G.nodes()
		count = len(all_nodes)
		start = time.time()
		for i, osm_id in enumerate(G.nodes()):
			if i < start_from:
				continue

			node = G.nodes[osm_id]
			self.add_one_document(node)

			for nei_osm_id in G.neighbors(osm_id):
				nei_node = G.nodes[nei_osm_id]
				self.add_one_document(nei_node)
				dist = self.calcDistance(node['x'], node['y'], nei_node['x'], nei_node['y'])
				self.add_neighbors(node, nei_node, dist)

			if log:
				logging.info(f"[{i}/{count}]... {time.time() - start} sec")

		if log:
			logging.info("create indexes")

		self.mongoDB.create_index('id', pymongo.ASCENDING)
		self.mongoDB.create_index('coordinates', pymongo.GEOSPHERE)

		if log:
			logging.info(f"[{count}/{count}]... complete {time.time() - start} sec")


def main(args):
	mongodbDB = MongodbDatabase(args.host, args.port, args.database, args.collection)
	converter = GraphConverter(mongodbDB)
	converter.convert_graph_from_osmnx(args.bbox, args.logging)




if __name__ == '__main__':
	parser = argparse.ArgumentParser(formatter_class=argparse.ArgumentDefaultsHelpFormatter)
	parser.add_argument('--host', default='localhost', type=str, help='mongodb host')
	parser.add_argument('--port', default=27017, type=int, help='mongodb port')
	parser.add_argument('--database', default='routing', type=str, help='mongodb database')
	parser.add_argument('--collection', default='graph', type=str, help='mongodb collection')
	parser.add_argument('--bbox', nargs=4, type=float, required=True, help='bounding box: north, south, east, west')
	parser.add_argument('--logging', default=True, nargs='?', type=(lambda s: False if s.lower() == 'false' else True), help='active logging')
	args = parser.parse_args()

	main(args)




	# bbox = [37.781233, 37.760753, -122.428663, -122.454557]


