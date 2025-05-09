package simulador.eventos;

import simulador.caminhoes.CaminhaoPequeno;
import simulador.configuracao.ParametrosSimulacao;
import simulador.util.TempoUtil;

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

        /**
         * Constrói um novo Evento de Coleta.
         *
         * @param tempo O tempo em minutos quando o evento ocorrerá
         * @param caminhao O caminhão pequeno que realizará a coleta
         */
        public EventoColeta(int tempo, CaminhaoPequeno caminhao) {
            super(tempo);
            this.caminhao = caminhao;
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
        System.out.println("==================================================");
        System.out.println("[COLETA] CAMINHÃO " + caminhao.getId());

        boolean coletou = caminhao.coletar(ParametrosSimulacao.QUANTIDADE_COLETA_POR_EVENTO);

        if (caminhao.podeRealizarNovaViagem() && coletou) {
            int tempoBase = (int) (Math.random() *
                (ParametrosSimulacao.TEMPO_MAX_FORA_PICO - ParametrosSimulacao.TEMPO_MIN_FORA_PICO + 1)
                + ParametrosSimulacao.TEMPO_MIN_FORA_PICO);

            int tempoReal = TempoUtil.calcularTempoRealDeViagem(tempo, tempoBase);

            System.out.println("TEMPO DE TRAJETO: " + TempoUtil.converterMinutoParaHora(tempoReal));

            int HoraDoDia = AgendaEventos.getTempoUltimoEvento() + tempoReal + 420;
            System.out.println("[" + TempoUtil.converterMinutoParaHora(HoraDoDia)+ "]");

            AgendaEventos.adicionarEvento(new EventoColeta(tempo + tempoReal, caminhao));
        } else {
            System.out.println();
            System.out.println("[Status] Caminhão " + caminhao.getId() + " encerrando jornada e indo para estação.");
            AgendaEventos.adicionarEvento(new EventoTransferenciaParaEstacao(tempo + 1, caminhao));
        }
        System.out.println();
    }
}