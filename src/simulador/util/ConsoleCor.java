package simulador.util;

/**
 * Classe utilitária que fornece códigos ANSI para formatação colorida no console.
 * <p>
 * Funciona apenas em terminais compatíveis com ANSI (como o terminal do IntelliJ, terminal Linux ou PowerShell moderno).
 * <p>
 * Pode ser usada para destacar logs durante a simulação, melhorando a leitura e o rastreamento visual.
 */
public class ConsoleCor {

    /** Reseta todas as formatações aplicadas */
    public static final String RESET = "\u001B[0m";

    /** Ativa o estilo negrito (bold) */
    public static final String NEGRITO = "\u001B[1m";

    /** Cor do texto: Preto */
    public static final String PRETO = "\u001B[30m";

    /** Cor do texto: Vermelho */
    public static final String VERMELHO = "\u001B[31m";

    /** Cor do texto: Verde */
    public static final String VERDE = "\u001B[32m";

    /** Cor do texto: Amarelo */
    public static final String AMARELO = "\u001B[33m";

    /** Cor do texto: Azul */
    public static final String AZUL = "\u001B[34m";

    /** Cor do texto: Roxo (magenta) */
    public static final String ROXO = "\u001B[35m";

    /** Cor do texto: Ciano (azul claro) */
    public static final String CIANO = "\u001B[36m";

    /** Cor do texto: Branco */
    public static final String BRANCO = "\u001B[37m";
}
