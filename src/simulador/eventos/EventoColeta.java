package simulador.eventos;

import simulador.caminhoes.CaminhaoPequeno;

public class EventoColeta extends Evento {
    private CaminhaoPequeno caminhao;

    public EventoColeta(int tempo, CaminhaoPequeno caminhao) {
        super(tempo);
        this.caminhao = caminhao;
    }

    @Override
    public void executar() {
        System.out.println("Executando coleta com caminhão " + caminhao.getId() + " no tempo " + tempo);

        caminhao.coletar(2); // simula coleta de 2 toneladas por evento, podemos ajustar isso dinamicamente

        caminhao.registrarViagem();

        if (caminhao.podeRealizarNovaViagem()) {
            int tempoProximaColeta = tempo + caminhao.calcularTempoViagem();
            AgendaEventos.adicionarEvento(new EventoColeta(tempoProximaColeta, caminhao));
        }else{
            System.out.println("Caminhão " + caminhao.getId() + " encerrou suas viagens diárias.");
        }
    }
}
