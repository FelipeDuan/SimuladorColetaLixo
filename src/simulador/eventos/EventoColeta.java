package simulador.eventos;

import simulador.caminhoes.CaminhaoPequeno;
import simulador.configuracao.ParametrosSimulacao;
import simulador.util.TempoUtil;

public class EventoColeta extends Evento {
    private CaminhaoPequeno caminhao;

    public EventoColeta(int tempo, CaminhaoPequeno caminhao) {
        super(tempo);
        this.caminhao = caminhao;
    }

    @Override
    public void executar() {
        System.out.println("==================================================");
        System.out.println("TEMPO SIMULADO: " + TempoUtil.converterMinutoParaHora(tempo));
        System.out.println("[Coleta] Caminhão " + caminhao.getId());

        boolean coletou = caminhao.coletar(ParametrosSimulacao.QUANTIDADE_COLETA_POR_EVENTO);

        if (!coletou) {
            System.out.println("[Coleta] Carga máxima atingida. Encerrando coletas.");
        }

        caminhao.registrarViagem();
        System.out.println("[Viagens] Restam " + caminhao.getNumeroDeViagensRestantes() + " viagens.");

        if (caminhao.podeRealizarNovaViagem() && coletou) {
            int tempoBase = (int) (Math.random() *
                (ParametrosSimulacao.TEMPO_MAX_FORA_PICO - ParametrosSimulacao.TEMPO_MIN_FORA_PICO + 1)
                + ParametrosSimulacao.TEMPO_MIN_FORA_PICO);

            int tempoReal = TempoUtil.calcularTempoRealDeViagem(tempo, tempoBase);

            System.out.println("[Viagem] Tempo base: " + tempoBase + " min | Tempo real ajustado: " + tempoReal + " min");

            AgendaEventos.adicionarEvento(new EventoColeta(tempo + tempoReal, caminhao));
        } else {
            System.out.println("[Status] Caminhão " + caminhao.getId() + " encerrando jornada e indo para estação.");
            AgendaEventos.adicionarEvento(new EventoTransferenciaParaEstacao(tempo + 1, caminhao));
        }
    }
}
