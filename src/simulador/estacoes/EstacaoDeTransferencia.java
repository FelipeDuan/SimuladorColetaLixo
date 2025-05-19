package simulador.estacoes;

import estruturas.lista.Fila;
import simulador.caminhoes.CaminhaoGrande;
import simulador.caminhoes.CaminhaoPequeno;
import simulador.configuracao.ParametrosSimulacao;
import simulador.eventos.AgendaEventos;
import simulador.eventos.EventoColeta;
import simulador.eventos.EventoGerarCaminhaoGrande;
import simulador.util.ConsoleCor;
import simulador.util.TempoUtil;
import simulador.zona.GerenciadorDeRotas;
import simulador.zona.MapeadorZonas;

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

    /**
     * Verifica se existe um caminhão grande disponível e com capacidade para receber carga.
     *
     * @return {@code true} se houver caminhão grande e ele não estiver cheio
     */
    public boolean temCaminhaoGrandeDisponivel() {
        return caminhaoGrandeAtual != null && !caminhaoGrandeAtual.estaCheio();
    }

    /**
     * Lida com a chegada de um caminhão pequeno na estação.
     * <p>
     * Se houver caminhão grande disponível, realiza o descarregamento. Caso contrário, adiciona à fila
     * e agenda um evento futuro para gerar novo caminhão grande se necessário.
     *
     * @param caminhao o caminhão pequeno que chegou
     * @param tempoAtual tempo atual da simulação (em minutos)
     */
    public void receberCaminhaoPequeno(CaminhaoPequeno caminhao, int tempoAtual) {
        System.out.println(ConsoleCor.ROXO + "====================== E S T A Ç Ã O ======================");
        System.out.printf("[%s]%n", TempoUtil.formatarHorarioSimulado(tempoAtual));
        System.out.printf("[Estação %s | Caminhão %s]%n", nomeEstacao, caminhao.getId());
        System.out.println("  → Chegada confirmada.");

        // Caso não haja caminhão grande ou ele esteja cheio
        if (caminhaoGrandeAtual == null || caminhaoGrandeAtual.estaCheio()) {
            filaCaminhoes.enqueue(caminhao);
            System.out.printf("  • Fila de espera aumentou. Tamanho: %d%n", filaCaminhoes.size());

            // Agenda evento de criação de caminhão grande se ainda não estiver agendado
            if (caminhao.getEventoAgendado() == null) {
                int tempoLimite = tempoAtual + 100;
                EventoGerarCaminhaoGrande evento = new EventoGerarCaminhaoGrande(tempoLimite, this);
                AgendaEventos.adicionarEvento(evento);
                caminhao.setEventoAgendado(evento);
                System.out.printf("  • Evento para gerar caminhão grande agendado para %s%n", TempoUtil.formatarHorarioSimulado(tempoLimite));
            }
        } else {
            // Caso consiga descarregar direto
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

            caminhao.registrarViagem();

            if (caminhao.podeRealizarNovaViagem()) {
                // ⚠️ Só avança se a zona ainda tiver lixo
                if (!caminhao.getZonaAlvo().estaLimpa()) {
                    caminhao.atualizarZonaAlvo();
                    int proximoHorario = tempoAtual + tempoDescarga;
                    AgendaEventos.adicionarEvento(new EventoColeta(proximoHorario, caminhao, caminhao.getZonaAlvo()));
                } else {
                    // 🚚 Redirecionamento para zona prioritária
                    GerenciadorDeRotas.redirecionarSeNecessario(caminhao, tempoAtual);
                }
            } else {
                System.out.printf("[CAMINHÃO %s] Finalizou suas atividades do dia.%n", caminhao.getId());
            }


            if (caminhaoGrandeAtual.estaCheio()) {
                System.out.println("  • Caminhão grande cheio!");
                System.out.println();
                System.out.printf("[COLETA] Caminhão Grande %s → Aterro%n", caminhaoGrandeAtual.getId());
                caminhaoGrandeAtual.descarregar();
                descarregarFilaEspera(tempoAtual);
            }
        }
    }

    /**
     * Tenta descarregar todos os caminhões da fila de espera no caminhão grande atual.
     *
     * @param tempoAtual tempo atual da simulação (em minutos)
     */
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