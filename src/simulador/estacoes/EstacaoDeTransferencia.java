package simulador.estacoes;

import java.util.LinkedList;
import simulador.caminhoes.CaminhaoPequeno;

public class EstacaoDeTransferencia {
    public String nomeEstacao;
    private static LinkedList<CaminhaoPequeno> filaCaminhoes = new LinkedList<>();
    private static int cargaAcumulada = 0;

    public EstacaoDeTransferencia(String nomeEstacao) {
        nomeEstacao = nomeEstacao;
    }

    public void receberCaminhaoPequeno(CaminhaoPequeno caminhao, int tempoAtual) {

    }

    public static void receberCarga(CaminhaoPequeno caminhao, int carga) {
        cargaAcumulada += carga;
        filaCaminhoes.add(caminhao);

        System.out.println("Estação recebeu " + carga + " toneladas. Total acumulado: " + cargaAcumulada + " toneladas.");
    }
}