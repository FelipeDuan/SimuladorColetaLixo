package simulador.eventos;

import estruturas.lista.Lista;

/**
 * Representa a agenda global de eventos da simulação.
 * <p>
 * Utiliza uma lista ordenada para armazenar e processar os eventos futuros em ordem cronológica.
 * Também armazena informações sobre o último evento executado e o tempo final da simulação.
 */
public class AgendaEventos {
    private static Lista<Evento> eventos = new Lista<>();
    private static int tempoUltimoEvento = 0;
    private static Evento ultimoEventoExecutado = null;

    /**
     * Adiciona um novo evento à agenda, mantendo a ordem de execução com base no tempo.
     *
     * @param evento o evento a ser agendado
     * @throws IllegalArgumentException se o evento for {@code null}
     */
    public static void adicionarEvento(Evento evento) {
        if (evento == null) {
            throw new IllegalArgumentException("Evento não pode ser nulo");
        }

        eventos.adicionarOrdenado(evento, (e1, e2) -> {
            if (e1.getTempo() < e2.getTempo()) return -1;
            if (e1.getTempo() > e2.getTempo()) return 1;
            return 0;
        });
    }

    /**
     * Remove um evento específico da agenda.
     * <p>
     * Esse método é útil para cancelar eventos agendados dinamicamente, como eventos de tolerância.
     *
     * @param evento o evento a ser removido
     * @return {@code true} se o evento foi encontrado e removido, {@code false} caso contrário
     */
    public static boolean removerEvento(Evento evento) {
        return eventos.removerProcurado(evento);
    }

    /**
     * Processa todos os eventos da agenda em ordem cronológica.
     * <p>
     * Para cada evento, atualiza o tempo da simulação e executa a lógica correspondente.
     */
    public static void processarEventos() {
        while (temEventos()) {
            Evento evento = eventos.removerHead();
            tempoUltimoEvento = evento.getTempo();
            ultimoEventoExecutado = evento;
            evento.executar();
        }
    }

    /**
     * Retorna o tempo em minutos do último evento processado.
     *
     * @return tempo do último evento
     */
    public static int getTempoUltimoEvento() {
        return tempoUltimoEvento;
    }

    /**
     * Retorna o último evento executado na simulação.
     *
     * @return o último {@link Evento} executado
     */
    public static Evento getUltimoEventoExecutado() {
        return ultimoEventoExecutado;
    }

    /**
     * Verifica se ainda existem eventos pendentes na agenda.
     *
     * @return {@code true} se houver eventos, {@code false} caso contrário
     */
    public static boolean temEventos() {
        return !eventos.estaVazia();
    }

}