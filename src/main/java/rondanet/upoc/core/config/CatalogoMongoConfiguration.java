package rondanet.upoc.core.config;

import rondanet.upoc.core.utils.converters.ArrayStringToStringCustomConverter;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import javax.ws.rs.DefaultValue;
import java.util.Arrays;

@Configuration
@EnableMongoRepositories(basePackages = "rondanet.upoc.catalogo.repository",
        mongoTemplateRef = "mongoTemplateCatalogo")
public class CatalogoMongoConfiguration {

    @DefaultValue("catalogo")
    @Value("${mongodb.catalogo.database}")
    private String catalogoDataBase;

    @DefaultValue("mongodb://localhost:27017")
    @Value("${mongodb.catalogo.uri}")
    private String catalogoUri;

    public @Bean
    MongoClient mongoClientCatalogo() {
        return MongoClients.create(catalogoUri);
    }

    @Bean(name = "mongoTemplateCatalogo")
    public MongoTemplate mongoTemplateCatalogo() throws Exception {
        MongoTemplate mongoTemplate = new MongoTemplate(mongoClientCatalogo(), catalogoDataBase);;
        MappingMongoConverter conv = (MappingMongoConverter) mongoTemplate.getConverter();
        conv.setCustomConversions(customConversions());
        conv.afterPropertiesSet();
        return mongoTemplate;
    }

    public MongoCustomConversions customConversions() {
        return new MongoCustomConversions(Arrays.asList(new ArrayStringToStringCustomConverter()));
    }

}
