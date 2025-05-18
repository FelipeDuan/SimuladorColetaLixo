package simulador.eventos;

import simulador.caminhoes.CaminhaoPequeno;
import simulador.estacoes.EstacaoDeTransferencia;
import simulador.util.ConsoleCor;
import simulador.util.TempoUtil;

/**
 * Evento responsável por levar um caminhão pequeno até uma estação de transferência para descarregamento.
 * <p>
 * Quando executado, o caminhão é entregue à estação, que decide se ele pode descarregar imediatamente
 * ou será adicionado à fila de espera.
 */
public class EventoEstacaoTransferencia extends Evento{
    private EstacaoDeTransferencia estacao;
    private CaminhaoPequeno caminhao;

    public EventoEstacaoTransferencia(int tempo, EstacaoDeTransferencia estacao, CaminhaoPequeno caminhao){
        super(tempo);
        this.estacao = estacao;
        this.caminhao = caminhao;
    }

    @Override
    public String toString() {
        return String.format("EventoEstacaoTransferencia | Caminhão %s | Estação %s | Horário: %s",
                caminhao.getId(),
                estacao.getNomeEstacao(),
                TempoUtil.formatarHorarioSimulado(tempo));
    }

    @Override
    public void executar() {
        estacao.receberCaminhaoPequeno(caminhao, tempo);
    }
}