package simulador.configuracao;


/**
 * Classe contendo todos os parâmetros de configuração da simulação.
 * <p>
 * Centraliza constantes utilizadas ao longo do sistema, permitindo ajustes rápidos e
 * manutenção facilitada. É implementada como um utilitário singleton, com todos os
 * campos estáticos e um construtor privado.
 */
public class ParametrosSimulacao {

    public static int DIAS_DE_SIMULACAO;
    public static int QTD_CAMINHOES_2T;
    public static int QTD_CAMINHOES_4T;
    public static int QTD_CAMINHOES_8T;
    public static int VIAGENS_2T;
    public static int VIAGENS_4T;
    public static int VIAGENS_8T;

    public static void setParametrosExternos(Parametros p) {
        DIAS_DE_SIMULACAO = p.dias;
        QTD_CAMINHOES_2T = p.qtd2t;
        VIAGENS_2T = p.viagens2t;
        QTD_CAMINHOES_4T = p.qtd4t;
        VIAGENS_4T = p.viagens4t;
        QTD_CAMINHOES_8T = p.qtd8t;
        VIAGENS_8T = p.viagens8t;
    }

    public static class Parametros {
        public final int dias;
        public final int qtd2t, qtd4t, qtd8t;
        public final int viagens2t, viagens4t, viagens8t;

        public Parametros(
            int dias,
            int qtd2t, int viagens2t,
            int qtd4t, int viagens4t,
            int qtd8t, int viagens8t
        ) {
            this.dias = dias;
            this.qtd2t = qtd2t;
            this.qtd4t = qtd4t;
            this.qtd8t = qtd8t;
            this.viagens2t = viagens2t;
            this.viagens4t = viagens4t;
            this.viagens8t = viagens8t;
        }
    }

    // ==================== TEMPOS DE OPERAÇÃO ====================

    /**
     * Tempo médio para descarregar por tonelada (em minutos)
     */
    public static final int TEMPO_DESCARGA_POR_TONELADA = 5;

    /**
     * Tempo médio para carregar por tonelada (em minutos)
     */
    public static final int TEMPO_COLETA_POR_TONELADA = 10;

    // ==================== CONFIGURAÇÕES DE VIAGEM ====================

    /**
     * Intervalo de tempo mínimo para viagens em horário de pico (minutos)
     */
    public static final int TEMPO_MIN_PICO = 30;

    /**
     * Intervalo de tempo máximo para viagens em horário de pico (minutos)
     */
    public static final int TEMPO_MAX_PICO = 60;

    /**
     * Intervalo de tempo mínimo para viagens fora de pico (minutos)
     */
    public static final int TEMPO_MIN_FORA_PICO = 20;

    /**
     * Intervalo de tempo máximo para viagens fora de pico (minutos)
     */
    public static final int TEMPO_MAX_FORA_PICO = 40;

    /**
     * Número máximo de viagens diárias para caminhões pequenos
     */
    public static final int MAX_VIAGENS_DIARIAS_CAMINHAO_PEQUENO = 3;


    // ==================== GERAÇÃO DE LIXO POR ZONA ====================

    /**
     * Intervalos de geração de lixo por zona (em toneladas por dia)
     */
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

    /**
     * Multiplicador de tempo para horário de pico
     */
    public static final double MULTIPLICADOR_TEMPO_PICO = 1.5;

    /**
     * Multiplicador de tempo para fora de pico
     */
    public static final double MULTIPLICADOR_TEMPO_FORA_PICO = 1.0;


    // ==================== HORÁRIOS OPERACIONAIS ====================

    /**
     * Definições de horário de pico (em horas do dia)
     */
    public static final int HORA_INICIO_PICO_MANHA = 7;
    public static final int HORA_FIM_PICO_MANHA = 9;

    public static final int HORA_INICIO_PICO_TARDE = 17;
    public static final int HORA_FIM_PICO_TARDE = 19;

    /**
     * Intervalo de almoço (Não utilizado diretamente, mas útil para futuras pausas operacionais)
     */
    public static final int HORA_INICIO_PICO_ALMOCO = 12;
    public static final int HORA_FIM_PICO_ALMOCO = 14;


    public static boolean isHorarioDePico(int hora) {
        return (hora >= HORA_INICIO_PICO_MANHA && hora < HORA_FIM_PICO_MANHA)
                || (hora >= HORA_INICIO_PICO_ALMOCO && hora < HORA_FIM_PICO_ALMOCO)
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