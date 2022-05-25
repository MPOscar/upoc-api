package rondanet.upoc.catalogo.repository;

import common.rondanet.catalogo.core.entity.ListaDeVenta;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface IListaDeVentaRepository extends MongoRepository<ListaDeVenta, String> {
    public ListaDeVenta findFirstByOldId(long oldId);
    public List<ListaDeVenta> findAllBySempresaAndSempresasInOrSgruposIn(String proveedor, String empresa,  Set<String> grupos);
    public Optional<ListaDeVenta> findFirstBySubicacionAndNombreAndEliminadoIsFalse(String ubicacion, String nombre);
    public List<ListaDeVenta> findAllBySubicacion(String ubicacion);
    public List<ListaDeVenta> findAllBySempresa(String proveedor);
    public Optional<ListaDeVenta> findBySubicacion(String subicacion);
}
