package simulador.zona;

import simulador.configuracao.ParametrosSimulacao;

public class Zonas {

    public static Zona zonaSul() {
        return new Zona("Sul", ParametrosSimulacao.LIXO_MIN_SUL, ParametrosSimulacao.LIXO_MAX_SUL);
    }

    public static Zona zonaNorte() {
        return new Zona("Norte", ParametrosSimulacao.LIXO_MIN_NORTE, ParametrosSimulacao.LIXO_MAX_NORTE);
    }

    public static Zona zonaCentro() {
        return new Zona("Centro", ParametrosSimulacao.LIXO_MIN_CENTRO, ParametrosSimulacao.LIXO_MAX_CENTRO);
    }

    public static Zona zonaLeste() {
        return new Zona("Leste", ParametrosSimulacao.LIXO_MIN_LESTE, ParametrosSimulacao.LIXO_MAX_LESTE);
    }

    public static Zona zonaSudeste() {
        return new Zona("Sudeste", ParametrosSimulacao.LIXO_MIN_SUDESTE, ParametrosSimulacao.LIXO_MAX_SUDESTE);
    }
}
