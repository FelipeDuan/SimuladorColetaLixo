package simulador.util;

/**
 * Representa uma decomposição do tempo total em coleta, deslocamento e extra por carga.
 */
public class TempoDetalhado {
    public final int tempoColeta;
    public final int tempoDeslocamento;
    public final int tempoExtraCarregado;
    public final int tempoTotal;

    public TempoDetalhado(int tempoColeta, int tempoDeslocamento, int tempoExtraCarregado) {
        this.tempoColeta = tempoColeta;
        this.tempoDeslocamento = tempoDeslocamento;
        this.tempoExtraCarregado = tempoExtraCarregado;
        this.tempoTotal = tempoColeta + tempoDeslocamento + tempoExtraCarregado;
    }
}