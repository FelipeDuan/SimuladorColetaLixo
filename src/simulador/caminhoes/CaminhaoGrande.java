package simulador.caminhoes;

import static simulador.configuracao.ParametrosSimulacao.CAMINHAO_GRANDE_20T;


/**
 * Representa um caminhão grande utilizado para transportar o lixo das estações de transferência até o aterro sanitário.
 * <p>
 * Cada caminhão grande possui um identificador único, capacidade máxima de carga e armazena o estado da sua carga atual.
 */
public class CaminhaoGrande {
    private static int contadorIds = 1;  // para gerar id automático
    private int id;
    private final int capacidadeMaxima = CAMINHAO_GRANDE_20T;
    private int cargaAtual;
    private boolean carregando;
    private int tempoMaximoEspera;

    public CaminhaoGrande() {
        this.id = contadorIds++;
        this.cargaAtual = 0;
        this.carregando = true;
    }

    public int getId() {
        return id;
    }

    public int getCargaAtual() {
        return cargaAtual;
    }

    public int getCapacidadeMaxima() {
        return capacidadeMaxima;
    }

    public boolean estaCheio() {
        return cargaAtual >= capacidadeMaxima;
    }

    public void receberCarga(int quantidade) {
        if (cargaAtual + quantidade > capacidadeMaxima) {
            cargaAtual = capacidadeMaxima;
        } else {
            cargaAtual += quantidade;
        }
    }

    public void descarregar() {
        System.out.println("Caminhão grande #" + id + " partiu para o aterro com " + cargaAtual + " unidades de carga.");
        cargaAtual = 0;
        carregando = false;
    }

}
