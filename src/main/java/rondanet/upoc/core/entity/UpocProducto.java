package rondanet.upoc.core.entity;

import common.rondanet.catalogo.core.entity.Producto;
import common.rondanet.pedidos.core.traductor.Oc2;
import org.joda.time.DateTime;
import org.joda.time.Days;

public class UpocProducto {

    private UpocPedido pedidoRealizado;

    private UpocPedido pedidoModificado;

    private UpocPedido pedidoFacturado;

    private boolean existe;

    private boolean existePorGtin;

    private boolean visible;

    private boolean discontinuado;

    private boolean suspendido;

    private boolean coincidenLasUnidadesPedidas;

    public UpocProducto() {
    }

    public void upocPedidoRealizado(Oc2 productoEnOrdenDeCompra, Producto producto) {
        UpocPedido upocPedido = new UpocPedido();
        upocPedido.setCpp(productoEnOrdenDeCompra.getItemIdSellerCode());
        upocPedido.setGtin(productoEnOrdenDeCompra.getItemIdGtin());
        upocPedido.setMarca(producto != null ? producto.getMarca() : "");
        upocPedido.setDescripcion(producto != null ? producto.getDescripcion() : "");
        upocPedido.setNumber(productoEnOrdenDeCompra.getNumber());
        upocPedido.setCantidadDeUnidadesEnElPedido(Integer.parseInt(productoEnOrdenDeCompra.getItemRequestedQuantity()));
        upocPedido.setCantidadDeCajasEnElPedido(Integer.parseInt(productoEnOrdenDeCompra.getNumberOfPackages()));
        this.pedidoRealizado = upocPedido;
        this.visible = producto != null ? true : false;
        this.existe = this.visible;
        this.verificarLasCantidades(producto);
        this.verificarEstadoDelProducto(producto);
    }

    public void verificarLasCantidades(Producto producto) {
        this.coincidenLasUnidadesPedidas = false;
        if (producto != null) {
            this.pedidoRealizado.setMinimoNivelDeVenta(producto.getNivelMinimoVenta() != null ? producto.getNivelMinimoVenta() : 0);
            this.coincidenLasUnidadesPedidas = (this.pedidoRealizado.getMinimoNivelDeVenta() * this.pedidoRealizado.getCantidadDeCajasEnElPedido()) == this.pedidoRealizado.getCantidadDeUnidadesEnElPedido();
        }
    }

    public void verificarEstadoDelProducto(Producto producto) {
        if (producto != null) {
            DateTime fechaActual = new DateTime();
            if (
                    (producto.getSuspendidoDesde() != null && producto.getSuspendidoHasta() == null &&
                            (Days.daysBetween(producto.getSuspendidoDesde().toLocalDate(), fechaActual.toLocalDate())
                                    .getDays() <= 25))
            ) {
                this.discontinuado = true;
            } else if (
                    (producto.getSuspendidoDesde() != null && producto.getSuspendidoHasta() != null)
                            && (Days.daysBetween(fechaActual.toLocalDate(), producto.getSuspendidoHasta().toLocalDate())
                                    .getDays() > 0)) {
                this.suspendido = true;
            }
        }
    }

    public UpocPedido getPedidoRealizado() {
        return pedidoRealizado;
    }

    public void setPedidoRealizado(UpocPedido pedidoRealizado) {
        this.pedidoRealizado = pedidoRealizado;
    }

    public UpocPedido getPedidoModificado() {
        return pedidoModificado;
    }

    public void setPedidoModificado(UpocPedido pedidoModificado) {
        this.pedidoModificado = pedidoModificado;
    }

    public UpocPedido getPedidoFacturado() {
        return pedidoFacturado;
    }

    public void setPedidoFacturado(UpocPedido pedidoFacturado) {
        this.pedidoFacturado = pedidoFacturado;
    }

    public boolean getExiste() {
        return existe;
    }

    public void setExiste(boolean existe) {
        this.existe = existe;
    }

    public boolean getExistePorGtin() {
        return existePorGtin;
    }

    public void setExistePorGtin(boolean existePorGtin) {
        this.existePorGtin = existePorGtin;
    }

    public boolean getVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean getDiscontinuado() {
        return discontinuado;
    }

    public void setDiscontinuado(boolean discontinuado) {
        this.discontinuado = discontinuado;
    }

    public boolean getSuspendido() {
        return suspendido;
    }

    public void setSuspendido(boolean suspendido) {
        this.suspendido = suspendido;
    }

    public boolean getCoincidenLasUnidadesPedidas() {
        return coincidenLasUnidadesPedidas;
    }

    public void setCoincidenLasUnidadesPedidas(boolean coincidenLasUnidadesPedidas) {
        this.coincidenLasUnidadesPedidas = coincidenLasUnidadesPedidas;
    }

}
