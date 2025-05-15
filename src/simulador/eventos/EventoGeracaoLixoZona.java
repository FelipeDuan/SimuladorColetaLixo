package simulador.eventos;

import simulador.zona.Zona;

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
