import simulador.caminhoes.CaminhaoGrande;
import simulador.caminhoes.CaminhaoPequeno;
import simulador.configuracao.ParametrosSimulacao;
import simulador.estacoes.EstacaoDeTransferencia;
import simulador.eventos.AgendaEventos;
import simulador.eventos.EventoColeta;

public class Simulador {

    CaminhaoGrande[] caminhoesGrandes;
    CaminhaoPequeno[] caminhaoPequenos;
    EstacaoDeTransferencia[] estacaoDeTransferencias;

    float tempoSimulacao;
    boolean rodando;

    public Simulador() {
        this.tempoSimulacao = tempoSimulacao;
        this.rodando = false;
    }

    public void iniciarSimulacao() {
        System.out.println("Iniciando simulação de coleta de lixo em Teresina...");

        CaminhaoPequeno caminhao1 = new CaminhaoPequeno("1", 4, 3); // id 1, capacidade 4 ton, 3 viagens

        AgendaEventos.adicionarEvento(new EventoColeta(0, caminhao1));

        AgendaEventos.processarEventos();

        System.out.println("Simulação finalizada.");
        System.out.println();
        System.out.println("Parâmetros da Simulação:");
        System.out.println("Capacidade caminhão grande: " + ParametrosSimulacao.CAMINHAO_GRANDE_20T + " toneladas");
        System.out.println("Tempo máximo de espera caminhão pequeno: " + ParametrosSimulacao.TEMPO_MAX_ESPERA_CAMINHAO_PEQUENO + " minutos");
    }

    public void continuar() {

    }

    public void interromper() {

    }

    public void gerarEstatisticas() {

    }
}
