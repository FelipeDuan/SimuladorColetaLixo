package simulador.eventos;

import simulador.caminhoes.CaminhaoPequeno;
import simulador.configuracao.ParametrosSimulacao;

public class EventoColeta extends Evento {
    private CaminhaoPequeno caminhao;

    public EventoColeta(int tempo, CaminhaoPequeno caminhao) {
        super(tempo);
        this.caminhao = caminhao;
    }

    @Override
    public void executar() {
        System.out.println("Tempo " + tempo + ": Executando coleta com caminhão " + caminhao.getId());

        caminhao.coletar(ParametrosSimulacao.QUANTIDADE_COLETA_POR_EVENTO);
        caminhao.registrarViagem();

        if (caminhao.podeRealizarNovaViagem()) {
            int tempoProximaColeta = tempo + caminhao.calcularTempoViagem(tempo);
            AgendaEventos.adicionarEvento(new EventoColeta(tempoProximaColeta, caminhao));
        } else {
            System.out.println("Caminhão " + caminhao.getId() + " encerrou suas viagens diárias.");
            // Aqui podemos disparar evento para ida à estação de transferência
        }
    }
}
