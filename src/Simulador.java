import simulador.caminhoes.CaminhaoGrande;
import simulador.caminhoes.CaminhaoPequeno;
import simulador.configuracao.ParametrosSimulacao;
import simulador.estacoes.EstacaoDeTransferencia;

public class Simulador {

    CaminhaoGrande[] caminhoesGrandes;
    CaminhaoPequeno[] caminhaoPequenos;
    EstacaoDeTransferencia[] estacaoDeTransferencias;

    float tempoSimulacao;
    boolean rodando;

    public Simulador(float tempoSimulacao) {
        this.tempoSimulacao = tempoSimulacao;
        this.rodando = false;
    }

    public void iniciarSimulacao() {
        rodando = true;
        while (rodando) {
            System.out.println("Simulação Iniciada");
            System.out.println("Rodando...");
        }
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
