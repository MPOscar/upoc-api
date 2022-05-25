package rondanet.upoc.core.utils;

import rondanet.upoc.UpocConfiguration;
import rondanet.upoc.core.utils.mandrill.MandrillConfiguration;
import com.microtripit.mandrillapp.lutung.MandrillApi;
import com.microtripit.mandrillapp.lutung.model.MandrillApiError;
import com.microtripit.mandrillapp.lutung.view.MandrillMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

@Component
public class MandrillEmailHelper implements IEmailHelper {

    @Value("${MAIL_INFRAESTRUCTURA}")
    private String correosANotificar;

    @Value("${API_URL}")
    private String ambiente;

    MandrillApi mandrillApi; // $NON-NLS-1$
    @SuppressWarnings("unused")
    @Autowired
    private UpocConfiguration configuration;
    @SuppressWarnings("unused")
    private MandrillConfiguration mandrillConfig;
    public MandrillEmailHelper(UpocConfiguration configuration) {
        this.configuration = configuration;
        this.mandrillConfig = configuration.getConfiguracionDespliegue().getMandrill();
        this.mandrillApi = new MandrillApi(this.mandrillConfig.getMandrillApiKey());
    }

    public void sendErrorEmail(String collecion, Exception error){
        SendEmailError(correosANotificar, "Al actualizar " + collecion + " ocurrio un error en el sincronizador de : " + ambiente + " <br> Fecha: " + new Date(), error);
    }

    public void SendEmailError(String emails, String mesaje, Exception error) {
        try {
            MandrillMessage message = new MandrillMessage();
            message.setSubject("Sincronizador"); //$NON-NLS-1$
            message.setHtml(mesaje + "<br>" + error.getMessage() + "<br>" + error.getCause() + "<br>" + error.getStackTrace()); //$NON-NLS-1$ //$NON-NLS-2$
            message.setAutoText(true);
            message.setAutoHtml(true);
            message.setFromEmail(this.mandrillConfig.getFromEmail()); // $NON-NLS-1$
            message.setFromName("Rondanet"); //$NON-NLS-1$

            String[] emailsArray = emails.split(",");
            ArrayList<MandrillMessage.Recipient> recipients = new ArrayList<MandrillMessage.Recipient>();
            for (String email: emailsArray) {
                MandrillMessage.Recipient recipient = new MandrillMessage.Recipient();
                recipient.setEmail(email);
                recipient.setName(email);
                recipients.add(recipient);
            }

            message.setTo(recipients);
            message.setPreserveRecipients(false);
            mandrillApi.messages().send(message, false);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MandrillApiError mandrillApiError) {
            mandrillApiError.printStackTrace();
        }
    }

    public void SendEmail(String email, String mesaje) {
        try {
            MandrillMessage message = new MandrillMessage();
            message.setSubject("Estamos sincronizando productos"); //$NON-NLS-1$
            message.setHtml(mesaje); //$NON-NLS-1$ //$NON-NLS-2$
            message.setAutoText(true);
            message.setAutoHtml(true);
            message.setFromEmail(this.mandrillConfig.getFromEmail()); // $NON-NLS-1$
            message.setFromName("Rondanet"); //$NON-NLS-1$

            ArrayList<MandrillMessage.Recipient> recipients = new ArrayList<MandrillMessage.Recipient>();
            MandrillMessage.Recipient recipient = new MandrillMessage.Recipient();
            recipient.setEmail(email);
            recipient.setName(email);
            recipients.add(recipient);

            message.setTo(recipients);
            message.setPreserveRecipients(false);
            mandrillApi.messages().send(message, false);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MandrillApiError mandrillApiError) {
            mandrillApiError.printStackTrace();
        }
    }
}
