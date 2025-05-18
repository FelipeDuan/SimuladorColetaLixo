package simulador.caminhoes;

public class CaminhaoGrande {
    private static int contadorIds = 1;  // para gerar id automático
    private int id;
    private final int capacidadeMaxima = 20000;
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
