package rondanet.upoc.catalogo.repository;

import common.rondanet.catalogo.core.entity.Empresa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface IEmpresaRepository extends MongoRepository<Empresa, String> {
    public Empresa findFirstByOldId(long oldId);

    public List<Empresa> findAllByIdNotAndEliminado(String id, boolean eliminado);

    public Set<Empresa> findAllBySidNotInAndEliminadoIsFalse(Set<String> id);

    public Page<Empresa> findAllByIdNot(String id, Pageable pageable);

    public List<Empresa> findAllBySidIn(Set<String> sidEmpresas);

    public Page<Empresa> findAllByIdNotAndSidNotInAndEliminado(String id, List<String> empresas, Pageable pageable);

    public Page<Empresa> findAllByIdNotAndSidInAndEliminado(String id, List<String> empresas, Pageable pageable);

    public Page<Empresa> findAllByIdNotAndSidInAndSidNotIn(String id, List<String> empresas,  List<String> empresasSeleccionadas, Pageable pageable);

    public List<Empresa> findAllByIdNotAndSidNotInAndEliminado(String id, List<String> empresas, boolean eliminado);

    public List<Empresa> findAllByIdNotAndSidInAndEliminado(String id, List<String> empresas, boolean eliminado);

    public Empresa findFirstByIdOrRut(String empresaId, String rut);

    public Optional<Empresa> findByRutAndEliminadoIsFalse(Long rut);
}
