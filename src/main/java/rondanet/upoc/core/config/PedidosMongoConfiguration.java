package rondanet.upoc.core.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import javax.ws.rs.DefaultValue;

@Configuration
@EnableMongoRepositories(basePackages = "rondanet.upoc.pedidos.repository",
        mongoTemplateRef = "mongoTemplatePedidos")
public class PedidosMongoConfiguration {

    @DefaultValue("pedidos")
    @Value("${mongodb.pedidos.database}")
    private String pedidosDataBase;

    @DefaultValue("mongodb://localhost:27017")
    @Value("${mongodb.pedidos.uri}")
    private String pedidosUri;

    public @Bean
    MongoClient mongoClientPedido() {
        return MongoClients.create(pedidosUri);
    }

    @Bean(name = "mongoTemplatePedidos")
    public MongoTemplate mongoTemplatePedidos() {
        return new MongoTemplate(mongoClientPedido(), pedidosDataBase);
    }
}
