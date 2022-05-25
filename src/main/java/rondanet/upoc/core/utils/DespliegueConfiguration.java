package rondanet.upoc.core.utils;

import rondanet.upoc.core.utils.mandrill.MandrillConfiguration;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DespliegueConfiguration {

    private Boolean apiFrontend;

    private Boolean apiTerceros;

    private String cors;

    private String bucket;

    private String bucketUrl;

    private MandrillConfiguration mandrill = new MandrillConfiguration();

    public DespliegueConfiguration() {
    }

    public DespliegueConfiguration(final Boolean apiFrontend, final Boolean apiTerceros, final String cors, final String bucket, final String bucketUrl, final MandrillConfiguration mandrillConfiguration) {
        this.apiFrontend = apiFrontend;
        this.apiTerceros = apiTerceros;
        this.cors = cors;
        this.bucket = bucket;
        this.bucketUrl = bucketUrl;
        this.mandrill = mandrillConfiguration;
    }

    /**
     * Define si se van a desplegar las APIs para el frontend o no.
     * 
     * @return Valor Booleano. Verdadero si se debe publicar la API para frontend
     */
    public Boolean isApiFrontend() {
        return this.apiFrontend;
    }

    /**
     * Define si se van a desplegar las APIs para el frontend o no.
     * 
     * @return Valor Booleano. Verdadero si se debe publicar la API para frontend
     */
    public Boolean getApiFrontend() {
        return this.apiFrontend;
    }

    /**
     * Define si se van a desplegar las APIs para frontend o no.
     * 
     * @return Valor Booleano
     */
    public void setApiFrontend(final Boolean apiFrontend) {
        this.apiFrontend = apiFrontend;
    }

    /**
     * Define si se van a desplegar las APIs para terceros o no.
     * 
     * @return Valor Booleano. Verdadero si se debe publicar la API para terceros
     */
    public Boolean isApiTerceros() {
        return this.apiTerceros;
    }

    /**
     * Define si se van a desplegar las APIs para terceros o no.
     * 
     * @return Valor Booleano. Verdadero si se debe publicar la API para terceros
     */
    public Boolean getApiTerceros() {
        return this.apiTerceros;
    }

    /**
     * Define si se van a desplegar las APIs para terceros o no.
     * 
     * @return Valor Booleano
     */
    public void setApiTerceros(final Boolean apiTerceros) {
        this.apiTerceros = apiTerceros;
    }

    public String getCors() {
        return this.cors;
    }

    public void setCors(final String cors) {
        this.cors = cors;
    }

    public DespliegueConfiguration apiFrontend(final Boolean apiFrontend) {
        this.apiFrontend = apiFrontend;
        return this;
    }

    public DespliegueConfiguration apiTerceros(final Boolean apiTerceros) {
        this.apiTerceros = apiTerceros;
        return this;
    }

    public DespliegueConfiguration cors(final String cors) {
        this.cors = cors;
        return this;
    }

    public String getBucket() {
        return this.bucket;
    }

    public void setBucket(final String bucket) {
        this.bucket = bucket;
    }

    public DespliegueConfiguration bucket(final String bucket) {
        this.bucket = bucket;
        return this;
    }

    public MandrillConfiguration getMandrill() {
        return this.mandrill;
    }

    public void setMandrill(final MandrillConfiguration mandrill) {
        this.mandrill = mandrill;
    }

    public String getBucketUrl() {
        return bucketUrl;
    }

    public void setBucketUrl(String bucketUrl) {
        this.bucketUrl = bucketUrl;
    }

    @Override
    public String toString() {
        return "{" + " apiFrontend='" + isApiFrontend() + "'" + ", apiTerceros='" + isApiTerceros() + "'" + ", cors='"
                + getCors() + "'" + ", bucket='" + "'" + ", mandrill='" + getMandrill() + "'" + "}";
    }

}