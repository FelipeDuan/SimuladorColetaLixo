package simulador.eventos;

import simulador.caminhoes.CaminhaoPequeno;
import simulador.estacoes.EstacaoDeTransferencia;
import simulador.zona.MapeadorZonas;
import simulador.zona.Zona;

public class EventoTransferenciaParaEstacao extends Evento {
    private CaminhaoPequeno caminhaoPequeno;
    private Zona zonaOrigem;

    public EventoTransferenciaParaEstacao(int tempo, CaminhaoPequeno caminhaoPequeno, Zona zonaOrigem) {
        super(tempo);
        this.caminhaoPequeno = caminhaoPequeno;
        zonaOrigem = zonaOrigem;
    }

    @Override
    public void executar() {
        // 1) escolhe a estação certa
        EstacaoDeTransferencia estacaoDestino =
                MapeadorZonas.getEstacaoPara(zonaOrigem);

        estacaoDestino.receberCaminhaoPequeno(caminhaoPequeno, getTempo());

        System.out.printf("[TRANSFERÊNCIA] Caminhão %s → Estação de Transferência%n", caminhaoPequeno.getId());
    }
}
