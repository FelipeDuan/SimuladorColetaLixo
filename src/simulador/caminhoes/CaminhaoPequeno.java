package simulador.caminhoes;

import simulador.configuracao.ParametrosSimulacao;

public class CaminhaoPequeno {

    private String id;
    private int capacidadeMaxima;
    private int cargaAtual;
    private int numeroDeViagensDiarias;
    private boolean emViagem;

    public CaminhaoPequeno(String id, int capacidadeMaxima, int numeroDeViagensDiarias) {
        this.id = id;
        this.capacidadeMaxima = capacidadeMaxima;
        this.cargaAtual = 0;
        this.numeroDeViagensDiarias = numeroDeViagensDiarias;
        this.emViagem = false;
    }

    public int getCapacidadeMaxima() {
        return capacidadeMaxima;
    }

    public int getNumeroDeViagensDiarias() {
        return numeroDeViagensDiarias;
    }

    public String getId() {
        return id;
    }

    public int getCargaAtual() {
        return cargaAtual;
    }

    public boolean coletar(int quantidade) {
        if (cargaAtual + quantidade <= capacidadeMaxima) {
            cargaAtual += quantidade;
            System.out.println("[CAMINHÃO " + id +"]" + " Coletou " + quantidade + " toneladas");
            return true;
        }
        System.out.println("[CAMINHÃO " + id + "] Carga máxima atingida.");
        return false;
    }

    public int descarregar() {
        int carga = cargaAtual;
        cargaAtual = 0;
        System.out.println("[CAMINHÃO " + id + "] Descarregou " + carga + " toneladas.");
        return carga;
    }

    public boolean podeRealizarNovaViagem() {
        return numeroDeViagensDiarias > 0;
    }


    public void registrarViagem() {
        if (numeroDeViagensDiarias > 0) {
            numeroDeViagensDiarias--;
            System.out.println("[CAMINHÃO " + id + "] "+ numeroDeViagensDiarias + " VIAGENS RESTANTES" );
        }
    }
}
