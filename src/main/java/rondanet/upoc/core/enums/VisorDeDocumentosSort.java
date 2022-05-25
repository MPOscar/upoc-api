package rondanet.upoc.core.enums;

import java.util.HashMap;

public class VisorDeDocumentosSort {
	public static final HashMap<String, String> columnasParaElSort = new HashMap<String, String>(){{
		put("para", "paraGLN");
		put("fechaEnvio", "mensajeFechaHora");
		put("numero", "documentoNumero");
	}};

	public static String getColumnaParaElSort(String columna){
		String sort = columnasParaElSort.get(columna);
		return sort != null ? sort: "mensajeFechaHora";
	}

	public static String getSort(String sort){
		return sort.contains("-") ? "-" + getColumnaParaElSort(sort.substring(1)) : getColumnaParaElSort(sort);
	}
}