package simulador.configuracao;

/**
 * Classe contendo todos os parâmetros de configuração da simulação.
 * <p>
 * Centraliza constantes utilizadas ao longo do sistema, permitindo ajustes rápidos e
 * manutenção facilitada. É implementada como um utilitário singleton, com todos os
 * campos estáticos e um construtor privado.
 */
public class ParametrosSimulacao {

    // ==================== CAPACIDADES DE CAMINHÕES ====================
    /** Capacidades em toneladas para caminhões pequenos */
    public static final int CAMINHAO_PEQUENO_2T = 2;
    public static final int CAMINHAO_PEQUENO_4T = 4;
    public static final int CAMINHAO_PEQUENO_8T = 8;
    public static final int CAMINHAO_PEQUENO_10T = 10;

    /** Capacidade em toneladas para caminhão grande */
    public static final int CAMINHAO_GRANDE_20T = 20;

    // ==================== TEMPOS DE OPERAÇÃO ====================
    /** Tempo médio para descarregar por tonelada (em minutos) */
    public static final int TEMPO_DESCARGA_POR_TONELADA = 5;

    /** Tempo médio para carregar por tonelada (em minutos) */
    public static final int TEMPO_COLETA_POR_TONELADA = 10;

    /** Tempo máximo de espera nas estações para caminhões pequenos (minutos) */
    public static final int TEMPO_MAX_ESPERA_CAMINHAO_PEQUENO = 15;

    /** Tolerância de espera para caminhões grandes (minutos) */
    public static final int TOLERANCIA_ESPERA_CAMINHAO_GRANDE = 20;

    // ==================== CONFIGURAÇÕES DE VIAGEM ====================
    /** Intervalo de tempo mínimo para viagens em horário de pico (minutos) */
    public static final int TEMPO_MIN_PICO = 30;

    /** Intervalo de tempo máximo para viagens em horário de pico (minutos) */
    public static final int TEMPO_MAX_PICO = 60;

    /** Intervalo de tempo mínimo para viagens fora de pico (minutos) */
    public static final int TEMPO_MIN_FORA_PICO = 20;

    /** Intervalo de tempo máximo para viagens fora de pico (minutos) */
    public static final int TEMPO_MAX_FORA_PICO = 40;

    /** Número máximo de viagens diárias para caminhões pequenos */
    public static final int MAX_VIAGENS_DIARIAS_CAMINHAO_PEQUENO = 3;

    // ==================== GERAÇÃO DE LIXO POR ZONA ====================
    /** Intervalos de geração de lixo por zona (em toneladas por dia) */
    public static final int LIXO_MIN_SUL = 20;
    public static final int LIXO_MAX_SUL = 40;
    public static final int LIXO_MIN_NORTE = 15;
    public static final int LIXO_MAX_NORTE = 30;
    public static final int LIXO_MIN_CENTRO = 10;
    public static final int LIXO_MAX_CENTRO = 20;
    public static final int LIXO_MIN_LESTE = 15;
    public static final int LIXO_MAX_LESTE = 25;
    public static final int LIXO_MIN_SUDESTE = 18;
    public static final int LIXO_MAX_SUDESTE = 35;

    // ==================== MULTIPLICADORES DE TEMPO ====================
    /** Multiplicador de tempo para horário de pico */
    public static final double MULTIPLICADOR_TEMPO_PICO = 1.5;

    /** Multiplicador de tempo para fora de pico */
    public static final double MULTIPLICADOR_TEMPO_FORA_PICO = 1.0;

    // ==================== HORÁRIOS OPERACIONAIS ====================
    /** Definições de horário de pico (em horas do dia) */
    public static final int HORA_INICIO_PICO_MANHA = 7;
    public static final int HORA_FIM_PICO_MANHA = 9;
    public static final int HORA_INICIO_PICO_TARDE = 17;
    public static final int HORA_FIM_PICO_TARDE = 19;

    /** Intervalo de almoço (em horas do dia) */
    public static final int HORA_DE_ALMOÇO = 12;
    public static final int HORA_DE_ALMOÇO_FIM = 14;

    // ==================== PARÂMETROS DE SIMULAÇÃO ====================
    /** Quantidade de lixo coletado por evento de coleta (em toneladas) */
//    public static final int QUANTIDADE_COLETA_POR_EVENTO 0;

    /** Duração total da simulação em minutos (24 horas) */
    public static final int DURACAO_SIMULACAO_MINUTOS = 1440;

    /** Número de estações de transferência */
    public static final int NUM_ESTACOES_TRANSFERENCIA = 2;

    /**
     * Verifica se um determinado horário está dentro do período de pico.
     *
     * @param hora A hora do dia a ser verificada (formato 24h)
     * @return true se estiver no horário de pico, false caso contrário
     */
    public static boolean isHorarioDePico(int hora) {
        return (hora >= HORA_INICIO_PICO_MANHA && hora < HORA_FIM_PICO_MANHA)
            || (hora >= HORA_INICIO_PICO_TARDE && hora < HORA_FIM_PICO_TARDE);
    }

    /**
     * Construtor privado para evitar instanciação.
     * <p>
     * Esta classe é um utilitário com apenas membros estáticos.
     */
    private ParametrosSimulacao() {
        // Construtor privado para evitar instância
    }
}