package simulador.caminhoes;

import simulador.estacoes.EstacaoDeTransferencia;
import simulador.eventos.AgendaEventos;
import simulador.eventos.EventoGerarCaminhaoGrande;

import java.util.HashMap;
import java.util.Map;

public class GerenciadorCaminhoes {

    private EstacaoDeTransferencia estacao;

    public GerenciadorCaminhoes(EstacaoDeTransferencia estacao) {
        this.estacao = estacao;
    }

    public void registrarCaminhaoPequeno(CaminhaoPequeno caminhao, int tempoAtual, int tempoMaximoEspera) {
        int tempoLimite = tempoAtual + tempoMaximoEspera;
        EventoGerarCaminhaoGrande evento = new EventoGerarCaminhaoGrande(tempoLimite, estacao);
        caminhao.setEventoAgendado(evento);
        AgendaEventos.adicionarEvento(evento);
    }

    public void removerCaminhaoDaEspera(CaminhaoPequeno caminhao) {
        if (caminhao.getEventoAgendado() != null) {
            AgendaEventos.removerEvento(caminhao.getEventoAgendado());
            caminhao.setEventoAgendado(null);
        }
    }

}
