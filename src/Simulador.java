import simulador.caminhoes.CaminhaoGrande;
import simulador.caminhoes.CaminhaoPequeno;
import simulador.configuracao.ParametrosSimulacao;
import simulador.estacoes.EstacaoDeTransferencia;
import simulador.eventos.AgendaEventos;
import simulador.eventos.EventoColeta;
import simulador.util.ConsoleCor;
import simulador.util.TempoUtil;
import simulador.zona.MapeadorZonas;
import simulador.zona.Zona;
import simulador.zona.Zonas;

public class Simulador {
    public void iniciarSimulacao() {
        // 1. Cria as 2 estações
        EstacaoDeTransferencia estA = new EstacaoDeTransferencia("Estação A");
        EstacaoDeTransferencia estB = new EstacaoDeTransferencia("Estação B");

        // 2. Diz ao mapeador quem atende cada conjunto de zonas
        MapeadorZonas.configurar(estA, estB);

        Zona leste = Zonas.zonaLeste();
        System.out.println(ConsoleCor.AMARELO + "=================== S I M U L A D O R ==================");

        System.out.println("Iniciando simulação de coleta de lixo em Teresina");
        leste.gerarLixoDiario();
        System.out.println();

        CaminhaoPequeno c1 = new CaminhaoPequeno("1", 4, 2);

        AgendaEventos.adicionarEvento(new EventoColeta(0, c1, leste));

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
