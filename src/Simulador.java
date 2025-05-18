import simulador.caminhoes.CaminhaoPequeno;
import simulador.estacoes.EstacaoDeTransferencia;
import simulador.eventos.AgendaEventos;
import simulador.eventos.EventoColeta;
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
        EstacaoDeTransferencia estA = new EstacaoDeTransferencia("Estação A");
        EstacaoDeTransferencia estB = new EstacaoDeTransferencia("Estação B");

        // 2. Diz ao mapeador quem atende cada conjunto de zonas
        MapeadorZonas.configurar(estA, estB);

        Zona leste = Zonas.zonaLeste();
        Zona norte = Zonas.zonaNorte();

        System.out.println(ConsoleCor.AMARELO + "=================== S I M U L A D O R ==================");

        System.out.println("Iniciando simulação de coleta de lixo em Teresina");
        leste.gerarLixoDiario();
        norte.gerarLixoDiario();
        System.out.println();

        CaminhaoPequeno c1 = new CaminhaoPequeno("1", 5, 2, leste);
        CaminhaoPequeno c2 = new CaminhaoPequeno("2", 5, 2, leste);
        CaminhaoPequeno c3 = new CaminhaoPequeno("3", 5, 2, leste);
        CaminhaoPequeno c4 = new CaminhaoPequeno("4", 5, 2, leste);
        CaminhaoPequeno c5 = new CaminhaoPequeno("5", 5, 2, leste);

        AgendaEventos.adicionarEvento(new EventoColeta(0, c5, c5.getZonaAlvo()));
        AgendaEventos.adicionarEvento(new EventoColeta(0, c4, c4.getZonaAlvo()));
        AgendaEventos.adicionarEvento(new EventoColeta(0, c3, c3.getZonaAlvo()));
        AgendaEventos.adicionarEvento(new EventoColeta(0, c2, c2.getZonaAlvo()));
        AgendaEventos.adicionarEvento(new EventoColeta(0, c1, c1.getZonaAlvo()));

        AgendaEventos.processarEventos();

        System.out.println();
        int tempoFinal = AgendaEventos.getTempoUltimoEvento();

        System.out.println();
        System.out.println(ConsoleCor.RESET + "===========================================================");
        System.out.println("Simulação finalizada com sucesso!");
        System.out.println("Tempo total: " + TempoUtil.formatarDuracao(tempoFinal) + " (encerra às " + TempoUtil.formatarHorarioSimulado(tempoFinal) + ")");
        System.out.println("[LIXO FINAL] " + leste.getLixoAcumulado() + "T");
        System.out.println("===========================================================");

        System.out.println("Último evento processado: " + AgendaEventos.getUltimoEventoExecutado());

    }
}