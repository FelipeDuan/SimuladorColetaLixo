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
     * Converte os minutos decorridos desde o início da simulação (07:00)
     * em um horário real no formato "HH:mm".
     *
     * @param minutosDecorridos total de minutos desde as 07:00
     * @return String no formato "HH:mm", representando o horário simulado
     * @throws IllegalArgumentException se minutosDecorridos for negativo
     */
    public static String formatarHorarioSimulado(int minutosDecorridos) {
        if (minutosDecorridos < 0) {
            throw new IllegalArgumentException("Minutos não podem ser negativos");
        }
        int hora = 7 + (minutosDecorridos / 60);
        int minuto = minutosDecorridos % 60;
        return String.format("%02d:%02d", hora, minuto);
    }

    /**
 * Converte uma duração (em minutos) para a forma "Xh Ym" ou "Z min"
 */
    public static String formatarDuracao(int duracaoMinutos) {
        int horas = duracaoMinutos / 60;
        int minutos = duracaoMinutos % 60;
        if (horas > 0) {
            return String.format("%dh %02dmin", horas, minutos);
        } else {
            return String.format("%dmin", minutos);
        }
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