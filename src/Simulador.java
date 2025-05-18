import estruturas.lista.Lista;
import simulador.caminhoes.CaminhaoPequeno;
import simulador.estacoes.EstacaoDeTransferencia;
import simulador.eventos.AgendaEventos;
import simulador.eventos.EventoColeta;
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
        // 1. Cria as 2 estações
        EstacaoDeTransferencia estA = new EstacaoDeTransferencia("A");
        EstacaoDeTransferencia estB = new EstacaoDeTransferencia("B");

        // 2. Diz ao mapeador quem atende cada conjunto de zonas
        MapeadorZonas.configurar(estA, estB);

        // 3. Inicializa zonas e gera lixo diário
        Lista<Zona> zonas = inicializarZonas();

        for (int i = 0; i < zonas.getTamanho(); i++) {
            zonas.getValor(i).gerarLixoDiario();
        }

        // 4. Inicializa caminhões e agenda os eventos iniciais
        Lista<CaminhaoPequeno> caminhoes = EventoDistribuidorDeRotas.distribuir(zonas, 5, 2);

        // 5. Inicia o processamento da simulação
        System.out.println(ConsoleCor.AMARELO + "=================== S I M U L A D O R ==================");
        System.out.println("Iniciando simulação de coleta de lixo em Teresina\n");

        AgendaEventos.processarEventos();

        int tempoFinal = AgendaEventos.getTempoUltimoEvento();

        System.out.println();
        System.out.println(ConsoleCor.RESET + "===========================================================");
        System.out.println("Simulação finalizada com sucesso!");
        System.out.println("Tempo total: " + TempoUtil.formatarDuracao(tempoFinal) + " (encerra às " + TempoUtil.formatarHorarioSimulado(tempoFinal) + ")");
        System.out.println("[LIXO FINAL POR ZONA]");

        // Percorre todas as zonas e exibe o lixo restante em cada uma
        for (int i = 0; i < zonas.getTamanho(); i++) {
            Zona zona = zonas.getValor(i);
            System.out.println("• " + zona.getNome() + ": " + zona.getLixoAcumulado() + "T");
        }

        System.out.println("===========================================================");

        System.out.println("Último evento processado: " + AgendaEventos.getUltimoEventoExecutado());
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