package rondanet.upoc.core.utils.enums;

import java.util.HashMap;

public class UnidadDeMedida {

	public static final HashMap<String, String> unidadesDeMedidaEstandar = new HashMap<>(){{
		put("lt","l");
		put("l","l");
		put("ml","ml");
		put("kg","kg");
		put("gr","g");
		put("g","g");
		put("ug","ug");
		put("mg", "mg");
		put("cc","cc");
		put("m3","m3");
		put("lb","lb");
		put("ea","ea");
		put("ui","ui");
		put("uni","ea");
		put("un","ea");
	}};

	public static final HashMap<String, String> unidadesDeMedidaEstandarCatalogoViejo = new HashMap<>(){{
		put("lt","lt");
		put("l","lt");
		put("ml","ml");
		put("kg","kg");
		put("gr","gr");
		put("g","gr");
		put("ug","ug");
		put("mg", "mg");
		put("cc","cc");
		put("m3","m3");
		put("lb","lb");
		put("ea","uni");
		put("ui","uni");
	}};

	public static String obtenerUnidadDeMedidaEstandar(String unidadDeMedida, boolean paraCatalogoViejo) {
		unidadDeMedida = unidadDeMedida != null ? unidadDeMedida : "";
		String unidad = paraCatalogoViejo ?
				unidadesDeMedidaEstandarCatalogoViejo.get(unidadDeMedida.toLowerCase()) :
				unidadesDeMedidaEstandar.get(unidadDeMedida.toLowerCase());
		return unidad != null ? unidad : unidadDeMedida;
	}

}