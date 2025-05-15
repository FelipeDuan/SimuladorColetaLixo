package simulador.eventos;

import simulador.caminhoes.CaminhaoPequeno;
import simulador.estacoes.EstacaoDeTransferencia;
import simulador.configuracao.ParametrosSimulacao;
import simulador.util.TempoUtil;

public class EventoTransferenciaParaEstacao extends Evento {
    private CaminhaoPequeno caminhao;

    public EventoTransferenciaParaEstacao(int tempo, CaminhaoPequeno caminhao) {
        super(tempo);
        this.caminhao = caminhao;
    }

    @Override
    public void executar() {
        System.out.println("[CAMINHÃO " + caminhao.getId() + "] chegando na estação de transferência.");

//        caminhao.viajarPara("Estação de Transferência"); // animação e atualização de zona

        int cargaTransferida = caminhao.descarregar();
        caminhao.registrarViagem();

        System.out.println();
        EstacaoDeTransferencia.receberCarga(caminhao, cargaTransferida);

    }
}
