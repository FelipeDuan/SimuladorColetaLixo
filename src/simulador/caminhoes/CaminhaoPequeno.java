package simulador.caminhoes;

import simulador.configuracao.ParametrosSimulacao;

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

    public void setId(String id) {
        this.id = id;
    }

    public int getCapacidadeMaxima() {
        return capacidadeMaxima;
    }

    public void setCapacidadeMaxima(int capacidadeMaxima) {
        this.capacidadeMaxima = capacidadeMaxima;
    }

    public void setCargaAtual(int cargaAtual) {
        this.cargaAtual = cargaAtual;
    }

    public int getNumeroDeViagensDiarias() {
        return numeroDeViagensDiarias;
    }

    public void setNumeroDeViagensDiarias(int numeroDeViagensDiarias) {
        this.numeroDeViagensDiarias = numeroDeViagensDiarias;
    }

    public String getZonaAtual() {
        return zonaAtual;
    }

    public void setZonaAtual(String zonaAtual) {
        this.zonaAtual = zonaAtual;
    }

    public boolean isEmViagem() {
        return emViagem;
    }

    public void setEmViagem(boolean emViagem) {
        this.emViagem = emViagem;
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

    public void viajarPara(String zonaAtual) {
        this.zonaAtual = zonaAtual;
        this.emViagem = true;
        System.out.println("Caminhão " + id + " em viagem para " + zonaAtual);
        this.emViagem = false;
    }

    public boolean podeRealizarNovaViagem() {
        return numeroDeViagensDiarias > 0;
    }

    public int calcularTempoViagem(int tempoAtualSimulacaoMinutos) {
        boolean horarioDePico = verificarHorarioDePico(tempoAtualSimulacaoMinutos);

        int tempoMin = horarioDePico ? ParametrosSimulacao.TEMPO_MIN_PICO : ParametrosSimulacao.TEMPO_MIN_FORA_PICO;
        int tempoMax = horarioDePico ? ParametrosSimulacao.TEMPO_MAX_PICO : ParametrosSimulacao.TEMPO_MAX_FORA_PICO;

        int tempoBase = (int) (Math.random() * (tempoMax - tempoMin + 1)) + tempoMin;

        double multiplicador = horarioDePico ? ParametrosSimulacao.MULTIPLICADOR_TEMPO_PICO : ParametrosSimulacao.MULTIPLICADOR_TEMPO_FORA_PICO;

        int tempoFinal = (int) (tempoBase * multiplicador);

        System.out.println("[CAMINHÃO " + id + "]tempo de viagem calculado: " + tempoFinal + " minutos. (Multiplicador: " + multiplicador + ")");
        return tempoFinal;
    }

    public void registrarViagem() {
        if (numeroDeViagensDiarias > 0) {
            numeroDeViagensDiarias--;
            System.out.println("[CAMINHÃO " + id + "] "+ numeroDeViagensDiarias + " VIAGENS RESTANTES" );
        }
    }

    public int getNumeroDeViagensRestantes() {
        return numeroDeViagensDiarias;
    }

    private boolean verificarHorarioDePico(int tempoAtualMinutos) {
        int hora = tempoAtualMinutos / 60;

        return (hora >= ParametrosSimulacao.HORA_INICIO_PICO_MANHA && hora <= ParametrosSimulacao.HORA_FIM_PICO_MANHA)
            || (hora >= ParametrosSimulacao.HORA_INICIO_PICO_TARDE && hora <= ParametrosSimulacao.HORA_FIM_PICO_TARDE);
    }
}
