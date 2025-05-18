package simulador.zona;

import simulador.configuracao.ParametrosSimulacao;

/**
 * Classe utilitária responsável por fornecer instâncias pré-configuradas de zonas da cidade.
 * <p>
 * Cada método estático retorna uma {@link Zona} com nome e faixa de geração de lixo
 * baseada nos parâmetros definidos em {@link ParametrosSimulacao}.
 * <p>
 * Utilizada no início da simulação para instanciar zonas reais do cenário.
 */
public class Zonas {

    /**
     * (Não utilizado)
     * <p>
     * Cria a zona Sul com seus respectivos parâmetros de geração de lixo.
     *
     * @return instância de {@link Zona} configurada para a região Sul
     */
    public static Zona zonaSul() {
        return new Zona("Sul", ParametrosSimulacao.LIXO_MIN_SUL, ParametrosSimulacao.LIXO_MAX_SUL);
    }

    /**
     * Cria a zona Norte com seus respectivos parâmetros de geração de lixo.
     *
     * @return instância de {@link Zona} configurada para a região Norte
     */
    public static Zona zonaNorte() {
        return new Zona("Norte", ParametrosSimulacao.LIXO_MIN_NORTE, ParametrosSimulacao.LIXO_MAX_NORTE);
    }

    /**
     * (Não utilizado)
     * <p>
     * Cria a zona Centro com seus respectivos parâmetros de geração de lixo.
     *
     * @return instância de {@link Zona} configurada para a região Central
     */
    public static Zona zonaCentro() {
        return new Zona("Centro", ParametrosSimulacao.LIXO_MIN_CENTRO, ParametrosSimulacao.LIXO_MAX_CENTRO);
    }

    /**
     * Cria a zona Leste com seus respectivos parâmetros de geração de lixo.
     *
     * @return instância de {@link Zona} configurada para a região Leste
     */
    public static Zona zonaLeste() {
        return new Zona("Leste", ParametrosSimulacao.LIXO_MIN_LESTE, ParametrosSimulacao.LIXO_MAX_LESTE);
    }

    /**
     * (Não utilizado)
     * <p>
     * Cria a zona Sudeste com seus respectivos parâmetros de geração de lixo.
     *
     * @return instância de {@link Zona} configurada para a região Sudeste
     */
    public static Zona zonaSudeste() {
        return new Zona("Sudeste", ParametrosSimulacao.LIXO_MIN_SUDESTE, ParametrosSimulacao.LIXO_MAX_SUDESTE);
    }
}
