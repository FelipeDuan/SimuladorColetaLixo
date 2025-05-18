package simulador.caminhoes;

import simulador.zona.Zona;

public class CaminhaoPequeno {

    private String id;
    private int capacidadeMaxima;
    private int cargaAtual;
    private int numeroDeViagensDiarias;
    private Zona zonaAlvo; // <- NOVO ATRIBUTO

    public CaminhaoPequeno(String id, int capacidadeMaxima, int numeroDeViagensDiarias, Zona zonaAlvo) {
        this.id = id;
        this.capacidadeMaxima = capacidadeMaxima;
        this.cargaAtual = 0;
        this.numeroDeViagensDiarias = numeroDeViagensDiarias;
        this.zonaAlvo = zonaAlvo;
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

    public Zona getZonaAlvo() {
        return zonaAlvo;
    }

    public boolean coletar(int quantidade) {
        if (cargaAtual + quantidade <= capacidadeMaxima) {
            cargaAtual += quantidade;
            System.out.println("[CAMINHÃO " + id + "]" + " Coletou " + quantidade + " toneladas");
            return true;
        }
        System.out.println("[CAMINHÃO " + id + "] Carga máxima atingida.");
        return false;
    }

    public boolean podeRealizarNovaViagem() {
        return numeroDeViagensDiarias > 0;
    }

    public void registrarViagem() {
        if (numeroDeViagensDiarias > 0) {
            numeroDeViagensDiarias--;
            System.out.println("[CAMINHÃO " + id + "] " + numeroDeViagensDiarias + " VIAGENS RESTANTES");
        }
    }
}
