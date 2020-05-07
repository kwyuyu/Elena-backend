package com.elena.repository;

import com.elena.model.ElenaNode;
import com.elena.model.LonLat;
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
    public ElenaNode getNodeByLonLat(LonLat lonLat) {
        Query query = new Query();
        query.addCriteria(Criteria.where("location.coordinates").is(new Double[]{lonLat.getLon(), lonLat.getLat()}));
        return this.mongoTemplate.findOne(query, ElenaNode.class);
    }

    @Override
    public ElenaNode getNodeById(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(Long.valueOf(id)));
        return this.mongoTemplate.findOne(query, ElenaNode.class);
    }

    @Override
    public ElenaNode getNodeByAddress(String address) {
        Query query = new Query();
        query.addCriteria(Criteria.where("address").is(address));
        return this.mongoTemplate.findOne(query, ElenaNode.class);
    }

    @Override
    public List<ElenaNode> getNeighborNodes(ElenaNode node) {
        List<String> ids = new ArrayList<String>(){{
            node.getNeighbors().forEach(
                    (nei) -> add(nei.getNei())
            );
        }};

        Query query = new Query();
        query.addCriteria(Criteria.where("id").in(ids));

        return this.mongoTemplate.find(query, ElenaNode.class);
    }

    @Override
    public ElenaNode getNearestNode(LonLat lonLat) {
        System.out.println(lonLat);
        NearQuery nearQuery = NearQuery.near(lonLat.getLat(), lonLat.getLat());
        nearQuery.query(new Query()).spherical(true).limit(1);
        return this.mongoTemplate.geoNear(nearQuery, ElenaNode.class).getContent().get(0).getContent();
    }

    @Override
    public List<String> getAutoCompleteSuggestions(String userInput) {
        Query query = new Query();
        query.addCriteria(Criteria.where("address").regex("^" + userInput, "i"));
        List<ElenaNode> results = this.mongoTemplate.find(query, ElenaNode.class);
        return new ArrayList<String>(){{
            results.forEach(
                    (node) -> {
                        add(node.getAddress());
                    }
            );
        }};
    }

}
