package rondanet.upoc.core.entity;

public class UpocPedido {

    private String cpp;

    private String gtin;

    private String descripcion;

    private int cantidadDeUnidadesEnElPedido;

    private int cantidadDeCajasEnElPedido;

    private int minimoNivelDeVenta;

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

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getCantidadDeUnidadesEnElPedido() {
        return cantidadDeUnidadesEnElPedido;
    }

    public void setCantidadDeUnidadesEnElPedido(int cantidadDeUnidadesEnElPedido) {
        this.cantidadDeUnidadesEnElPedido = cantidadDeUnidadesEnElPedido;
    }

    public int getCantidadDeCajasEnElPedido() {
        return cantidadDeCajasEnElPedido;
    }

    public void setCantidadDeCajasEnElPedido(int cantidadDeCajasEnElPedido) {
        this.cantidadDeCajasEnElPedido = cantidadDeCajasEnElPedido;
    }

    public int getMinimoNivelDeVenta() {
        return minimoNivelDeVenta;
    }

    public void setMinimoNivelDeVenta(int minimoNivelDeVenta) {
        this.minimoNivelDeVenta = minimoNivelDeVenta;
    }
}
