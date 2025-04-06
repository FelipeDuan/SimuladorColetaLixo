package edu.simulador.model;

public class Simulador {
    CaminhaoPequeno[] caminhoesPequenos;
    CaminhaoGrande[] caminhoesGrandes;
    EstacaoDeTransferencia[] estacoes;
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


    public void pausar() {

    }

    public void continuar() {

    }

    public void interromper() {

    }

    public void gerarEstatisticas() {

    }
}
