package rondanet.upoc.core.utils.mandrill;

import org.springframework.context.annotation.Configuration;

import javax.ws.rs.DefaultValue;

@Configuration
public class MandrillConfiguration {

    //@DefaultValue("catalogo@catalogorondanet.com")
    private String fromEmail; //= "catalogo@catalogorondanet.com";

    private String mandrillApiKey; //= "n4RCNJWbxJW7xOs9liyZPw";

    @DefaultValue("")
    private String url;

    public MandrillConfiguration() {
    }

    public MandrillConfiguration(String fromEmail, String mandrillApiKey, String url) {
        this.fromEmail = fromEmail;
        this.mandrillApiKey = mandrillApiKey;
        this.url = url;
    }

    public String getFromEmail() {
        return this.fromEmail;
    }

    public void setFromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
    }

    public String getMandrillApiKey() {
        return this.mandrillApiKey;
    }

    public void setMandrillApiKey(String mandrillApiKey) {
        this.mandrillApiKey = mandrillApiKey;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public MandrillConfiguration fromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
        return this;
    }

    public MandrillConfiguration mandrillApiKey(String mandrillApiKey) {
        this.mandrillApiKey = mandrillApiKey;
        return this;
    }

    public MandrillConfiguration url(String url) {
        this.url = url;
        return this;
    }

    @Override
    public String toString() {
        return "{" + " fromEmail='" + getFromEmail() + "'" + ", mandrillApiKey='" + getMandrillApiKey() + "'"
                + ", url='" + getUrl() + "'" + "}";
    }

}