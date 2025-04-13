package simulador.caminhoes;

public class CaminhaoPequeno {

  String id;
    int capacidadeMaxima;
    int cargaAtual;
    int numeroDeViagensDiarias;
    String zonaAtual;
    boolean emViagem;

    public CaminhaoPequeno(int capacidadeMaxima) {
        this.capacidadeMaxima = capacidadeMaxima;
        this.cargaAtual = 0;
        this.emViagem = false;
    }

    public int getCargaAtual() {
        return cargaAtual;
    }

    public boolean coletar(int quantidade) {
        if (cargaAtual + quantidade <= capacidadeMaxima) {
            cargaAtual += quantidade;
            return true;
        }
        return false;
    }

    public int descarregar() {
        int carga = cargaAtual;
        cargaAtual = 0;
        return carga;
    }

    public void viajarPara(String zonaAtual) throws InterruptedException {
        this.zonaAtual = zonaAtual;
        this.emViagem = true;
        System.out.println("Em viagem para " + zonaAtual);
        CaminhaoAnimacoes.caminhaoAndando();
    }

    public void tempoCarregar() {

    }

    public static void main(String[] args) throws InterruptedException {
        CaminhaoAnimacoes.caminhaoAndando();
    }
}

