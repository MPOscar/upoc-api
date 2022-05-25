package rondanet.upoc.catalogo.repository;

import common.rondanet.catalogo.core.entity.ListaDePrecio;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface IPrecioRepository extends MongoRepository<ListaDePrecio, String> {
    public void deleteAllByGlnListaDeVenta(String glnListaVenta);
    public List<ListaDePrecio> findAllByProductoCppAndGlnListaDeVenta(String productoCpp, String glnListaVenta);
}
