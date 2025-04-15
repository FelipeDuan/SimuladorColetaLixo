package simulador.eventos;

import simulador.caminhoes.CaminhaoPequeno;
import simulador.estacoes.EstacaoDeTransferencia;
import simulador.configuracao.ParametrosSimulacao;

public class EventoTransferenciaParaEstacao extends Evento {
    private CaminhaoPequeno caminhao;

    public EventoTransferenciaParaEstacao(int tempo, CaminhaoPequeno caminhao) {
        super(tempo);
        this.caminhao = caminhao;
    }

    @Override
    public void executar() {
        System.out.println("Tempo " + tempo + ": Caminhão " + caminhao.getId() + " indo para estação de transferência.");

        caminhao.viajarPara("Estação de Transferência"); // animação e atualização de zona

        int cargaTransferida = caminhao.descarregar();

        EstacaoDeTransferencia.receberCarga(caminhao, cargaTransferida);

    }
}
