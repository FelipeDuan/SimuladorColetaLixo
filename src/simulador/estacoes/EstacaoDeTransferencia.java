package simulador.estacoes;

import java.util.LinkedList;

import simulador.caminhoes.CaminhaoGrande;
import simulador.caminhoes.CaminhaoPequeno;

public class EstacaoDeTransferencia {
    public String nomeEstacao;
    private static LinkedList<CaminhaoPequeno> filaCaminhoes = new LinkedList<>();
    private CaminhaoGrande caminhaoGrandeAtual;

    public EstacaoDeTransferencia(String nomeEstacao) {
        this.nomeEstacao = nomeEstacao;
        this.caminhaoGrandeAtual = new CaminhaoGrande();
    }

    public void gerarNovoCaminhaoGrande() {
        this.caminhaoGrandeAtual = new CaminhaoGrande();
        System.out.println("[ESTAÇÃO " + nomeEstacao + "] Novo caminhão grande criado.");
    }

    public String getNomeEstacao() {
        return nomeEstacao;
    }

    public LinkedList<CaminhaoPequeno> getFilaCaminhoes() {
        return filaCaminhoes;
    }

    public void receberCaminhaoPequeno(CaminhaoPequeno caminhao, int tempoAtual) {
        System.out.printf("[ESTAÇÃO %s] Caminhão pequeno %s chegou.%n", nomeEstacao, caminhao.getId());

        if (caminhaoGrandeAtual == null || caminhaoGrandeAtual.estaCheio()) {
            // Sem caminhão grande disponível para descarregar
            filaCaminhoes.add(caminhao);
            System.out.printf("Fila de espera aumentou. Tamanho: %d%n", filaCaminhoes.size());
        } else {
            // Descarrega carga no caminhão grande
            int carga = caminhao.getCargaAtual();
            caminhaoGrandeAtual.receberCarga(carga);

            System.out.printf("[ESTAÇÃO %s] Caminhão pequeno %s descarregou %d toneladas no caminhão grande.%n", nomeEstacao,caminhao.getId(), carga);

            if (caminhaoGrandeAtual.estaCheio()) {
                System.out.println("Caminhão grande cheio! Indo para o aterro...");
                caminhaoGrandeAtual.descarregar();

                // Após descarregar, gera um novo caminhão grande para continuar o processo
                gerarNovoCaminhaoGrande();

                // Aqui você pode checar fila e tentar descarregar caminhões pequenos que esperavam
                descarregarFilaEspera(tempoAtual);
            }
        }
    }

    private void descarregarFilaEspera(int tempoAtual) {
        while (!filaCaminhoes.isEmpty() && !caminhaoGrandeAtual.estaCheio()) {
            CaminhaoPequeno caminhaoFila = filaCaminhoes.poll();
            int carga = caminhaoFila.getCargaAtual();
            caminhaoGrandeAtual.receberCarga(carga);
            System.out.printf("[ESTAÇÃO %s] Caminhão pequeno %s da fila descarregou %d toneladas.%n", nomeEstacao, caminhaoFila.getId(), carga);
        }
    }
}