package com.elena.config;


import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClients;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDbFactory;

import java.util.Collections;

public abstract class AbstractMongodbConfig {

    private String host;
    private int port;
    private String database;
    private String username;
    private String password;
    private int maxPoolSize;

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public MongoDbFactory mongoDbFactory() {
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyToConnectionPoolSettings((settings) -> settings.maxWaitQueueSize(this.maxPoolSize))
                .applyToClusterSettings((settings) -> settings.hosts(Collections.singletonList(new ServerAddress(this.host, this.port))))
                .credential(MongoCredential.createCredential(this.username, this.database, this.password.toCharArray()))
                .build();

        return new SimpleMongoClientDbFactory(MongoClients.create(mongoClientSettings), this.database);
    }

    public abstract MongoTemplate getMongoTemplate() throws Exception;

}
