package simulador.caminhoes;

public class CaminhaoPequeno {

    private String id;
    private int capacidadeMaxima;
    private int cargaAtual;
    private int numeroDeViagensDiarias;
    private String zonaAtual;
    private boolean emViagem;

    public CaminhaoPequeno(String id, int capacidadeMaxima, int numeroDeViagensDiarias) {
        this.id = id;
        this.capacidadeMaxima = capacidadeMaxima;
        this.cargaAtual = 0;
        this.numeroDeViagensDiarias = numeroDeViagensDiarias;
        this.emViagem = false;
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
            System.out.println("Caminhão " + id + " coletou " + quantidade + " toneladas. Carga atual: " + cargaAtual + "/" + capacidadeMaxima);
            return true;
        }
        System.out.println("Caminhão " + id + " não pode coletar mais " + quantidade + " toneladas. Carga máxima atingida.");
        return false;
    }

    public int descarregar() {
        int carga = cargaAtual;
        cargaAtual = 0;
        System.out.println("Caminhão " + id + " descarregou " + carga + " toneladas.");
        return carga;
    }

    public void viajarPara(String zonaAtual) {
        this.zonaAtual = zonaAtual;
        this.emViagem = true;
        System.out.println("Caminhão " + id + " em viagem para " + zonaAtual);
        this.emViagem = false;
    }

    public boolean podeRealizarNovaViagem() {
        return numeroDeViagensDiarias > 0;
    }

    public int calcularTempoViagem() {
        // Simula o tempo de viagem entre 5 a 15 minutos, ajustável depois pelos parâmetros
        return (int) (Math.random() * 10 + 5);
    }

    public void registrarViagem() {
        if (numeroDeViagensDiarias > 0) {
            numeroDeViagensDiarias--;
        }
    }
}
