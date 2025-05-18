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
     * @param tempo     o tempo em que o evento será executado (em minutos desde o início da simulação)
     * @param caminhao  o caminhão pequeno responsável pela coleta
     * @param zona      a zona alvo da coleta
     */
    public EventoColeta(int tempo, CaminhaoPequeno caminhao, Zona zona) {
        super(tempo);
        this.caminhao = caminhao;
        this.zonaAtual = zona;
    }


    @Override
    public String toString() {
        return String.format("EventoColeta | Caminhão %s | Zona %s | Horário: %s",
                caminhao.getId(),
                zonaAtual.getNome(),
                TempoUtil.formatarHorarioSimulado(getTempo()));
    }

    @Override
    public void executar() {
        // Verifica disponibilidade na zona
        int qtdZona = zonaAtual.getLixoAcumulado();
        if (qtdZona == 0) {
            System.out.println("  • Zona está limpa. Nenhuma coleta realizada.");

            // Considera como uma tentativa de viagem
            caminhao.registrarViagem();

            if (caminhao.podeRealizarNovaViagem()) {
                // Agenda próxima tentativa (simulando tempo perdido na viagem)
                int tempoDeEspera = 30; // ou o tempo de trajeto real
                AgendaEventos.adicionarEvento(new EventoColeta(tempo + tempoDeEspera, caminhao, caminhao.getZonaAlvo()));
            } else {
                AgendaEventos.adicionarEvento(new EventoTransferenciaParaEstacao(tempo, caminhao, caminhao.getZonaAlvo()));
            }
            return;
        }

        // Coleta
        boolean coletou = false;
        int totalColetado = 0;

        while (caminhao.podeRealizarNovaViagem() &&
                caminhao.getCargaAtual() < caminhao.getCapacidadeMaxima() &&
                zonaAtual.getLixoAcumulado() > 0) {

            int qtdDisponivelZona = zonaAtual.getLixoAcumulado();
            int espacoRestante = caminhao.getCapacidadeMaxima() - caminhao.getCargaAtual();
            int qtdReal = Math.min(qtdDisponivelZona, espacoRestante);

            // Header
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

        // Agendamento de próximo passo
        if (caminhao.podeRealizarNovaViagem() && coletou) {
            int tempoAtual = tempo;

            TempoDetalhado tempoDetalhado = TempoUtil.calcularTempoDetalhado(tempoAtual, totalColetado, false);

            System.out.printf("  • Tempo de coleta: %s%n", TempoUtil.formatarDuracao(tempoDetalhado.tempoColeta));
            System.out.printf("  • Tempo de trajeto: %s%n", TempoUtil.formatarDuracao(tempoDetalhado.tempoDeslocamento));
            if (tempoDetalhado.tempoExtraCarregado > 0)
                System.out.printf("  • Carga cheia: +%s%n", TempoUtil.formatarDuracao(tempoDetalhado.tempoExtraCarregado));

            System.out.printf("  • Horário: %s    Tempo total: %s%n",
                    TempoUtil.formatarHorarioSimulado(tempoAtual + tempoDetalhado.tempoTotal),
                    TempoUtil.formatarDuracao(tempoDetalhado.tempoTotal)
            );
            System.out.println();

            //  Agendamento
            AgendaEventos.adicionarEvento(new EventoColeta(tempoAtual + tempoDetalhado.tempoTotal, caminhao, zonaAtual));
        } else {
            // Finaliza coleta e vai para transferência
            AgendaEventos.adicionarEvento(new EventoTransferenciaParaEstacao(tempo, caminhao, zonaAtual));
        }
    }
}