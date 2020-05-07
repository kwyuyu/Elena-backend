package com.elena.config;

import com.elena.model.ElenaGraph;
import com.elena.model.Graph;
import com.elena.model.Node;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;

@Configuration
@ConfigurationProperties("elena.graph")
public class InMemoryGraphConfig {

    private String filePath;

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }


    /**
     * create an in-memory graph from json file, filePath define in application.yml
     * @return ElenaGraph
     */
    @Bean(name = "graph")
    public ElenaGraph getGraph() {
        try {
            Gson gson = new Gson();

            Resource resource = new ClassPathResource(this.filePath);
            BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()));
            List<Node> nodes = gson.fromJson(reader, new TypeToken<List<Node>>(){}.getType());

            return new Graph(
                    new HashMap<String, Node>(){{
                        nodes.forEach(
                                (node) -> put(node.getId(), node)
                        );
                    }}
            );
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return new Graph();
    }
}
