package rondanet.upoc.pedidos.services.interfaces;

import common.rondanet.clasico.core.afiliados.models.Empresa;

import java.util.List;

public interface IPedidosSincronizadorService {
    public void sincronizarDatosAfiliados(List<Empresa> empresasASincronizar);
}
