package simulador.zona;

import simulador.estacoes.EstacaoDeTransferencia;

public class MapeadorZonas {
    private static EstacaoDeTransferencia estacaoA;
    private static EstacaoDeTransferencia estacaoB;

    /**
     * Defina aqui quem atende Leste/Norte/Centro (A) e Sul/Sudeste (B)
     */
    public static void configurar(EstacaoDeTransferencia a,
                                  EstacaoDeTransferencia b) {
        estacaoA = a;
        estacaoB = b;
    }

    public static EstacaoDeTransferencia getEstacaoPara(Zona zona) {
        String nome = zona.getNome().toLowerCase();
        if (nome.equals("leste") || nome.equals("norte") || nome.equals("centro")) {
            return estacaoA;
        }
        if (nome.equals("sul") || nome.equals("sudeste")) {
            return estacaoB;
        }
        throw new IllegalArgumentException("Zona desconhecida: " + zona.getNome());
    }
}

