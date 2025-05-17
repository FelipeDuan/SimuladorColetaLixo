package simulador.eventos;

import simulador.caminhoes.CaminhaoGrande;
import simulador.caminhoes.CaminhaoPequeno;
import simulador.estacoes.EstacaoDeTransferencia;
import simulador.configuracao.ParametrosSimulacao;
import simulador.util.TempoUtil;

public class EventoTransferenciaParaEstacao extends Evento {
    private CaminhaoPequeno caminhaoPequeno;
     private EstacaoDeTransferencia estacao;
    private CaminhaoGrande CaminhaoGrande;

    public EventoTransferenciaParaEstacao(int tempo, CaminhaoPequeno caminhaoPequeno) {
        super(tempo);
        this.caminhaoPequeno = caminhaoPequeno;
    }

    @Override
    public void executar() {

        System.out.println("[CAMINHÃO " + caminhaoPequeno.getId() + "] chegando na estação de transferência.");

        int cargaTransferida = caminhaoPequeno.descarregar();
        caminhaoPequeno.registrarViagem();

        System.out.println();
        EstacaoDeTransferencia.receberCarga(caminhaoPequeno, cargaTransferida);
    }
}
