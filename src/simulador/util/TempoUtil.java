package simulador.util;

import simulador.configuracao.ParametrosSimulacao;

/**
 * Classe utilitária para conversão e cálculo de tempos na simulação.
 * <p>
 * Fornece métodos para manipulação e transformação de unidades de tempo
 * utilizadas no sistema de simulação.
 */
public class TempoUtil {

    /**
     * Converte minutos totais em formato de hora legível (HH:MM).
     * <p>
     * Considera que a simulação inicia às 07:00 horas.
     *
     * @param minutos Total de minutos decorridos desde o início da simulação
     * @return String formatada no padrão HH:MM representando a hora atual
     * @throws IllegalArgumentException se minutos for negativo
     */
    public static String converterMinutoParaHora(int minutos) {
        if (minutos < 0) {
            throw new IllegalArgumentException("Minutos não podem ser negativos");
        }

        int hora =  minutos / 60;
        int minuto = minutos % 60;
        return String.format("%02d:%02d:00", hora, minuto);
    }

    /**
     * Calcula o tempo real de viagem considerando horários de pico.
     * <p>
     * Aplica multiplicadores de tempo baseado no horário da simulação:
     * <ul>
     *   <li>Horário de pico: tempo prolongado</li>
     *   <li>Fora de pico: tempo normal</li>
     * </ul>
     *
     * @param tempoAtual Minutos decorridos desde o início da simulação
     * @param duracaoBase Duração base da viagem em minutos (sem considerar pico)
     * @return Tempo total ajustado em minutos
     * @throws IllegalArgumentException se qualquer parâmetro for negativo
     */
    public static int calcularTempoRealDeViagem(int tempoAtual, int duracaoBase) {
        if (tempoAtual < 0 || duracaoBase < 0) {
            throw new IllegalArgumentException("Parâmetros de tempo não podem ser negativos");
        }

        int tempoRestante = duracaoBase;
        int tempoSimulado = tempoAtual;
        int tempoFinal = 0;

        while (tempoRestante > 0) {
            int hora = 7 + (tempoSimulado / 60);

            double multiplicador = ParametrosSimulacao.isHorarioDePico(hora)
                    ? ParametrosSimulacao.MULTIPLICADOR_TEMPO_PICO
                    : ParametrosSimulacao.MULTIPLICADOR_TEMPO_FORA_PICO;

            tempoFinal += (int) Math.round(multiplicador);
            tempoSimulado++;
            tempoRestante--;
        }

        return tempoFinal;
    }
}