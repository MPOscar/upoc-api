package rondanet.upoc.api;

import common.rondanet.catalogo.core.resources.PaginadoRequest;
import common.rondanet.catalogo.core.resources.Representacion;
import common.rondanet.catalogo.core.resources.UsuarioPrincipal;
import common.rondanet.pedidos.core.resources.DocumentoCatalogoPayload;
import common.rondanet.catalogo.core.resources.PaginadoResponse;
import common.rondanet.pedidos.core.resources.VisorDocumentosFiltro;
import org.eclipse.jetty.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import rondanet.upoc.core.entity.UpocDocumento;
import rondanet.upoc.core.resources.EstadisticasReporteDeOrdenesDeCompra;
import rondanet.upoc.core.security.IAuthenticationFacade;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import rondanet.upoc.core.services.interfaces.IUpocDocumentosService;

import javax.ws.rs.WebApplicationException;
import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/documentosUpoc")
public class DocumentosUpocController {

  Logger logger = LogManager.getLogger(DocumentosUpocController.class);

  private final IAuthenticationFacade authenticationFacade;

  @Autowired
  IUpocDocumentosService upocDocumentosService;

  public DocumentosUpocController(IAuthenticationFacade authenticationFacade) {
    this.authenticationFacade = authenticationFacade;
  }

  @PostMapping("")
  @PreAuthorize("hasRole(T(common.rondanet.catalogo.core.enums.Role).BUSINESS_USER) OR hasRole(T(common.rondanet.catalogo.core.enums.Role).BUSINESS_ADMIN) OR hasRole(T(common.rondanet.catalogo.core.enums.Role).SYSTEM_ADMIN)")
  public Representacion<List<DocumentoCatalogoPayload>> obtenerDocumentosUpoc(
          @RequestParam(defaultValue = "1") Long page,
          @RequestParam(defaultValue = "25") Long limit,
          @RequestParam(defaultValue = "-fecha_envio") String sort,
          @RequestHeader("Authorization") String bearerToken,
          @RequestBody() VisorDocumentosFiltro visorDocumentosFiltro
  ) {
    try {
      PaginadoRequest paginadoRequest = new PaginadoRequest(page, limit);
      paginadoRequest.setSort(sort);
      visorDocumentosFiltro.validarLosDatos();

      UsuarioPrincipal user = authenticationFacade.getPrincipalAuth();

      PaginadoResponse paginadoResponse = this.upocDocumentosService.obtenerListadoDocumentos(
              visorDocumentosFiltro,
              paginadoRequest,
              user.getUsuarioEmpresa()
      );
      Representacion<List<DocumentoCatalogoPayload>> response = new Representacion<>(200,
              paginadoResponse);
      return response;
    } catch (Exception ex) {
      throw new WebApplicationException("Ocurrió un error inesperado, intente nuevamente - " + ex.getMessage(),
              HttpStatus.INTERNAL_SERVER_ERROR_500);
    }
  }

  @PostMapping("/estadisticas")
  @PreAuthorize("hasRole(T(common.rondanet.catalogo.core.enums.Role).BUSINESS_USER) OR hasRole(T(common.rondanet.catalogo.core.enums.Role).BUSINESS_ADMIN) OR hasRole(T(common.rondanet.catalogo.core.enums.Role).SYSTEM_ADMIN)")
  public Representacion<EstadisticasReporteDeOrdenesDeCompra> obtenerEstadisticasReporteDeOrdenesDeCompra(
          @RequestBody() VisorDocumentosFiltro visorDocumentosFiltro
  ) {
    try {
      PaginadoRequest paginadoRequest = new PaginadoRequest();
      visorDocumentosFiltro.validarLosDatos();

      UsuarioPrincipal user = authenticationFacade.getPrincipalAuth();

      EstadisticasReporteDeOrdenesDeCompra estadisticasReporteDeOrdenesDeCompra = this.upocDocumentosService.obtenerEstadisticasReporteDeOrdenesDeCompra(
              visorDocumentosFiltro,
              user.getUsuarioEmpresa()
      );
      Representacion<EstadisticasReporteDeOrdenesDeCompra> response = new Representacion<>(
              200,
              estadisticasReporteDeOrdenesDeCompra
      );
      return response;
    } catch (Exception ex) {
      throw new WebApplicationException("Ocurrió un error inesperado, intente nuevamente - " + ex.getMessage(),
              HttpStatus.INTERNAL_SERVER_ERROR_500);
    }
  }

  @GetMapping("/{idOC}/documento")
  public ResponseEntity obtenerUpocDocumento(
          @PathVariable("idOC") String id
  ) {
    try {
      UsuarioPrincipal user = authenticationFacade.getPrincipalAuth();
      UpocDocumento upocDocumento = this.upocDocumentosService.obtenerUpocDocumento(id, user.getUsuarioEmpresa());
      return ok(upocDocumento);
    } catch (Exception ex) {
      throw new WebApplicationException("Ocurró un error inesperado, intente nuevamente - " + ex.getMessage(),
              HttpStatus.INTERNAL_SERVER_ERROR_500);
    }
  }

}