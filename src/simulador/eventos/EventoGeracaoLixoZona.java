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

    /**
     * Construtor do evento de geração de lixo em uma zona.
     *
     * @param tempo o tempo (em minutos) em que o evento será executado
     * @param zona  a zona onde o lixo será gerado
     */
    public EventoGeracaoLixoZona(int tempo, Zona zona) {
        super(tempo);
        this.zona = zona;
    }

    /**
     * Executa a geração de lixo na zona associada.
     * <p>
     * A quantidade gerada é definida aleatoriamente conforme os limites da zona.
     */
    @Override
    public void executar() {
        zona.gerarLixoDiario();
        System.out.println("[Geração] Zona " + zona.getNome() + " com lixo: " + zona.getLixoAcumulado() + "t");
    }
}
