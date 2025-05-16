package simulador.estacoes;

import java.util.LinkedList;
import simulador.caminhoes.CaminhaoPequeno;
import simulador.zona.Zona;

public class EstacaoDeTransferencia {
    private static LinkedList<CaminhaoPequeno> filaCaminhoes = new LinkedList<>();
    private static int cargaAcumulada = 0;


    public static EstacaoDeTransferencia escolherEstacaoFixo(Zona zona, EstacaoDeTransferencia estA, EstacaoDeTransferencia estB) {
    switch (zona.getNome().toLowerCase()) {
        case "sul":
        case "leste":
            return estA;
        case "norte":
        case "centro":
        case "sudeste":
            return estB;
        default:
            // fallback: envia sempre para A
            return estA;
    }
}

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
