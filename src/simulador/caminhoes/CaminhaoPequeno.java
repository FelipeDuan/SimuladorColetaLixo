package simulador.caminhoes;

import simulador.eventos.EventoGerarCaminhaoGrande;
import simulador.zona.Zona;

/**
 * Representa um caminhão pequeno responsável por coletar lixo nas zonas urbanas.
 * <p>
 * Cada caminhão possui uma capacidade máxima, um número limitado de viagens diárias
 * e está associado a uma zona específica de coleta.
 */
public class CaminhaoPequeno {

    private String id;
    private int capacidadeMaxima;
    private int cargaAtual;
    private int numeroDeViagensDiarias;
    private Zona zonaAlvo;
    private EventoGerarCaminhaoGrande eventoAgendado;


    /**
     * Construtor do caminhão pequeno.
     *
     * @param id identificador único do caminhão
     * @param capacidadeMaxima capacidade máxima de carga (em toneladas)
     * @param numeroDeViagensDiarias número total de viagens permitidas por dia
     * @param zonaAlvo zona de coleta associada a este caminhão
     */
    public CaminhaoPequeno(String id, int capacidadeMaxima, int numeroDeViagensDiarias, Zona zonaAlvo) {
        this.id = id;
        this.capacidadeMaxima = capacidadeMaxima;
        this.cargaAtual = 0;
        this.numeroDeViagensDiarias = numeroDeViagensDiarias;
        this.zonaAlvo = zonaAlvo;
    }

    /**
     * Retorna a capacidade máxima de carga do caminhão.
     *
     * @return capacidade máxima em toneladas
     */
    public int getCapacidadeMaxima() {
        return capacidadeMaxima;
    }

    /**
     * Retorna o número restante de viagens diárias.
     *
     * @return número de viagens disponíveis
     */
    public int getNumeroDeViagensDiarias() {
        return numeroDeViagensDiarias;
    }

    /**
     * Retorna o identificador do caminhão.
     *
     * @return ID do caminhão
     */
    public String getId() {
        return id;
    }

    /**
     * Retorna a carga atual do caminhão.
     *
     * @return carga atual em toneladas
     */
    public int getCargaAtual() {
        return cargaAtual;
    }

    /**
     * Retorna a zona de coleta atribuída ao caminhão.
     *
     * @return zona alvo de coleta
     */
    public Zona getZonaAlvo() {
        return zonaAlvo;
    }

    /**
     * Retorna o evento de geração de caminhão grande vinculado a este caminhão.
     *
     * @return evento agendado ou {@code null}
     */
    public EventoGerarCaminhaoGrande getEventoAgendado() {
        return eventoAgendado;
    }

    public void setEventoAgendado(EventoGerarCaminhaoGrande eventoAgendado) {
        this.eventoAgendado = eventoAgendado;
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

    public void descarregar() {
        this.cargaAtual = 0;
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
