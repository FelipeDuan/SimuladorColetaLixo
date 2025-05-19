// Classe utilitária para avaliar a prioridade de zonas com base em critérios definidos
package simulador.zona;

import estruturas.lista.Lista;
import simulador.caminhoes.CaminhaoPequeno;

public class AvaliadorDePrioridade {

    /**
     * Retorna a zona com maior prioridade para coleta de lixo.
     * A prioridade é baseada na maior relação entre lixo acumulado e número de caminhões ativos.
     *
     * @param zonas Lista de zonas disponíveis
     * @param caminhoesAtivos Lista de caminhões em operação
     * @return Zona com maior prioridade
     */
    public static Zona calcularZonaPrioritaria(Lista<Zona> zonas, Lista<CaminhaoPequeno> caminhoesAtivos) {
        Zona zonaMaisPrioritaria = null;
        double maiorPrioridade = -1;

        for (int i = 0; i < zonas.getTamanho(); i++) {
            Zona zona = zonas.getValor(i);
            int lixo = zona.getLixoAcumulado();
            int caminhoesNaZona = contarCaminhoesNaZona(zona, caminhoesAtivos);

            // Evita divisão por zero
            double prioridade = caminhoesNaZona == 0 ? lixo : (double) lixo / caminhoesNaZona;

            if (prioridade > maiorPrioridade) {
                maiorPrioridade = prioridade;
                zonaMaisPrioritaria = zona;
            }
        }

        return zonaMaisPrioritaria;
    }

    /**
     * Conta quantos caminhões estão atualmente atribuídos a uma zona específica.
     *
     * @param zona Zona a ser verificada
     * @param caminhoes Lista de caminhões ativos
     * @return número de caminhões atribuídos a essa zona
     */
    private static int contarCaminhoesNaZona(Zona zona, Lista<CaminhaoPequeno> caminhoes) {
        int contador = 0;
        for (int i = 0; i < caminhoes.getTamanho(); i++) {
            CaminhaoPequeno caminhao = caminhoes.getValor(i);
            if (caminhao.getZonaAlvo().equals(zona) && caminhao.podeRealizarNovaViagem()) {
                contador++;
            }
        }
        return contador;
    }
}
