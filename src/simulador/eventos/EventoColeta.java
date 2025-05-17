package simulador.eventos;

import simulador.caminhoes.CaminhaoPequeno;
import simulador.configuracao.ParametrosSimulacao;
import simulador.util.TempoUtil;
import simulador.zona.Zona;

import java.util.concurrent.ThreadLocalRandom;

public class EventoColeta extends Evento {
    private CaminhaoPequeno caminhao;
    private Zona zonaAtual;

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
            System.out.println("======================= C O L E T A =======================");
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
            // 1. Calcular partes separadas
            int tempoColeta = totalColetado * ParametrosSimulacao.TEMPO_COLETA_POR_TONELADA;

            boolean pico = ParametrosSimulacao.isHorarioDePico(tempo);
            int min = pico ? ParametrosSimulacao.TEMPO_MIN_PICO : ParametrosSimulacao.TEMPO_MIN_FORA_PICO;
            int max = pico ? ParametrosSimulacao.TEMPO_MAX_PICO : ParametrosSimulacao.TEMPO_MAX_FORA_PICO;
            int tempoBase = ThreadLocalRandom.current().nextInt(min, max + 1);
            int tempoDeslocamento = TempoUtil.calcularTempoRealDeViagem(tempo, tempoBase);

            // 2. Aplica tempo extra se estiver carregado (aqui: ainda não está carregado, então false)
            int tempoExtraCarregado = 0;
            int tempoTotal = tempoColeta + tempoDeslocamento + tempoExtraCarregado;

            // 3. Logs informativos
            String horario = TempoUtil.formatarHorarioSimulado(tempo + tempoTotal);
            String duracao = TempoUtil.formatarDuracao(tempoTotal);

            System.out.printf("  • Tempo de coleta: %s%n", TempoUtil.formatarDuracao(tempoColeta));
            System.out.printf("  • Tempo de trajeto: %s%n", TempoUtil.formatarDuracao(tempoDeslocamento));
            if (tempoExtraCarregado > 0)
                System.out.printf("  • Carga cheia: +%s%n", TempoUtil.formatarDuracao(tempoExtraCarregado));

            System.out.printf("  • Horário: %s    Tempo total: %s%n", horario, duracao);

            // 4. Agendamento
            AgendaEventos.adicionarEvento(new EventoColeta(tempo + tempoTotal, caminhao, zonaAtual));
        } else {
            // Finaliza coleta e vai para transferência
            System.out.println("===========================================================");
            int tTransfer = tempo + 1;
            String hTransfer = TempoUtil.formatarHorarioSimulado(tTransfer);
            System.out.printf("[%s] \n", hTransfer);
            System.out.printf("[TRANSFERÊNCIA] Caminhão %s → Estação de Transferência%n",
                    caminhao.getId());
            AgendaEventos.adicionarEvento(new EventoTransferenciaParaEstacao(tTransfer, caminhao, zonaAtual));
        }
    }
}