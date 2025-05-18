package simulador.util;

/**
 * Representa a decomposição do tempo total de uma operação em três partes:
 * <ul>
 *     <li><b>tempoColeta</b>: tempo gasto na coleta de lixo</li>
 *     <li><b>tempoDeslocamento</b>: tempo estimado de trajeto até a estação</li>
 *     <li><b>tempoExtraCarregado</b>: tempo adicional por estar com carga máxima</li>
 * </ul>
 * A soma dos tempos é armazenada em {@link #tempoTotal}.
 */
public class TempoDetalhado {

    /** Tempo gasto na coleta (em minutos) */
    public final int tempoColeta;

    /** Tempo de deslocamento até o destino (em minutos) */
    public final int tempoDeslocamento;

    /** Tempo extra devido à carga cheia (em minutos) */
    public final int tempoExtraCarregado;

    /** Tempo total da operação (coleta + deslocamento + extra) */
    public final int tempoTotal;

    /**
     * Construtor da estrutura de tempo detalhado.
     *
     * @param tempoColeta         tempo da coleta (min)
     * @param tempoDeslocamento   tempo de trajeto (min)
     * @param tempoExtraCarregado tempo extra devido à carga máxima (min)
     */
    public TempoDetalhado(int tempoColeta, int tempoDeslocamento, int tempoExtraCarregado) {
        this.tempoColeta = tempoColeta;
        this.tempoDeslocamento = tempoDeslocamento;
        this.tempoExtraCarregado = tempoExtraCarregado;
        this.tempoTotal = tempoColeta + tempoDeslocamento + tempoExtraCarregado;
    }
}
