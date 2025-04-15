import simulador.caminhoes.CaminhaoGrande;
import simulador.caminhoes.CaminhaoPequeno;
import simulador.configuracao.ParametrosSimulacao;
import simulador.estacoes.EstacaoDeTransferencia;
import simulador.eventos.AgendaEventos;
import simulador.eventos.EventoColeta;
import simulador.util.TempoUtil;

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
        System.out.println("Iniciando simulação de coleta de lixo em Teresina...");

        CaminhaoPequeno caminhao1 = new CaminhaoPequeno("1", 8, 3); // id 1, capacidade 4 ton, 3 viagens

        AgendaEventos.adicionarEvento(new EventoColeta(0, caminhao1));

        AgendaEventos.processarEventos();

        System.out.println();
        int tempoFinal = AgendaEventos.getTempoUltimoEvento();
        System.out.println("[Tempo Final] Simulação encerrada às " + TempoUtil.converterMinutoParaHora(tempoFinal));


        System.out.println();
        System.out.println("============================================");
        System.out.println("Simulação finalizada com sucesso!");
        System.out.println("Tempo total de simulação: " + TempoUtil.converterMinutoParaHora(AgendaEventos.getTempoUltimoEvento()));
        System.out.println("Caminhões rodaram, lixo coletado, missão cumprida!");
        System.out.println("============================================");
    }

    public void continuar() {

    }

    public void interromper() {

    }

    public void gerarEstatisticas() {

    }
}
