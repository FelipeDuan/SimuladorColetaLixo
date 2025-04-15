package simulador.estacoes;

import java.util.LinkedList;
import simulador.caminhoes.CaminhaoPequeno;

public class EstacaoDeTransferencia {
    private static LinkedList<CaminhaoPequeno> filaCaminhoes = new LinkedList<>();
    private static int cargaAcumulada = 0;

    public static void receberCarga(CaminhaoPequeno caminhao, int carga) {
        cargaAcumulada += carga;
        filaCaminhoes.add(caminhao);

        System.out.println("Estação recebeu " + carga + " toneladas. Total acumulado: " + cargaAcumulada + " toneladas.");

        // Exemplo: quando atinge 20 toneladas, podemos agendar o carregamento do caminhão grande
        if (cargaAcumulada >= 20) {
            System.out.println("Carga suficiente para caminhão grande. Pode-se agendar EventoCarregamentoCaminhaoGrande.");
        }
    }
}
