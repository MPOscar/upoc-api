package rondanet.upoc.core.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import rondanet.upoc.core.entity.UpocDocumento;

import java.util.Optional;

public interface IUpocDocumentoRepository extends MongoRepository<UpocDocumento, String> {

    public Optional<UpocDocumento> findFirstByDocumentoNumero(String documentoNumero);

    public Optional<UpocDocumento> findFirstBySmensaje(String mensajeId);

}
