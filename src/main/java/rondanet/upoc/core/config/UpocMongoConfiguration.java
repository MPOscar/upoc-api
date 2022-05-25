package rondanet.upoc.core.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import rondanet.upoc.core.utils.converters.DateTimeToDateCustomConverter;
import rondanet.upoc.core.utils.converters.DateToDateTimeCustomConverter;

import javax.ws.rs.DefaultValue;
import java.util.Arrays;

@Configuration
@EnableMongoRepositories(basePackages = "rondanet.upoc.core.repository",
        mongoTemplateRef = "mongoTemplateUpoc")
public class UpocMongoConfiguration {

    @DefaultValue("upoc")
    @Value("${mongodb.upoc.database}")
    private String upocDataBase;

    @DefaultValue("mongodb://localhost:27017")
    @Value("${mongodb.upoc.uri}")
    private String upocUri;

    public @Bean
    MongoClient mongoClientUpoc() {
        return MongoClients.create(upocUri);
    }

    @Bean(name = "mongoTemplateUpoc")
    public MongoTemplate mongoTemplateUpoc() {
        MongoDatabaseFactory dbFactory = new SimpleMongoClientDatabaseFactory(mongoClientUpoc(), upocDataBase);
        DefaultDbRefResolver dbRefResolver = new DefaultDbRefResolver(dbFactory);
        MongoMappingContext mappingContext = new MongoMappingContext();
        mappingContext.setAutoIndexCreation(true);
        mappingContext.afterPropertiesSet();
        MongoTemplate mongoTemplate = new MongoTemplate(dbFactory, new MappingMongoConverter(dbRefResolver, mappingContext));
        MappingMongoConverter converters = (MappingMongoConverter) mongoTemplate.getConverter();
        converters.setCustomConversions(customConversions());
        converters.afterPropertiesSet();
        return mongoTemplate;
    }

    public MongoCustomConversions customConversions() {
        return new MongoCustomConversions(
                Arrays.asList(new DateTimeToDateCustomConverter(), new DateToDateTimeCustomConverter())
        );
    }

}
