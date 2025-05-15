package simulador.eventos;

import simulador.caminhoes.CaminhaoPequeno;
import simulador.configuracao.ParametrosSimulacao;
import simulador.util.TempoUtil;
import simulador.zona.Zona;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Representa um evento de coleta de lixo realizado por um caminhão pequeno no simulador.
 * <p>
 * Este evento é responsável por:
 * <ul>
 *   <li>Executar a operação de coleta</li>
 *   <li>Controlar o número de viagens realizadas</li>
 *   <li>Gerenciar a transição para o próximo estado (nova coleta ou retorno à estação)</li>
 * </ul>
 *
 * @see Evento
 * @see CaminhaoPequeno
 */
    public class EventoColeta extends Evento {
        private CaminhaoPequeno caminhao;
        private Zona zona;
        /**
         * Constrói um novo Evento de Coleta.
         *
         * @param tempo O tempo em minutos quando o evento ocorrerá
         * @param caminhao O caminhão pequeno que realizará a coleta
         */
        public EventoColeta(int tempo, CaminhaoPequeno caminhao, Zona zona) {
            super(tempo);
            this.caminhao = caminhao;
            this.zona = zona; // ✔ agora sim: o parâmetro e o campo têm o mesmo nome
        }

    /**
     * Executa as ações do evento de coleta:
     * <ol>
     *   <li>Exibe informações iniciais</li>
     *   <li>Tenta realizar a coleta</li>
     *   <li>Registra a viagem realizada</li>
     *   <li>Decide e agenda o próximo evento (nova coleta ou retorno à estação)</li>
     * </ol>
     * <p>
     * O próximo evento é determinado com base na capacidade do caminhão e no número
     * de viagens restantes.
     *
     * @see ParametrosSimulacao#QUANTIDADE_COLETA_POR_EVENTO
     * @see ParametrosSimulacao#TEMPO_MIN_FORA_PICO
     * @see ParametrosSimulacao#TEMPO_MAX_FORA_PICO
     * @see TempoUtil#calcularTempoRealDeViagem(int, int)
     */
    @Override
    public void executar() {
        // Header
        String horarioAtual = TempoUtil.formatarHorarioSimulado(tempo);
        System.out.println("======================= C O L E T A =======================");
        System.out.printf("[%s] \n",horarioAtual);
System.out.printf("[COLETA] Caminhão %s → Zona %s%n",
             caminhao.getId(), zona.getNome());

        // Verifica disponibilidade na zona
        int qtdZona = zona.getLixoAcumulado();
        if (qtdZona == 0) {
            System.out.println("  • Zona está limpa. Nenhuma coleta realizada.");
            return;
        }

        // Coleta
        int qtdSol = ParametrosSimulacao.QUANTIDADE_COLETA_POR_EVENTO;
        int qtdReal = Math.min(qtdSol, qtdZona);
        boolean coletou = caminhao.coletar(qtdReal);
        if (coletou) {
            zona.coletarLixo(qtdReal);
            System.out.printf("  • Coletou: %dt    Carga: %d/%d%n",
                qtdReal, caminhao.getCargaAtual(), caminhao.getCapacidadeMaxima());
        } else {
            System.out.println("  • Carga máxima atingida. Encerrando jornada.");
        }

        // Agendamento de próximo passo
        if (caminhao.podeRealizarNovaViagem() && coletou) {
            // Define tempo base conforme pico
            boolean pico = ParametrosSimulacao.isHorarioDePico(tempo);
            int min = pico ? ParametrosSimulacao.TEMPO_MIN_PICO : ParametrosSimulacao.TEMPO_MIN_FORA_PICO;
            int max = pico ? ParametrosSimulacao.TEMPO_MAX_PICO : ParametrosSimulacao.TEMPO_MAX_FORA_PICO;
            int tempoBase = ThreadLocalRandom.current().nextInt(min, max + 1);

            int tempoReal = TempoUtil.calcularTempoRealDeViagem(tempo, tempoBase);
            String duracao = TempoUtil.formatarDuracao(tempoReal);
            String proximoHorario = TempoUtil.formatarHorarioSimulado(tempo + tempoReal);

            System.out.printf("  • Tempo de trajeto: %s    Próximo horário: %s%n",
                duracao, proximoHorario);

            // Agenda próxima coleta
            AgendaEventos.adicionarEvento(new EventoColeta(tempo + tempoReal, caminhao, zona));
        } else {
            // Finaliza coleta e vai para transferência
            System.out.println("===========================================================");
            int tTransfer = tempo + 1;
            String hTransfer = TempoUtil.formatarHorarioSimulado(tTransfer);
            System.out.printf("[%s] \n",hTransfer);
            System.out.printf("[TRANSFERÊNCIA] Caminhão %s → Estação de Transferência%n",
                hTransfer, caminhao.getId());
            AgendaEventos.adicionarEvento(new EventoTransferenciaParaEstacao(tTransfer, caminhao));
        }
    }
}