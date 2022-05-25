package rondanet.upoc.catalogo.repository;

import common.rondanet.catalogo.core.entity.Empresa;
import common.rondanet.catalogo.core.entity.Grupo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface IGrupoRepository extends MongoRepository<Grupo, String> {

    public Grupo findFirstByOldId(long oldId);

    public List<Grupo> findAllByOldId(long oldId);

    public Optional<Grupo> findFisrtByNombreAndEmpresa(String nombre, Empresa empresa);

    public Optional<Grupo> findFirstByNombreAndSempresa(String nombre, String empresaId);

    public Page<Grupo> findBySempresaAndEliminado(String empresa, boolean eliminado, Pageable pageable);

    public Page<Grupo> findAllBySempresaAndSidNotInAndEliminado(String id, List<String> grupos, Pageable pageable);

    public Page<Grupo> findAllBySempresaAndSidInAndEliminado(String id, List<String> grupos, Pageable pageable);

    public List<Grupo> findAllBySempresaAndSidNotInAndEliminado(String id, List<String> grupos, boolean  eliminado);

    public List<Grupo> findAllBySempresaAndSidInAndEliminado(String id, List<String> grupos, boolean eliminado);
}