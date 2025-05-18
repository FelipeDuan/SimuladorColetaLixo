package simulador.eventos;

import simulador.caminhoes.CaminhaoPequeno;
import simulador.estacoes.EstacaoDeTransferencia;
import simulador.util.ConsoleCor;
import simulador.util.TempoDetalhado;
import simulador.util.TempoUtil;
import simulador.zona.MapeadorZonas;
import simulador.zona.Zona;

public class EventoTransferenciaParaEstacao extends Evento {
    private CaminhaoPequeno caminhaoPequeno;
    private Zona zonaOrigem;

    public EventoTransferenciaParaEstacao(int tempo, CaminhaoPequeno caminhaoPequeno, Zona zonaOrigem) {
        super(tempo);
        this.caminhaoPequeno = caminhaoPequeno;
        this.zonaOrigem = zonaOrigem;
    }

    @Override
    public String toString() {
        return String.format("EventoTransferencia | Caminhão %s | Zona %s | Horário: %s",
                caminhaoPequeno.getId(),
                zonaOrigem.getNome(),
                TempoUtil.formatarHorarioSimulado(getTempo()));
    }

    @Override
    public void executar() {

        // 1) escolhe a estação certa
        EstacaoDeTransferencia estacaoDestino = MapeadorZonas.getEstacaoPara(zonaOrigem);

        // 2) obtém o tempo atual da simulação
        int tempoAtual = getTempo();

        // 3) obtém a carga atual do caminhão
        int cargaAtual = caminhaoPequeno.getCargaAtual();

        // 4) calcula o tempo total considerando que está carregado e horário de pico
        TempoDetalhado tempoDetalhado = TempoUtil.calcularTempoDetalhado(tempoAtual, cargaAtual, true);

        //5) log para debug / info
        System.out.println(ConsoleCor.AZUL + "================ T R A N S F E R Ê N C I A ===============");
        System.out.printf("[%s] \n", TempoUtil.formatarHorarioSimulado(tempoAtual));
        System.out.printf("Caminhão %s → Estação %s%n", caminhaoPequeno.getId(), estacaoDestino.getNomeEstacao());
        System.out.printf("  • Tempo de trajeto: %s%n", TempoUtil.formatarDuracao(tempoDetalhado.tempoDeslocamento));
        if (tempoDetalhado.tempoExtraCarregado > 0) {
            System.out.printf("  • Tempo extra por carga: +%s%n", TempoUtil.formatarDuracao(tempoDetalhado.tempoExtraCarregado));
        }
        System.out.printf("  • Tempo total da viagem: %s%n", TempoUtil.formatarDuracao(tempoDetalhado.tempoTotal));
        System.out.printf("  • Horário previsto de chegada: %s%n", TempoUtil.formatarHorarioSimulado(tempoAtual + tempoDetalhado.tempoTotal));
        System.out.println();

        AgendaEventos.adicionarEvento(new EventoEstacaoTransferencia((tempoAtual + tempoDetalhado.tempoTotal), estacaoDestino, caminhaoPequeno));
    }
}
