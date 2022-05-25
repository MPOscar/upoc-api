package rondanet.upoc.pedidos.query;

public class ListaDeVentaQuerys {

    public static final String OCULTAR_DISCONTINUADOS_Y_SUSPENDIDOS = "{'$expr':\n" +
            "\t\t {'$or' :\n" +
            "\t\t [{'$and' :\n" +
            "\t\t [\n" +
            "\t\t {'$eq': [ {'$ifNull': ['$suspendidoDesde', null]}, null]},\n" +
            "\t\t {'$eq': [ {'$ifNull': ['$suspendidoHasta', null]}, null]}\n" +
            "\t\t ]},\n" +
            "\t\t {'$and' :\n" +
            "\t\t [\n" +
            "\t\t {'$gte':[{'$subtract':[new Date(),'$suspendidoHasta']}, 0]},\n" +
            "\t\t {'$ne': [ {'$ifNull': ['$suspendidoDesde', null]}, null]},\n" +
            "\t\t {'$ne': [ {'$ifNull': ['$suspendidoHasta', null]}, null]}\n" +
            "\t\t ]}\n" +
            "\t\t ]}}";

    public static final String MOSTRAR_TODOS = "{'$expr':\n" +
            "\t\t {'$or' :\n" +
            "\t\t [\n" +
            "\t\t {'$and' :\n" +
            "\t\t [\n" +
            "\t\t {'$eq': [ {'$ifNull': ['$suspendidoDesde', null]}, null]},\n" +
            "\t\t {'$eq': [ {'$ifNull': ['$suspendidoHasta', null]}, null]}\n" +
            "\t\t ]},\n" +
            "\t\t {'$and' :\n" +
            "\t\t [\n" +
            "\t\t {'$ne': [ {'$ifNull': ['$suspendidoDesde', null]}, null]},\n" +
            "\t\t {'$ne': [ {'$ifNull': ['$suspendidoHasta', null]}, null]}\n" +
            "\t\t ]},\n" +
            "\t\t {'$and' :\n" +
            "\t\t [\n" +
            "\t\t {'$lte':[{'$subtract':[new Date(),'$suspendidoDesde']}, 2160000000]},\n" +
            "\t\t {'$ne': [ {'$ifNull': ['$suspendidoDesde', null]}, null]},\n" +
            "\t\t {'$eq': [ {'$ifNull': ['$suspendidoHasta', null]}, null]}\n" +
            "\t\t ]}\n" +
            "\t\t ]}}";

}