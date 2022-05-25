package rondanet.upoc.core.resources;

public class EstadisticasReporteDeOrdenesDeCompra {

    private long totalDeOrdenesDeCompra;

    private long totalDeOrdenesDeCompraConError;

    private long totalDeLineasEnOrdenesDeCompra;

    private long totalDeLineasConErrorEnOrdenesDeCompra;

    private long totalDeLineasConWarningEnOrdenesDeCompra;

    public EstadisticasReporteDeOrdenesDeCompra() {
    }

    public long getTotalDeOrdenesDeCompra() {
        return totalDeOrdenesDeCompra;
    }

    public void setTotalDeOrdenesDeCompra(long totalDeOrdenesDeCompra) {
        this.totalDeOrdenesDeCompra = totalDeOrdenesDeCompra;
    }

    public long getTotalDeOrdenesDeCompraConError() {
        return totalDeOrdenesDeCompraConError;
    }

    public void setTotalDeOrdenesDeCompraConError(long totalDeOrdenesDeCompraConError) {
        this.totalDeOrdenesDeCompraConError = totalDeOrdenesDeCompraConError;
    }

    public long getTotalDeLineasEnOrdenesDeCompra() {
        return totalDeLineasEnOrdenesDeCompra;
    }

    public void setTotalDeLineasEnOrdenesDeCompra(long totalDeLineasEnOrdenesDeCompra) {
        this.totalDeLineasEnOrdenesDeCompra = totalDeLineasEnOrdenesDeCompra;
    }

    public long getTotalDeLineasConErrorEnOrdenesDeCompra() {
        return totalDeLineasConErrorEnOrdenesDeCompra;
    }

    public void setTotalDeLineasConErrorEnOrdenesDeCompra(long totalDeLineasConErrorEnOrdenesDeCompra) {
        this.totalDeLineasConErrorEnOrdenesDeCompra = totalDeLineasConErrorEnOrdenesDeCompra;
    }

    public long getTotalDeLineasConWarningEnOrdenesDeCompra() {
        return totalDeLineasConWarningEnOrdenesDeCompra;
    }

    public void setTotalDeLineasConWarningEnOrdenesDeCompra(long totalDeLineasConWarningEnOrdenesDeCompra) {
        this.totalDeLineasConWarningEnOrdenesDeCompra = totalDeLineasConWarningEnOrdenesDeCompra;
    }
}
