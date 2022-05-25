package rondanet.upoc.core.utils;

import common.rondanet.pedidos.core.devoluciones.Devoluciones;

import java.util.HashMap;

public class Documento {
    public static final String TIPO_ORDEN = "ORDER"; // Tipo documento para orden de compra
    public static final String TIPO_FACTURA = "INVOICE"; // Tipo documento para fectua
    public static final String TIPO_NOTA_CREDITO = "CREDIT_NOTE"; // Tipo documento para nota de credito
    public static final String TIPO_DEVOLUCION = Devoluciones.NOTA_DEV_TAG; // Tipo documento para nota de credito
    public static final String TIPO_MAIL = "MAIL"; // Tipo documento para nota de credito
    public static final String TIPO_FACTURA_PAGA = "FACPAG"; // Tipo documento para fectua

    public static final HashMap<String, String> formatos = new HashMap<String, String>(){{
        put("eancom", ".txt");
        put("json", ".json");
        put("txt", ".txt");
    }};

    public static final HashMap<String, String> nombreExcel = new HashMap<String, String>(){{
        put("ORDER", "OC");
        put("INVOICE", "FA");
        put("CREDIT_NOTE", "NC");
        put("MAIL", "NOT");
        put("FACPAG", "FP");
    }};

    public static String obtenerNombreParaExportar(String tipoDocumento){
        return nombreExcel.get(tipoDocumento) != null ? nombreExcel.get(tipoDocumento) : "";
    }
}