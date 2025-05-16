import simulador.caminhoes.CaminhaoGrande;
import simulador.caminhoes.CaminhaoPequeno;
import simulador.configuracao.ParametrosSimulacao;
import simulador.estacoes.EstacaoDeTransferencia;
import simulador.eventos.AgendaEventos;
import simulador.eventos.EventoColeta;
import simulador.util.TempoUtil;
import simulador.zona.Zona;
import simulador.zona.Zonas;

public class Simulador {

    EstacaoDeTransferencia estA = new EstacaoDeTransferencia();
    EstacaoDeTransferencia estB = new EstacaoDeTransferencia();

    public void iniciarSimulacao() {

        Zona leste = Zonas.zonaLeste();
        System.out.println("===========  S I M U L A Ç Ã O   I N I C I A D A  ===========");
        System.out.println();
        leste.gerarLixoDiario();
        System.out.println();

        CaminhaoPequeno c1 = new CaminhaoPequeno("1", 4, 2);
        AgendaEventos.adicionarEvento(new EventoColeta(0, c1, leste));

        AgendaEventos.processarEventos();

        System.out.println();
        int tempoFinal = AgendaEventos.getTempoUltimoEvento();

        System.out.println();
        System.out.println("============================================================");
        System.out.println("Simulação finalizada com sucesso!");
        System.out.println("Tempo total: " + TempoUtil.formatarDuracao(tempoFinal) + " (encerra às " + TempoUtil.formatarHorarioSimulado(tempoFinal) + ")");
        System.out.println("[LIXO FINAL] " + leste.getLixoAcumulado()+"T");
        System.out.println("===========================================================");

    }
}