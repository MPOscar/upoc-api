package rondanet.upoc.core.services.implementations;

import common.rondanet.catalogo.core.entity.UsuarioEmpresa;
import common.rondanet.catalogo.core.exceptions.ServiceException;
import common.rondanet.catalogo.core.resources.PaginadoRequest;
import common.rondanet.catalogo.core.resources.PaginadoResponse;
import common.rondanet.pedidos.core.entity.DatosAfiliados;
import common.rondanet.pedidos.core.resources.OCCatalogoRequest;
import common.rondanet.pedidos.core.resources.VisorDocumentosFiltro;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rondanet.upoc.catalogo.db.EmpresasDAO;
import rondanet.upoc.core.db.UpocDocumentoDAO;
import rondanet.upoc.core.entity.UpocDocumento;
import rondanet.upoc.core.enums.VisorDeDocumentosSort;
import rondanet.upoc.core.resources.EstadisticasReporteDeOrdenesDeCompra;
import rondanet.upoc.core.services.interfaces.IUpocDocumentosService;
import rondanet.upoc.pedidos.db.DatosAfiliadosDAO;

import java.util.*;

@Service
public class UpocDocumentosService implements IUpocDocumentosService {

	Logger logger = LogManager.getLogger(UpocDocumentosService.class);

	@Autowired
	UpocDocumentoDAO upocDocumentoDAO;

	@Autowired
	DatosAfiliadosDAO datosAfiliadosDAO;

	@Autowired
	EmpresasDAO empresasDAO;

	@Override
	public PaginadoResponse obtenerListadoDocumentos(
			VisorDocumentosFiltro visorDocumentosFiltro,
			PaginadoRequest paginadoRequest,
			UsuarioEmpresa ue
	) throws ServiceException {
		try {
			String rutEmpresa = ue.getEmpresa().getRut();
			Optional<DatosAfiliados> optional = datosAfiliadosDAO.findByRut(rutEmpresa);
			if (!optional.isPresent()) {
				throw new ServiceException("Error: No existen datos para conectarse a Rondanet");
			}

			buscarLosCodigosInternosDeLasEmpresasAFiltrar(visorDocumentosFiltro);
			PaginadoResponse<List<UpocDocumento>> paginadoResponse = upocDocumentoDAO.obtenerListaDeDocumentos(
					optional.get().getNroEmpresa(),
					visorDocumentosFiltro,
					paginadoRequest.getPage().intValue() - 1,
					paginadoRequest.getLimit().intValue(),
					VisorDeDocumentosSort.getSort(paginadoRequest.getSort()
					));
			return paginadoResponse;
		} catch (Exception e) {
			final String error = "Error al obtener las Órdenes de Compra del Servicio RN-WS. ";
			throw new ServiceException(error);
		}
	}

	@Override
	public EstadisticasReporteDeOrdenesDeCompra obtenerEstadisticasReporteDeOrdenesDeCompra(
			VisorDocumentosFiltro visorDocumentosFiltro,
			UsuarioEmpresa ue
	) throws ServiceException {
		try {
			String rutEmpresa = ue.getEmpresa().getRut();
			Optional<DatosAfiliados> optional = datosAfiliadosDAO.findByRut(rutEmpresa);
			if (!optional.isPresent()) {
				throw new ServiceException("Error: No existen datos para conectarse a Rondanet");
			}

			buscarLosCodigosInternosDeLasEmpresasAFiltrar(visorDocumentosFiltro);
			EstadisticasReporteDeOrdenesDeCompra estadisticasReporteDeOrdenesDeCompra = upocDocumentoDAO.obtenerEstadisticasReporteDeOrdenesDeCompra(
					optional.get().getNroEmpresa(),
					visorDocumentosFiltro
			);
			return estadisticasReporteDeOrdenesDeCompra;
		} catch (Exception e) {
			final String error = "Error al obtener las Órdenes de Compra del Servicio RN-WS. ";
			throw new ServiceException(error);
		}
	}

	public void buscarLosCodigosInternosDeLasEmpresasAFiltrar(VisorDocumentosFiltro visorDocumentosFiltro){
		if(!visorDocumentosFiltro.getEmpresas().isEmpty()) {
			List<String> empresasAFiltrar = empresasDAO.getAllCodigoInternoEmpresasPorId(visorDocumentosFiltro.getEmpresas());
			visorDocumentosFiltro.setEmpresas(empresasAFiltrar);
		}
	}

	@Override
	public UpocDocumento obtenerUpocDocumento(String idOC, UsuarioEmpresa usuarioEmpresa) throws Exception {
		Optional<UpocDocumento> optionalUpocDocumento = upocDocumentoDAO.findById(idOC);
		return optionalUpocDocumento.isPresent() ? optionalUpocDocumento.get() : null;
	}
}
