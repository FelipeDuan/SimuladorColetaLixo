import caminhoes.CaminhaoGrande;
import caminhoes.CaminhaoPequeno;
import estacoes.EstacaoDeTransferencia;

public class Simulador {
    CaminhaoPequeno[] caminhaoPequenos;
    CaminhaoGrande[] caminhaoGrandes;
    EstacaoDeTransferencia [] estacaoDeTransferencias;

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
    }

    public void continuar() {

    }

    public void interromper() {

    }

    public void gerarEstatisticas() {

    }
}
