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

    CaminhaoGrande[] caminhoesGrandes;
    CaminhaoPequeno[] caminhaoPequenos;
    EstacaoDeTransferencia[] estacaoDeTransferencias;

    float tempoSimulacao;
    boolean rodando;

    public Simulador() {
        this.tempoSimulacao = tempoSimulacao;
        this.rodando = false;
    }

    public void iniciarSimulacao() {
        Zona leste = Zonas.zonaLeste();
        System.out.println("===========================================================");
        System.out.println("Iniciando simulação de coleta de lixo em Teresina");
        leste.gerarLixoDiario();
        System.out.println();

        CaminhaoPequeno c1 = new CaminhaoPequeno("1", 4, 2);

        AgendaEventos.adicionarEvento(new EventoColeta(0, c1, leste));

        AgendaEventos.processarEventos();

        System.out.println();
        int tempoFinal = AgendaEventos.getTempoUltimoEvento();


        System.out.println();
            System.out.println("===========================================================");
        System.out.println("Simulação finalizada com sucesso!");
        System.out.println("Tempo total: " + TempoUtil.formatarDuracao(tempoFinal) + " (encerra às " + TempoUtil.formatarHorarioSimulado(tempoFinal) + ")");
        System.out.println("[LIXO FINAL] " + leste.getLixoAcumulado()+"T");
            System.out.println("===========================================================");
    }

    public void continuar() {

    }

    public void interromper() {

    }

    public void gerarEstatisticas() {

    }
}
