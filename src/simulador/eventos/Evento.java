package simulador.eventos;

/**
 * Classe abstrata que representa um evento genérico no simulador.
 * <p>
 * Esta classe serve como base para todos os tipos de eventos no sistema de simulação,
 * fornecendo a estrutura necessária para ordenação cronológica e execução de lógica específica.
 * <p>
 * Todo evento possui um tempo de execução (em minutos desde o início da simulação) e
 * deve implementar o método {@link #executar()} para definir seu comportamento.
 *
 * @see Comparable
 */
public abstract class Evento implements Comparable<Evento> {

    /**
     * O tempo (em minutos) em que o evento está agendado para ocorrer.
     */
    protected int tempo;

    /**
     * Construtor da classe base {@code Evento}.
     *
     * @param tempo o tempo (em minutos desde o início da simulação) em que o evento deve ocorrer
     * @throws IllegalArgumentException se o tempo for negativo
     */
    public Evento(int tempo) {
        if (tempo < 0) {
            throw new IllegalArgumentException("Tempo não pode ser negativo");
        }
        this.tempo = tempo;
    }

    /**
     * Retorna o tempo em que este evento está agendado para ocorrer.
     *
     * @return o tempo (em minutos) de execução do evento
     */
    public int getTempo() {
        return tempo;
    }

    /**
     * Executa a lógica associada ao evento.
     * <p>
     * Cada tipo de evento deve implementar este método com seu comportamento específico.
     */
    public abstract void executar();

    /**
     * Compara dois eventos com base no tempo de ocorrência.
     * <p>
     * Permite a ordenação automática dos eventos em listas ordenadas, como a agenda da simulação.
     *
     * @param outro o outro evento a ser comparado
     * @return valor negativo se este evento ocorrer antes do outro,
     *         zero se forem simultâneos,
     *         positivo se ocorrer depois
     * @throws NullPointerException se o evento comparado for {@code null}
     */
    @Override
    public int compareTo(Evento outro) {
        if (outro == null) {
            throw new NullPointerException("Evento para comparação não pode ser null");
        }
        return Integer.compare(this.tempo, outro.tempo);
    }
}