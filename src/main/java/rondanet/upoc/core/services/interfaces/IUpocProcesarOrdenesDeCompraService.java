package rondanet.upoc.core.services.interfaces;

import java.util.Date;

public interface IUpocProcesarOrdenesDeCompraService {

    void procesarOrdenenesDeCompra(Date fechaDeActualizacion);

    void procesarOrdenenesDeCompraParaUPOC(Date fechaDeActualizacion) throws Exception;
}
