package com.elena.repository;

import com.elena.model.LonLat;
import com.elena.model.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.NearQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.*;


@Repository("mongodbDAL")
public class MongodbDAL implements GeoDataDAL {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public MongodbDAL(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Node getNodeByLonLat(LonLat lonLat) {
        Query query = new Query();
        query.addCriteria(Criteria.where("coordinates").is(new Double[]{lonLat.getLon(), lonLat.getLat()}));
        return this.mongoTemplate.findOne(query, Node.class);
    }

    @Override
    public Node getNodeById(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(Long.valueOf(id)));
        return this.mongoTemplate.findOne(query, Node.class);
    }

    @Override
    public Node getNodeByAddress(String address) {
        Query query = new Query();
        query.addCriteria(Criteria.where("address").is(address));
        return this.mongoTemplate.findOne(query, Node.class);
    }

    @Override
    public Node getNearestNode(LonLat lonLat) {
        NearQuery nearQuery = NearQuery.near(lonLat.getLat(), lonLat.getLat());
        nearQuery.query(new Query()).spherical(true).limit(1);
        return this.mongoTemplate.geoNear(nearQuery, Node.class).getContent().get(0).getContent();
    }

    @Override
    public List<String> getAllAddress() {
        return new ArrayList<String>(){{
           mongoTemplate.findAll(Node.class, "graph").forEach(
                   (node) -> {
                       add(node.getAddress());
                   }
           );
        }};
    }

}
