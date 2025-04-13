import simulador.caminhoes.CaminhaoPequeno;
import simulador.eventos.AgendaEventos;
import simulador.eventos.EventoColeta;

public class Main {
    public static void main(String[] args) {
        System.out.println("Iniciando simulação de coleta de lixo em Teresina...");

        CaminhaoPequeno caminhao1 = new CaminhaoPequeno("1", 4, 3); // id 1, capacidade 4 ton, 3 viagens

        AgendaEventos.adicionarEvento(new EventoColeta(0, caminhao1));

        AgendaEventos.processarEventos();

        System.out.println("Simulação finalizada.");
    }
}