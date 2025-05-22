package simulador.eventos;

import simulador.caminhoes.CaminhaoPequeno;
import simulador.util.ConsoleCor;
import simulador.util.TempoDetalhado;
import simulador.util.TempoUtil;
import simulador.zona.Zona;

/**
 * Evento responsável por realizar a coleta de lixo em uma zona específica por um caminhão pequeno.
 * <p>
 * Executa o processo de coleta conforme a capacidade do caminhão e a quantidade de lixo disponível.
 * Dependendo do resultado, agenda um novo evento de coleta ou transfere o caminhão para a estação.
 */
public class EventoColeta extends Evento {
    private CaminhaoPequeno caminhao;
    private Zona zonaAtual;

    /**
     * Construtor do evento de coleta.
     *
     * @param tempo    o tempo em que o evento será executado (em minutos desde o início da simulação)
     * @param caminhao o caminhão pequeno responsável pela coleta
     * @param zona     a zona alvo da coleta
     */
    public EventoColeta(int tempo, CaminhaoPequeno caminhao, Zona zona) {
        super(tempo);
        this.caminhao = caminhao;
        this.zonaAtual = zona;
    }


    /**
     * Retorna uma representação textual do evento, útil para logs e rastreamento.
     *
     * @return string formatada com dados do caminhão, zona e horário
     */
    @Override
    public String toString() {
        return String.format("EventoColeta | Caminhão %s | Zona %s | Horário: %s",
                caminhao.getId(),
                zonaAtual.getNome(),
                TempoUtil.formatarHorarioSimulado(getTempo()));
    }

    /**
     * Executa a lógica de coleta de lixo:
     * <ul>
     *     <li>Verifica se há lixo disponível na zona</li>
     *     <li>Realiza a coleta respeitando a capacidade do caminhão</li>
     *     <li>Agenda uma nova coleta ou envio para a estação de transferência</li>
     * </ul>
     */
    @Override
    public void executar() {
        int qtdZona = zonaAtual.getLixoAcumulado();

        if (qtdZona == 0) {
            System.out.println("  • Zona está limpa. Nenhuma coleta realizada.");
            caminhao.registrarViagem();

            if (caminhao.podeRealizarNovaViagem()) {
                boolean mudouZona = caminhao.atualizarProximaZonaAlvo();
                if (mudouZona) {
                    AgendaEventos.adicionarEvento(new EventoColeta(tempo + 30, caminhao, caminhao.getZonaAlvo()));
                } else {
                    System.out.println("  • Todas as zonas da rota estão limpas.");
                    AgendaEventos.adicionarEvento(new EventoTransferenciaParaEstacao(tempo, caminhao, zonaAtual));
                }
            } else {
                AgendaEventos.adicionarEvento(new EventoTransferenciaParaEstacao(tempo, caminhao, zonaAtual));
            }
            return;
        }

        boolean coletou = false;
        int totalColetado = 0;

        while (caminhao.podeRealizarNovaViagem() &&
                caminhao.getCargaAtual() < caminhao.getCapacidadeMaxima() &&
                zonaAtual.getLixoAcumulado() > 0) {

            int qtdDisponivelZona = zonaAtual.getLixoAcumulado();
            int espacoRestante = caminhao.getCapacidadeMaxima() - caminhao.getCargaAtual();
            int qtdReal = Math.min(qtdDisponivelZona, espacoRestante);

            String horarioAtual = TempoUtil.formatarHorarioSimulado(tempo);
            System.out.println(ConsoleCor.VERDE + "====================== C O L E T A ======================");
            System.out.printf("[%s] \n", horarioAtual);
            System.out.printf("[COLETA] Caminhão %s → Zona %s | %s Viagens %n", caminhao.getId(), zonaAtual.getNome(), caminhao.getNumeroDeViagensDiarias());

            coletou = caminhao.coletar(qtdReal);
            if (coletou) {
                zonaAtual.coletarLixo(qtdReal);
                totalColetado += qtdReal;

                System.out.printf("  • Coletou: %dt    Carga: %d/%d%n",
                        qtdReal, caminhao.getCargaAtual(), caminhao.getCapacidadeMaxima());
            } else {
                System.out.println("  • Carga máxima atingida.");
                break;
            }
        }
        int tempoAtual = tempo;
        TempoDetalhado tempoDetalhado = TempoUtil.calcularTempoDetalhado(tempoAtual, totalColetado, false);


        if (simulador.ui.SimuladorFXController.isInstanciado()) {
            var controller = simulador.ui.SimuladorFXController.getInstancia();

            controller.animarColeta(
                    caminhao.getId(),
                    zonaAtual.getNome(),
                    caminhao.getCapacidadeMaxima(),
                    tempoDetalhado.tempoTotal
            );

            // Atualiza carga atual no rótulo do caminhão
            controller.atualizarCargaCaminhao(
                    caminhao.getId(),
                    caminhao.getCargaAtual(),
                    caminhao.getCapacidadeMaxima()
            );
        }

        if (caminhao.podeRealizarNovaViagem() && coletou) {


            System.out.printf("  • Tempo de coleta: %s%n", TempoUtil.formatarDuracao(tempoDetalhado.tempoColeta));
            System.out.printf("  • Tempo de trajeto: %s%n", TempoUtil.formatarDuracao(tempoDetalhado.tempoDeslocamento));
            if (tempoDetalhado.tempoExtraCarregado > 0)
                System.out.printf("  • Carga cheia: +%s%n", TempoUtil.formatarDuracao(tempoDetalhado.tempoExtraCarregado));

            System.out.printf("  • Horário: %s    Tempo total: %s%n",
                    TempoUtil.formatarHorarioSimulado(tempoAtual + tempoDetalhado.tempoTotal),
                    TempoUtil.formatarDuracao(tempoDetalhado.tempoTotal));
            System.out.println();


            // Agenda nova coleta
            AgendaEventos.adicionarEvento(new EventoColeta(tempoAtual + tempoDetalhado.tempoTotal, caminhao, zonaAtual));

        } else {
            AgendaEventos.adicionarEvento(new EventoTransferenciaParaEstacao(tempo, caminhao, zonaAtual));
        }
    }
}