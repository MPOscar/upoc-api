package rondanet.upoc.core.services.interfaces;

import common.rondanet.catalogo.core.entity.UsuarioEmpresa;
import common.rondanet.catalogo.core.exceptions.ServiceException;
import common.rondanet.catalogo.core.resources.PaginadoRequest;
import common.rondanet.catalogo.core.resources.PaginadoResponse;
import common.rondanet.pedidos.core.resources.VisorDocumentosFiltro;
import rondanet.upoc.core.entity.UpocDocumento;
import rondanet.upoc.core.resources.EstadisticasReporteDeOrdenesDeCompra;

public interface IUpocDocumentosService {

    PaginadoResponse obtenerListadoDocumentos(
            VisorDocumentosFiltro visorDocumentosFiltro,
            PaginadoRequest paginadoRequest,
            UsuarioEmpresa ue
    ) throws ServiceException;

    EstadisticasReporteDeOrdenesDeCompra obtenerEstadisticasReporteDeOrdenesDeCompra(
            VisorDocumentosFiltro visorDocumentosFiltro,
            UsuarioEmpresa ue
    ) throws ServiceException;

    UpocDocumento obtenerUpocDocumento(String idOC, UsuarioEmpresa usuarioEmpresa) throws Exception;
}
