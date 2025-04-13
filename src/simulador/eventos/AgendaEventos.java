package simulador.eventos;

import java.util.PriorityQueue;

public class AgendaEventos {
    private static PriorityQueue<Evento> eventos = new PriorityQueue<>();

    public static void adicionarEvento(Evento evento) {
        eventos.add(evento);
    }

    public static void processarEventos() {
        while (!eventos.isEmpty()) {
            Evento evento = eventos.poll();
            evento.executar();
        }
    }
}