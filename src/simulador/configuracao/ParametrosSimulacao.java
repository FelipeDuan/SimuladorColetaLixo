package simulador.configuracao;

public class ParametrosSimulacao {

    // 🔢 Capacidades dos caminhões pequenos
    public static final int CAMINHAO_PEQUENO_2T = 2;
    public static final int CAMINHAO_PEQUENO_4T = 4;
    public static final int CAMINHAO_PEQUENO_8T = 8;
    public static final int CAMINHAO_PEQUENO_10T = 10;

    // 🔢 Capacidade do caminhão grande
    public static final int CAMINHAO_GRANDE_20T = 20;

    // ⏱️ Intervalo de tempo para viagens (em minutos) - horário de pico
    public static final int TEMPO_MIN_PICO = 5;
    public static final int TEMPO_MAX_PICO = 10;

    // ⏱️ Intervalo de tempo para viagens (em minutos) - fora de pico
    public static final int TEMPO_MIN_FORA_PICO = 3;
    public static final int TEMPO_MAX_FORA_PICO = 7;

    // 🚛 Número máximo de viagens diárias para cada caminhão pequeno
    public static final int MAX_VIAGENS_DIARIAS_CAMINHAO_PEQUENO = 3;

    // ♻️ Intervalos para geração de lixo por zona (em toneladas por dia)
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

    // ⏱️ Multiplicador de tempo para horário de pico
    public static final double MULTIPLICADOR_TEMPO_PICO = 1.5;

    // ⏱️ Multiplicador de tempo para fora de pico
    public static final double MULTIPLICADOR_TEMPO_FORA_PICO = 1.0;

    // 🕒 Definição de horário de pico (em horas do dia)
    public static final int HORA_INICIO_PICO_MANHA = 7;
    public static final int HORA_FIM_PICO_MANHA = 9;

    // 🕒 Intervalo de almoço (caso queira aplicar multiplicador diferente no futuro)
    public static final int HORA_DE_ALMOÇO = 12;
    public static final int HORA_DE_ALMOÇO_FIM = 14;

    public static final int HORA_INICIO_PICO_TARDE = 17;
    public static final int HORA_FIM_PICO_TARDE = 19;

    // ⚙️ Quantidade de lixo coletado por evento de coleta (em toneladas)
    public static final int QUANTIDADE_COLETA_POR_EVENTO = 2;

    // ⏳ Tempo máximo de espera nas estações para caminhões pequenos (minutos)
    public static final int TEMPO_MAX_ESPERA_CAMINHAO_PEQUENO = 15;

    // ⏳ Tolerância de espera para caminhões grandes (minutos)
    public static final int TOLERANCIA_ESPERA_CAMINHAO_GRANDE = 20;

    // 🎛️ Outras configurações úteis
    public static final int DURACAO_SIMULACAO_MINUTOS = 1440; // 24h

    // 👥 Número de estações de transferência
    public static final int NUM_ESTACOES_TRANSFERENCIA = 2;

    private ParametrosSimulacao() {
        // Construtor privado para evitar instância
    }
}
