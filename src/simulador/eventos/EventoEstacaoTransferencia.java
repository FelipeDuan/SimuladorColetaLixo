package simulador.eventos;

import simulador.caminhoes.CaminhaoPequeno;
import simulador.estacoes.EstacaoDeTransferencia;
import simulador.util.ConsoleCor;
import simulador.util.TempoUtil;

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
