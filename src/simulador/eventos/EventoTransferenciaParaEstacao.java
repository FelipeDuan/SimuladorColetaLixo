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
        // receber o caminhão e armazenar ele em uma fila de caminhoes
//
//levamos em consideraçào a seguinte logica. Quando um caminhão pequeno chega na estação de transferencia ele tem um tempo maximo de espera para descarregar.
// Se não tiver nenhum caminhão grande para receber a carga e o tempo maximo do caminhão pequeno ultrapassar. Então a Estação de transferencia deve gerar um novo caminhão grande.
// Com cuidado claro, para não gerar mais caminhões grande quanto necessario. Para isso pensei na seguinte logica Cada caminhão pequeno ao chegar na estação de transferencia ele
// ira agendar um evento de gerar um caminhão grande.porém se ele for atendido antes do tempo maximo, então esse evento é  excluido.

        System.out.println("[CAMINHÃO " + caminhaoPequeno.getId() + "] chegando na estação de transferência.");

        int cargaTransferida = caminhaoPequeno.descarregar();
        caminhaoPequeno.registrarViagem();

        System.out.println();
        EstacaoDeTransferencia.receberCarga(caminhaoPequeno, cargaTransferida);

    }
}
