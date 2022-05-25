package rondanet.upoc.catalogo.services.interfaces;

import common.rondanet.catalogo.core.entity.ListaDeVenta;
import common.rondanet.clasico.core.afiliados.models.Empresa;
import common.rondanet.clasico.core.afiliados.models.Ubicacion;
import common.rondanet.clasico.core.catalogo.models.*;

import java.util.List;

public interface ICatalogoSincronizadorService {

    public void sincronizarEmpresas(List<Empresa> empresasASincronizar);

    public void sincronizarEmpresasDadasDeBaja(List<Empresa> empresasASincronizar);

    public void sincronizarUbicaciones(List<Ubicacion> ubicacionesASincronizar);

    public void sincronizarUbicacionesDadasDeBaja(List<Ubicacion> ubicacionesASincronizar);

    void enviarPorcientoDeActualizacionGrupos(String mensaje);

    void actualizarGruposAntesDeSincronizar(Empresa empresa, String gln);

    void actualizarGruposDespuesDeSincronizar(Empresa empresa, String gln);

    void eliminarGruposNoActualizados(Empresa empresa);

    public void sincronizarProductos(List<ProductoGtin> productosASincronizar);

    public void sincronizarGrupos(List<Grupo> gruposASincronizar, Empresa empresa);

    ListaDeVenta obtenerListaDeVenta(Ubicacion ubicacion, String gln, Empresa empresa);

    void sincronizarProductosEliminadosDelCatalogoViejo();

    public void sincronizarListaPrecio(List<ListaPrecio> listaPrecios);

    void sincronizarVisibilidad(List<VisibilidadProducto> visibilidadProductosASincronizar, ListaDeVenta listaDeVenta, String gln, long totalRegistros);

    void verificarVisibilidad();
}
