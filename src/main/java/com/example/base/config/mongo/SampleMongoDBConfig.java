package com.example.base.config.mongo;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(
        basePackages = {"com.example.base.repository.mongo"},
        mongoTemplateRef = "sampleMongoTemplate"
)
@EnableConfigurationProperties
public class SampleMongoDBConfig {
    @Value("${spring.mongo.uri}")
    private String uri;

    @Value("${spring.mongo.dbname.sample}")
    private String databaseSample;

    @Bean(name = "sampleMongoFactory")
    @Primary
    public MongoDatabaseFactory sampleMongoFactory(
            @Qualifier("sampleMongoClient")MongoClient mongoClient) {
        return new SimpleMongoClientDatabaseFactory(mongoClient, databaseSample);
    }

    @Bean("sampleMongoClient")
    @Primary
    public MongoClient sampleMongoClient() {
        return MongoClients.create(uri);
    }

    @Bean("sampleMongoTemplate")
    @Primary
    public MongoTemplate sampleMongoTemplate(
            @Qualifier("sampleMongoFactory")MongoDatabaseFactory mongoDatabaseFactory) {
        return new MongoTemplate(mongoDatabaseFactory);
    }
}
