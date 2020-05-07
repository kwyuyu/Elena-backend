package com.elena.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;


@Configuration
@ConfigurationProperties(prefix = "elena.graph.mongodb")
@EnableMongoRepositories(mongoTemplateRef = "graphMongoTemplate")
public class MongodbConfig extends AbstractMongodbConfig {


    @Override
    @Bean("graphMongoTemplate")
    public MongoTemplate getMongoTemplate() throws Exception {
        return new MongoTemplate(mongoDbFactory());
    }
}
