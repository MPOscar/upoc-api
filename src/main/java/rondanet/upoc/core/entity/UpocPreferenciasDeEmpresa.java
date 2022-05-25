package rondanet.upoc.core.entity;

import common.rondanet.catalogo.core.entity.Empresa;
import common.rondanet.catalogo.core.entity.Entidad;
import common.rondanet.clasico.core.ediservices.models.Usuario;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Column;

@Document(collection = "UpocPreferenciasDeEmpresa")
@CompoundIndexes({
        @CompoundIndex(name = "sempresa", def = "{ 'sempresa': 1 }", unique = true),
})
public class UpocPreferenciasDeEmpresa extends Entidad {

    private String codigoInterno;

    private String rutEmpresa;

    private String sempresa;

    @Column(name = "UPOC_VAL")
    private String upocVal;

    @Column(name = "UPOC_AUTOEX")
    private String UpocAutoex;

    @Column(name = "UPOC_FILTROGLN")
    private String UpocFiltroGln;

    @Column(name = "UPOC_FILTRODIV")
    private String UpocFiltroDiv;

    @Column(name = "UPOC_GENERAOC")
    private String UpocGeneraOc;

    @Column(name = "UPOC_EDITAOC")
    private String UpocEditaOc;

    @Column(name = "UPOC_SOLOPEN")
    private String UpocSoloOpen;



    public UpocPreferenciasDeEmpresa(Empresa empresa, Usuario usuario) {
        this.codigoInterno = empresa.getCodigoInterno();
        this.rutEmpresa = empresa.getRut();
        this.sempresa = empresa.getId();
        /*this.upocVal = upocVal;
        UpocAutoex = upocAutoex;
        UpocFiltroGln = upocFiltroGln;
        UpocFiltroDiv = upocFiltroDiv;
        UpocGeneraOc = upocGeneraOc;
        UpocEditaOc = upocEditaOc;
        UpocSoloOpen = upocSoloOpen;*/
    }
}
