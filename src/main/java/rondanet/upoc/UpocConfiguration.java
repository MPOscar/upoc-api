package rondanet.upoc;

import common.rondanet.catalogo.core.entity.Param;
import rondanet.upoc.catalogo.db.EmpresasDAO;
import rondanet.upoc.catalogo.repository.IParamRepository;
import rondanet.upoc.catalogo.utils.Propiedades;
import rondanet.upoc.core.utils.DespliegueConfiguration;
import rondanet.upoc.core.utils.mandrill.MandrillConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

@Component
public class UpocConfiguration {

    @Autowired
    IParamRepository paramRepository;

    @Autowired
    EmpresasDAO empresasDAO;

    private DespliegueConfiguration configuracionDespliegue = new DespliegueConfiguration();

    public DespliegueConfiguration getConfiguracionDespliegue() {
        return this.configuracionDespliegue;
    }

    public void setConfiguracionDespliegue(DespliegueConfiguration despliegue) {
        this.configuracionDespliegue = despliegue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UpocConfiguration)) return false;
        UpocConfiguration that = (UpocConfiguration) o;
        return Objects.equals(getConfiguracionDespliegue(), that.getConfiguracionDespliegue());
    }

    @PostConstruct
    public void init() {
        cargarConfiguracion();
    }

    public void cargarConfiguracion() {

        Param despliqueFrontend = paramRepository.findByNombreAndEliminadoIsFalse(Propiedades.DW_DESPLIEGUE_FRONTEND).orElse(null);
        Param despliqueTerceros = paramRepository.findByNombreAndEliminadoIsFalse(Propiedades.DW_DESPLIEGUE_TERCEROS).orElse(null);
        Param despliqueCors = paramRepository.findByNombreAndEliminadoIsFalse(Propiedades.DW_DESPLIEGUE_CORS).orElse(null);
        Param despliqueBucket = paramRepository.findByNombreAndEliminadoIsFalse(Propiedades.DW_DESPLIEGUE_BUCKET).orElse(null);
        Param despliqueBucketUrl = paramRepository.findByNombreAndEliminadoIsFalse(Propiedades.DW_DESPLIEGUE_BUCKET_URL).orElse(null);

        Param s3Id = paramRepository.findByNombreAndEliminadoIsFalse(Propiedades.DW_S3_S3Id).orElse(null);
        Param s3Api = paramRepository.findByNombreAndEliminadoIsFalse(Propiedades.DW_S3_S3APIKEY).orElse(null);

        Param mandrillFromEmail = paramRepository.findByNombreAndEliminadoIsFalse(Propiedades.DW_DESPLIEGUE_MANDRILL_MAIL).orElse(null);
        Param mandrillApiKey = paramRepository.findByNombreAndEliminadoIsFalse(Propiedades.DW_DESPLIEGUE_MANDRILL_APIKEY).orElse(null);
        Param mandrillUrlFronted = paramRepository.findByNombreAndEliminadoIsFalse(Propiedades.DW_DESPLIEGUE_MANDRILL_URL).orElse(null);


        MandrillConfiguration mandrillConfiguration = new MandrillConfiguration(mandrillFromEmail.getValor(), mandrillApiKey.getValor(),mandrillUrlFronted.getValor());

        DespliegueConfiguration despliegueConfiguration = new DespliegueConfiguration(
                Boolean.valueOf(despliqueFrontend.getValor()),
                Boolean.valueOf(despliqueTerceros.getValor()),
                despliqueCors.getValor(),
                despliqueBucket.getValor(),
                despliqueBucketUrl.getValor(),
                mandrillConfiguration);

        this.configuracionDespliegue = despliegueConfiguration;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getConfiguracionDespliegue());
    }

    @Override
    public String toString() {
        return "CatalogoConfiguration{" +
                ", configuracionDespliegue=" + configuracionDespliegue +
                '}';
    }

}
