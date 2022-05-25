package rondanet.upoc.core.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import common.rondanet.catalogo.core.entity.Empresa;
import common.rondanet.catalogo.core.entity.Entidad;
import common.rondanet.pedidos.core.entity.Mensaje;
import common.rondanet.pedidos.core.utils.serializer.CustomDateTimeDeserializer;
import common.rondanet.pedidos.core.utils.serializer.CustomDateTimeSerializer;
import org.joda.time.DateTime;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;


@Document(collection = "UpocDocumento")
@CompoundIndexes({
        @CompoundIndex(name = "mensajeId", def = "{ 'smensaje': 1 }", unique = true),
})
public class UpocDocumento extends Entidad {

    private String smensaje;

    private int mensajeId;

    private String documentoNumero;

    private String documentoTipo;

    private String deUsuario;

    private String deGLN;

    private String deRazonSocial;

    private String paraUsuario;

    private String paraGLN;

    private String paraRazonSocial;

    private boolean existeEmisor;

    private boolean existeProveedor;

    private int totalDeLineas;

    private int totalDeLineasConError;

    private int totalDeLineasConWarning;

    private boolean tieneErrores;

    @Indexed(direction = IndexDirection.ASCENDING)
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    private DateTime mensajeFechaHora;

    private List<UpocProducto> upocProductos = new ArrayList<UpocProducto>();

    public UpocDocumento() {
    }

    public UpocDocumento(Mensaje mensaje) {
        this.smensaje = mensaje.getId();
        this.mensajeId = mensaje.getMensajeId();
        this.documentoNumero = mensaje.getDocumentoNumero();
        this.documentoTipo = mensaje.getDocumentoTipo();
        this.deUsuario = mensaje.getDeUsuario();
        this.deGLN = mensaje.getDeGLN();
        this.deRazonSocial = mensaje.getDeRazonSocial();
        this.paraUsuario = mensaje.getParaUsuario();
        this.paraGLN = mensaje.getParaGLN();
        this.paraRazonSocial = mensaje.getParaRazonSocial();
        this.mensajeFechaHora = mensaje.getMensajeFechaHora();
    }

    public void validarEmpresas(Empresa empresa, Empresa proveedor) {
        this.existeEmisor = empresa != null;
        this.existeProveedor = proveedor != null;
    }


    public String getSmensaje() {
        return smensaje;
    }

    public void setSmensaje(String smensaje) {
        this.smensaje = smensaje;
    }

    public int getMensajeId() {
        return mensajeId;
    }

    public void setMensajeId(int mensajeId) {
        this.mensajeId = mensajeId;
    }

    public String getDocumentoNumero() {
        return documentoNumero;
    }

    public void setDocumentoNumero(String documentoNumero) {
        this.documentoNumero = documentoNumero;
    }

    public String getDocumentoTipo() {
        return documentoTipo;
    }

    public void setDocumentoTipo(String documentoTipo) {
        this.documentoTipo = documentoTipo;
    }

    public String getDeUsuario() {
        return deUsuario;
    }

    public void setDeUsuario(String deUsuario) {
        this.deUsuario = deUsuario;
    }

    public String getDeGLN() {
        return deGLN;
    }

    public void setDeGLN(String deGLN) {
        this.deGLN = deGLN;
    }

    public String getDeRazonSocial() {
        return deRazonSocial;
    }

    public void setDeRazonSocial(String deRazonSocial) {
        this.deRazonSocial = deRazonSocial;
    }

    public String getParaUsuario() {
        return paraUsuario;
    }

    public void setParaUsuario(String paraUsuario) {
        this.paraUsuario = paraUsuario;
    }

    public String getParaGLN() {
        return paraGLN;
    }

    public void setParaGLN(String paraGLN) {
        this.paraGLN = paraGLN;
    }

    public String getParaRazonSocial() {
        return paraRazonSocial;
    }

    public void setParaRazonSocial(String paraRazonSocial) {
        this.paraRazonSocial = paraRazonSocial;
    }

    public boolean getExisteEmisor() {
        return existeEmisor;
    }

    public void setExisteEmisor(boolean existeEmisor) {
        this.existeEmisor = existeEmisor;
    }

    public boolean getExisteProveedor() {
        return existeProveedor;
    }

    public void setExisteProveedor(boolean existeProveedor) {
        this.existeProveedor = existeProveedor;
    }

    public int getTotalDeLineas() {
        return totalDeLineas;
    }

    public void setTotalDeLineas(int totalDeLineas) {
        this.totalDeLineas = totalDeLineas;
    }

    public int getTotalDeLineasConError() {
        return totalDeLineasConError;
    }

    public void setTotalDeLineasConError(int totalDeLineasConError) {
        this.totalDeLineasConError = totalDeLineasConError;
    }

    public int getTotalDeLineasConWarning() {
        return totalDeLineasConWarning;
    }

    public void setTotalDeLineasConWarning(int totalDeLineasConWarning) {
        this.totalDeLineasConWarning = totalDeLineasConWarning;
    }

    public DateTime getMensajeFechaHora() {
        return mensajeFechaHora;
    }

    public void setMensajeFechaHora(DateTime mensajeFechaHora) {
        this.mensajeFechaHora = mensajeFechaHora;
    }

    public List<UpocProducto> getUpocProductos() {
        return upocProductos;
    }

    public void setUpocProductos(List<UpocProducto> upocProductos) {
        this.upocProductos = upocProductos;
    }

    public boolean getTieneErrores() {
        return tieneErrores;
    }

    public void setTieneErrores(boolean tieneErrores) {
        this.tieneErrores = tieneErrores;
    }
}
