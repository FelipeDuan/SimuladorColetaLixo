package simulador.estacoes;

import estruturas.lista.Fila;
import simulador.caminhoes.CaminhaoGrande;
import simulador.caminhoes.CaminhaoPequeno;
import simulador.configuracao.ParametrosSimulacao;
import simulador.eventos.AgendaEventos;
import simulador.eventos.EventoGerarCaminhaoGrande;
import simulador.util.ConsoleCor;
import simulador.util.TempoUtil;

/**
 * Representa uma estação de transferência de lixo.
 * <p>
 * Recebe caminhões pequenos carregados, armazena os caminhões em fila se necessário,
 * e gerencia o carregamento do caminhão grande responsável por levar o lixo ao aterro.
 */
public class EstacaoDeTransferencia {
    public String nomeEstacao;
    private CaminhaoGrande caminhaoGrandeAtual;
    private Fila<CaminhaoPequeno> filaCaminhoes = new Fila<>();

    /**
     * Construtor da estação de transferência.
     *
     * @param nomeEstacao o nome identificador da estação
     */
    public EstacaoDeTransferencia(String nomeEstacao) {
        this.nomeEstacao = nomeEstacao;
        this.caminhaoGrandeAtual = new CaminhaoGrande();
    }

    /**
     * Gera um novo caminhão grande e tenta descarregar a fila de espera.
     *
     * @param tempoAtual tempo atual da simulação (em minutos)
     */
    public void gerarNovoCaminhaoGrande(int tempoAtual) {
        this.caminhaoGrandeAtual = new CaminhaoGrande();
        System.out.println("[ESTAÇÃO " + nomeEstacao + "] Novo caminhão grande criado.");
        descarregarFilaEspera(tempoAtual);
    }

    /**
     * Retorna o nome da estação de transferência.
     *
     * @return nome da estação
     */
    public String getNomeEstacao() {
        return nomeEstacao;
    }

    /**
     * (Não utilizado)
     * <p>
     * Retorna a fila de caminhões pequenos que aguardam descarregamento.
     *
     * @return fila de espera de caminhões pequenos
     */
    public Fila<CaminhaoPequeno> getFilaCaminhoes() {
        return filaCaminhoes;
    }

    public boolean temCaminhaoGrandeDisponivel() {
        return caminhaoGrandeAtual != null && !caminhaoGrandeAtual.estaCheio();
    }

    public void receberCaminhaoPequeno(CaminhaoPequeno caminhao, int tempoAtual) {
        System.out.println(ConsoleCor.ROXO + "====================== E S T A Ç Ã O ======================");
        System.out.printf("[%s]%n", TempoUtil.formatarHorarioSimulado(tempoAtual));
        System.out.printf("[%s | Caminhão %s]%n", nomeEstacao, caminhao.getId());
        System.out.println("  → Chegada confirmada.");

        if (caminhaoGrandeAtual == null || caminhaoGrandeAtual.estaCheio()) {
            filaCaminhoes.enqueue(caminhao);
            System.out.printf("  • Fila de espera aumentou. Tamanho: %d%n", filaCaminhoes.size());

            if (caminhao.getEventoAgendado() == null) {
                int tempoLimite = tempoAtual + 100; // 30 minutos de espera
                EventoGerarCaminhaoGrande evento = new EventoGerarCaminhaoGrande(tempoLimite, this);
                AgendaEventos.adicionarEvento(evento);
                caminhao.setEventoAgendado(evento);
                System.out.printf("  • Evento para gerar caminhão grande agendado para %s%n", TempoUtil.formatarDuracao(tempoLimite));
            }
        } else {
            if (caminhao.getEventoAgendado() != null) {
                AgendaEventos.removerEvento(caminhao.getEventoAgendado());
                caminhao.setEventoAgendado(null);
                System.out.println("  • Evento anterior para geração de caminhão grande cancelado.");
            }

            int carga = caminhao.getCargaAtual();
            int tempoDescarga = carga * ParametrosSimulacao.TEMPO_DESCARGA_POR_TONELADA;

            caminhaoGrandeAtual.receberCarga(carga);
            caminhao.descarregar();

            System.out.printf("  • Descarregou: %dt    Carga: %d/%d%n", carga, caminhao.getCargaAtual(), caminhao.getCapacidadeMaxima());
            System.out.printf("  • Horário: %s     Tempo de Descarga: %s%n", TempoUtil.formatarHorarioSimulado(tempoDescarga + tempoAtual), TempoUtil.formatarDuracao(tempoDescarga));
            System.out.printf("  → Volta para atividadades %n", TempoUtil.formatarHorarioSimulado(tempoDescarga + tempoAtual));
            // Adicionar metodo aqui

            if (caminhaoGrandeAtual.estaCheio()) {
                System.out.println("  • Caminhão grande cheio!");
                System.out.println();
                System.out.printf("[COLETA] Caminhão Grande %s → Aterro%n", caminhaoGrandeAtual.getId());
                caminhaoGrandeAtual.descarregar();

                descarregarFilaEspera(tempoAtual);
            }
        }
    }

    private void descarregarFilaEspera(int tempoAtual) {
        while (!filaCaminhoes.isEmpty() && !caminhaoGrandeAtual.estaCheio()) {
            CaminhaoPequeno caminhaoFila = filaCaminhoes.poll();

            if (caminhaoFila.getEventoAgendado() != null) {
                AgendaEventos.removerEvento(caminhaoFila.getEventoAgendado());
                caminhaoFila.setEventoAgendado(null);
            }

            int carga = caminhaoFila.getCargaAtual();
            caminhaoGrandeAtual.receberCarga(carga);
            System.out.printf("[ESTAÇÃO %s] Caminhão pequeno %s da fila descarregou %d toneladas.%n", nomeEstacao, caminhaoFila.getId(), carga);
        }
    }
}