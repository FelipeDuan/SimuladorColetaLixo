package simulador.eventos;

import estruturas.lista.Lista;

public class AgendaEventos {
    private static Lista<Evento> eventos = new Lista<>();
    private static int tempoUltimoEvento = 0;
    private static Evento ultimoEventoExecutado = null;

    /**
     * Adiciona um novo evento à agenda, mantendo a ordem pelo tempo do evento
     *
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
            tempoUltimoEvento = evento.getTempo();
            ultimoEventoExecutado = evento;
            evento.executar();
        }
    }

    public static int getTempoUltimoEvento() {
        return tempoUltimoEvento;
    }

    public static Evento getUltimoEventoExecutado() {
        return ultimoEventoExecutado;
    }

    public static boolean temEventos() {
        return !eventos.estaVazia();
    }

}