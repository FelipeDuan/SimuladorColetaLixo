package simulador.zona;

import estruturas.lista.Lista;
import simulador.caminhoes.CaminhaoPequeno;
import simulador.estacoes.EstacaoDeTransferencia;

/**
 * Classe utilitária responsável por mapear zonas da cidade às estações de transferência.
 * <p>
 * Atua como um roteador central que define qual estação deve atender determinada zona,
 * permitindo que a simulação envie os caminhões para a estação correta.
 */
public class MapeadorZonas {

    private static EstacaoDeTransferencia estacaoA;
    private static EstacaoDeTransferencia estacaoB;
    private static Lista<Zona> zonas;
    private static Lista<CaminhaoPequeno> caminhoes;

    /**
     * Define a configuração de quais zonas cada estação atende.
     * <p>
     * A convenção utilizada é:
     * <ul>
     *   <li><b>Estação A</b>: Leste, Norte, Centro</li>
     *   <li><b>Estação B</b>: Sul, Sudeste</li>
     * </ul>
     *
     * @param a referência da Estação A
     * @param b referência da Estação B
     */
    public static void configurar(EstacaoDeTransferencia a, EstacaoDeTransferencia b) {
        estacaoA = a;
        estacaoB = b;
    }

    /**
     * Retorna a estação de transferência responsável por uma determinada zona.
     *
     * @param zona a zona que deseja obter a estação correspondente
     * @return a estação que atende a zona informada
     * @throws IllegalArgumentException se a zona não for reconhecida
     */
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

    /**
     * Salva a lista de zonas da simulação.
     * Deve ser chamada no início da simulação.
     *
     * @param listaZonas lista de zonas usadas na simulação
     */
    public static void setZonas(Lista<Zona> listaZonas) {
        zonas = listaZonas;
    }

    /**
     * Salva a lista de caminhões da simulação.
     * Deve ser chamada no início da simulação.
     *
     * @param listaCaminhoes lista de caminhões usados na simulação
     */
    public static void setCaminhoes(Lista<CaminhaoPequeno> listaCaminhoes) {
        caminhoes = listaCaminhoes;
    }

    /**
     * Retorna a lista completa de zonas utilizadas na simulação.
     */
    public static Lista<Zona> getTodasZonas() {
        return zonas;
    }

    /**
     * Retorna os caminhões que ainda têm viagens disponíveis.
     */
    public static Lista<CaminhaoPequeno> getCaminhoesAtivos() {
        Lista<CaminhaoPequeno> ativos = new Lista<>();

        for (int i = 0; i < caminhoes.getTamanho(); i++) {
            CaminhaoPequeno caminhao = caminhoes.getValor(i);
            if (caminhao.podeRealizarNovaViagem()) {
                ativos.adicionar(ativos.getTamanho(), caminhao);
            }
        }

        return ativos;
    }
}

