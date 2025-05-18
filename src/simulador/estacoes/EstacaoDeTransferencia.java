package simulador.estacoes;

import java.util.LinkedList;

import simulador.caminhoes.CaminhaoPequeno;

public class EstacaoDeTransferencia {
    public String nomeEstacao;
    private static LinkedList<CaminhaoPequeno> filaCaminhoes = new LinkedList<>();
    private static int cargaAcumulada = 0;

    public EstacaoDeTransferencia(String nomeEstacao) {
        this.nomeEstacao = nomeEstacao;
    }

    public String getNomeEstacao() {
        return nomeEstacao;
    }

    // Método para receber caminhão pequeno e adicioná-lo à fila
    public void receberCaminhaoPequeno(CaminhaoPequeno caminhao, int tempoAtual) {
        filaCaminhoes.add(caminhao);
        System.out.printf("[ESTAÇÃO %s] Caminhão pequeno %s chegou e foi adicionado à fila. Tamanho da fila: %d%n",
                          nomeEstacao, caminhao.getId(), filaCaminhoes.size());
    }

    // Método para ver a fila atual (só para debug)
    public LinkedList<CaminhaoPequeno> getFilaCaminhoes() {
        return filaCaminhoes;
    }

    public static void receberCarga(CaminhaoPequeno caminhao, int carga) {
        cargaAcumulada += carga;
        filaCaminhoes.add(caminhao);

        System.out.println("Estação recebeu " + carga + " toneladas. Total acumulado: " + cargaAcumulada + " toneladas.");
    }
}