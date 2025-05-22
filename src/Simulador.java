import estruturas.lista.Lista;
import simulador.caminhoes.CaminhaoPequeno;
import simulador.configuracao.ParametrosSimulacao;
import simulador.estacoes.EstacaoDeTransferencia;
import simulador.eventos.AgendaEventos;
import simulador.eventos.EventoDistribuidorDeRotas;
import simulador.util.ConsoleCor;
import simulador.util.TempoUtil;
import simulador.zona.MapeadorZonas;
import simulador.zona.Zona;
import simulador.zona.Zonas;

/**
 * Classe responsável por iniciar e coordenar a simulação de coleta de lixo.
 * <p>
 * A simulação é baseada em eventos e utiliza zonas, caminhões pequenos e estações de transferência.
 * Caminhões são criados com destinos definidos e os eventos iniciais são agendados e processados em ordem cronológica.
 */
public class Simulador {

    /**
     * Inicia a simulação de coleta de lixo, realizando a configuração
     * inicial de zonas, estações, caminhões e eventos.
     */
    public void iniciarSimulacao() {

        int dias = ParametrosSimulacao.DIAS_DE_SIMULACAO;
        int quantidade2t = ParametrosSimulacao.QTD_CAMINHOES_2T;
        int quantidade4t = ParametrosSimulacao.QTD_CAMINHOES_4T;
        int quantidade8t = ParametrosSimulacao.QTD_CAMINHOES_8T;
        int viagens2t = ParametrosSimulacao.VIAGENS_2T;
        int viagens4t = ParametrosSimulacao.VIAGENS_4T;
        int viagens8t = ParametrosSimulacao.VIAGENS_8T;

        Lista<Zona> zonas = inicializarZonas();

        // 1. Cria as estações (fixas)
        EstacaoDeTransferencia estA = new EstacaoDeTransferencia("A");
        EstacaoDeTransferencia estB = new EstacaoDeTransferencia("B");
        MapeadorZonas.configurar(estA, estB);
        MapeadorZonas.setZonas(zonas);

        for (int dia = 1; dia <= dias; dia++) {
            System.out.println();
            System.out.println(ConsoleCor.AMARELO + "=================== DIA " + dia + " ====================");
            System.out.println(ConsoleCor.RESET);

            // 2. Gera lixo nas zonas
            System.out.println("Gerando lixo nas zonas...");
            for (int i = 0; i < zonas.getTamanho(); i++) {
                zonas.getValor(i).gerarLixoDiario();
            }

            // 3. Distribui caminhões
            Lista<CaminhaoPequeno> caminhoes2t = EventoDistribuidorDeRotas.distribuir(zonas, quantidade2t, viagens2t, 2);
            Lista<CaminhaoPequeno> caminhoes4t = EventoDistribuidorDeRotas.distribuir(zonas, quantidade4t, viagens4t, 4);
            Lista<CaminhaoPequeno> caminhoes8t = EventoDistribuidorDeRotas.distribuir(zonas, quantidade8t, viagens8t, 8);

            MapeadorZonas.setCaminhoes(caminhoes2t);
            MapeadorZonas.setCaminhoes(caminhoes4t);
            MapeadorZonas.setCaminhoes(caminhoes8t);

            System.out.println("Iniciando coleta...\n");

            // 4. Processa os eventos do dia
            AgendaEventos.processarEventos();

            // 5. Exibe resumo do dia
            int tempoFinal = AgendaEventos.getTempoUltimoEvento();
            System.out.println();
            System.out.println(ConsoleCor.RESET + "=========== FIM DO DIA " + dia + " ===========");
            System.out.println("Tempo total: " + TempoUtil.formatarDuracao(tempoFinal) + " (encerra às " + TempoUtil.formatarHorarioSimulado(tempoFinal) + ")");
            System.out.println("[LIXO ACUMULADO POR ZONA]");
            for (int i = 0; i < zonas.getTamanho(); i++) {
                Zona zona = zonas.getValor(i);
                System.out.println("• " + zona.getNome() + ": " + zona.getLixoAcumulado() + "T");
            }

            System.out.println("[RESUMO DOS CAMINHÕES]");
            System.out.printf("• Caminhões de 2t: %d%n", caminhoes2t.getTamanho());
            System.out.printf("• Caminhões de 4t: %d%n", caminhoes4t.getTamanho());
            System.out.printf("• Caminhões de 8t: %d%n", caminhoes8t.getTamanho());
            System.out.println("Último evento processado: " + AgendaEventos.getUltimoEventoExecutado());

            // Limpa eventos para o próximo dia
            AgendaEventos.resetar();
        }

        System.out.println();
        System.out.println(ConsoleCor.AMARELO + "=============== SIMULAÇÃO ENCERRADA ===============");
        System.out.println(ConsoleCor.RESET);
    }

    /**
     * Cria e retorna a lista de zonas da cidade utilizadas na simulação.
     * <p>
     * Cada zona é instanciada com os seus parâmetros de geração de lixo definidos
     * em {@link simulador.configuracao.ParametrosSimulacao}.
     *
     * @return Lista contendo as zonas: Sul, Sudeste, Centro, Leste e Norte.
     */
    public Lista<Zona> inicializarZonas() {
        Lista<Zona> zonas = new Lista<>();
        zonas.adicionar(0, Zonas.zonaSul());
        zonas.adicionar(1, Zonas.zonaSudeste());
        zonas.adicionar(2, Zonas.zonaCentro());
        zonas.adicionar(3, Zonas.zonaLeste());
        zonas.adicionar(4, Zonas.zonaNorte());
        return zonas;
    }
}