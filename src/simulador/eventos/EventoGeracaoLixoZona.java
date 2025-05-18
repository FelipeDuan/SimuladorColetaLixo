package simulador.eventos;

import simulador.zona.Zona;

/**
 * (Não utilizado)
 * <p>
 * Evento responsável por simular a geração diária de lixo em uma determinada zona da cidade.
 * <p>
 * Quando executado, aumenta a quantidade de lixo acumulado na zona, de acordo com
 * os parâmetros definidos para ela.
 */
public class EventoGeracaoLixoZona extends Evento {
    private Zona zona;

    public EventoGeracaoLixoZona(int tempo, Zona zona) {
        super(tempo);
        this.zona = zona;
    }

    @Override
    public void executar() {
        zona.gerarLixoDiario();
        System.out.println("[Geração] Zona " + zona.getNome() + " com lixo: " + zona.getLixoAcumulado() + "t");
    }
}
