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

        caminhao.coletar(2);

        caminhao.registrarViagem();

        if (caminhao.podeRealizarNovaViagem()) {
            int tempoProximaColeta = tempo + caminhao.calcularTempoViagem(tempo);
            AgendaEventos.adicionarEvento(new EventoColeta(tempoProximaColeta, caminhao));
        } else {
            System.out.println("Caminhão " + caminhao.getId() + " encerrou suas viagens diárias.");
        }
    }
}
