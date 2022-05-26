package rondanet.upoc.core.entity;

import common.rondanet.pedidos.core.traductor.Fa2;
import rondanet.upoc.core.utils.numbers.NumberValidator;

public class UpocPedido {

    private String cpp;

    private String gtin;

    private String marca;

    private String descripcion;

    private Integer cantidadDeUnidadesEnElPedido;

    private Integer cantidadDeCajasEnElPedido;

    private Integer minimoNivelDeVenta;

    private boolean existeElProducto;

    private String number = "";

    public UpocPedido() {
    }

    public UpocPedido(Fa2 fa2, boolean existeElProducto) {
        this.cpp = fa2.getItemIdSellerCode();
        this.gtin = fa2.getItemIdGtin();
        this.marca = fa2.getItemBrandName();
        this.descripcion = fa2.getItemDescription();
        this.cantidadDeUnidadesEnElPedido = NumberValidator.obtenerValorNumerico(fa2.getItemInvoicedQuantity());
        this.cantidadDeCajasEnElPedido = NumberValidator.obtenerValorNumerico(fa2.getNumberOfPackages());
        this.existeElProducto = existeElProducto;
        this.number = fa2.getNumber();
    }

    public String getCpp() {
        return cpp;
    }

    public void setCpp(String cpp) {
        this.cpp = cpp;
    }

    public String getGtin() {
        return gtin;
    }

    public void setGtin(String gtin) {
        this.gtin = gtin;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getCantidadDeUnidadesEnElPedido() {
        return cantidadDeUnidadesEnElPedido;
    }

    public void setCantidadDeUnidadesEnElPedido(Integer cantidadDeUnidadesEnElPedido) {
        this.cantidadDeUnidadesEnElPedido = cantidadDeUnidadesEnElPedido;
    }

    public Integer getCantidadDeCajasEnElPedido() {
        return cantidadDeCajasEnElPedido;
    }

    public void setCantidadDeCajasEnElPedido(Integer cantidadDeCajasEnElPedido) {
        this.cantidadDeCajasEnElPedido = cantidadDeCajasEnElPedido;
    }

    public Integer getMinimoNivelDeVenta() {
        return minimoNivelDeVenta;
    }

    public void setMinimoNivelDeVenta(Integer minimoNivelDeVenta) {
        this.minimoNivelDeVenta = minimoNivelDeVenta;
    }

    public boolean isExisteElProducto() {
        return existeElProducto;
    }

    public void setExisteElProducto(boolean existeElProducto) {
        this.existeElProducto = existeElProducto;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
