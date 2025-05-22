package simulador.eventos;

import simulador.caminhoes.CaminhaoPequeno;
import simulador.estacoes.EstacaoDeTransferencia;
import simulador.util.ConsoleCor;
import simulador.util.TempoDetalhado;
import simulador.util.TempoUtil;
import simulador.zona.MapeadorZonas;
import simulador.zona.Zona;

/**
 * Evento responsável por realizar a transferência de um caminhão pequeno para uma estação de transferência.
 * <p>
 * Quando executado, calcula o tempo de deslocamento com base na carga e no horário atual,
 * e agenda a chegada do caminhão à estação por meio de um {@link EventoEstacaoTransferencia}.
 */
public class EventoTransferenciaParaEstacao extends Evento {

    private CaminhaoPequeno caminhaoPequeno;
    private Zona zonaOrigem;

    /**
     * Construtor do evento de transferência de caminhão pequeno para a estação.
     *
     * @param tempo           o tempo (em minutos) da execução do evento
     * @param caminhaoPequeno o caminhão pequeno que será transferido
     * @param zonaOrigem      a zona de onde o caminhão está partindo
     */
    public EventoTransferenciaParaEstacao(int tempo, CaminhaoPequeno caminhaoPequeno, Zona zonaOrigem) {
        super(tempo);
        this.caminhaoPequeno = caminhaoPequeno;
        this.zonaOrigem = zonaOrigem;
    }

    /**
     * Retorna uma descrição resumida do evento, útil para logs e monitoramento.
     *
     * @return string contendo caminhão, zona de origem e horário do evento
     */
    @Override
    public String toString() {
        return String.format("EventoTransferencia | Caminhão %s | Zona %s | Horário: %s",
                caminhaoPequeno.getId(),
                zonaOrigem.getNome(),
                TempoUtil.formatarHorarioSimulado(getTempo()));
    }

    /**
     * Executa o evento de transferência.
     * <p>
     * Calcula o tempo de deslocamento do caminhão pequeno até a estação de transferência
     * e agenda um {@link EventoEstacaoTransferencia} correspondente para o horário previsto de chegada.
     */
    @Override
    public void executar() {
        // 1) Seleciona a estação de destino com base na zona de origem
        EstacaoDeTransferencia estacaoDestino = MapeadorZonas.getEstacaoPara(zonaOrigem);

        // 2) Tempo atual da simulação
        int tempoAtual = getTempo();

        // 3) Carga transportada pelo caminhão
        int cargaAtual = caminhaoPequeno.getCargaAtual();

        // 4) Calcula tempo de deslocamento levando em conta se está carregado e se é horário de pico
        TempoDetalhado tempoDetalhado = TempoUtil.calcularTempoDetalhado(tempoAtual, cargaAtual, true);

        // 5) Exibe no terminal as informações da transferência
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

        // 🔁 ANIMAÇÃO (se estiver no modo gráfico)
        if (simulador.ui.SimuladorFXController.isInstanciado()) {
            var controller = simulador.ui.SimuladorFXController.getInstancia();
            String nomeEstacao = estacaoDestino.getNomeEstacao(); // "A" ou "B"
            String nomeCompleto = "Estação " + nomeEstacao;

            controller.animarTransferencia(
                    caminhaoPequeno.getId(),
                    nomeCompleto,
                    caminhaoPequeno.getCargaAtual(),
                    caminhaoPequeno.getCapacidadeMaxima(),
                    tempoDetalhado.tempoTotal
            );
        }

        // 6) Agenda o evento de chegada à estação
        AgendaEventos.adicionarEvento(
                new EventoEstacaoTransferencia(
                        tempoAtual + tempoDetalhado.tempoTotal,
                        estacaoDestino,
                        caminhaoPequeno
                )
        );
    }
}
