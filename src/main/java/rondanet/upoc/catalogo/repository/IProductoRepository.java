package rondanet.upoc.catalogo.repository;

import common.rondanet.catalogo.core.entity.Empresa;
import common.rondanet.catalogo.core.entity.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface IProductoRepository extends MongoRepository<Producto, String> {
    public Producto findFirstByOldId(long oldId);

    public Optional<Producto> findFirstByIdAndSempresa(String id, String empresaId);

    public List<Producto> findAllBySgruposConVisibilidadIn(Set<String> gruposConVisibilidad);

    public List<Producto> findAllBySempresasConVisibilidadIn(String empresaId);

    public List<Producto> findAllBySgruposConVisibilidadInOrSempresasConVisibilidadIn(Set<String> gruposConVisibilidad, String empresaId);

    public List<Producto> findAllByEsPublicoAndSempresaNot(boolean esPublico, String empresaId);

    public List<Producto> findAllByEmpresa(Empresa empresa);

    public List<Producto> findAllBySempresa(String empresaId);

    public List<Producto> findAllBySempresaAndSgruposConVisibilidadIn(String empresaId, String grupoId);

    public Page<Producto> findAllBySempresaAndSidNotInAndEliminado(String id, List<String> productos, Pageable pageable);

    public Page<Producto> findAllBySempresaAndSidIn(String id, List<String> productos, Pageable pageable);

    public Page<Producto> findAllBySempresaAndSidNotInAndEsPrivado(String id, List<String> productos, boolean esPrivado, Pageable pageable);

    public Page<Producto> findAllBySempresaAndSidInAndEsPrivado(String id, List<String> productos, boolean esPrivado,  Pageable pageable);

    public Page<Producto> findAllBySempresaAndSidNotInOrSempresaAndEsPublico(String id, List<String> productos, String empresaId, boolean esPublico, Pageable pageable);

    public Page<Producto> findAllBySempresaAndSidInOrSempresaAndEsPublico(String id, List<String> productos, String empresaId, boolean esPublico,  Pageable pageable);

    public Page<Producto> findAllBySempresaAndSidNotInOrSempresa(String id, List<String> productos, String empresaId, Pageable pageable);

    public Page<Producto> findAllBySempresaAndSidInOrSempresa(String id, List<String> productos, String empresaId, Pageable pageable);

    public List<Producto> findAllBySempresaAndSidNotInAndEliminado(String id, List<String> productos, boolean eliminado);

    public List<Producto> findAllBySempresaAndSidInAndEliminado(String id, List<String> productos, boolean eliminado);

    public List<Producto> findAllByDescripcionLike(List<String> descripcion);

}
