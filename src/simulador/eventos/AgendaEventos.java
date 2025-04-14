package simulador.eventos;

import estruturas.lista.Lista;

public class AgendaEventos {
    private static Lista<Evento> eventos = new Lista<>();

    /**
     * Adiciona um novo evento à agenda, mantendo a ordem pelo tempo do evento
     * @param evento O evento a ser agendado
     * @throws IllegalArgumentException se o evento for nulo
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
     * Processa todos os eventos na agenda em ordem cronológica
     */
    public static void processarEventos() {
        while (temEventos()) {
            Evento evento = eventos.removerHead();
            evento.executar();
        }
    }

    /**
     * Verifica se existem eventos pendentes na agenda
     * @return true se houver eventos, false caso contrário
     */
    public static boolean temEventos() {
        return !eventos.estaVazia();
    }
}