package simulador.eventos;

import estruturas.lista.Lista;
import simulador.caminhoes.CaminhaoPequeno;
import simulador.eventos.AgendaEventos;
import simulador.eventos.EventoColeta;
import simulador.zona.Zona;

/**
 * Responsável por distribuir zonas entre caminhões pequenos de forma balanceada
 * e agendar os primeiros eventos de coleta para cada um deles.
 */
public class EventoDistribuidorDeRotas {
    /**
     * Cria caminhões pequenos com rotas balanceadas e agenda os primeiros eventos de coleta.
     *
     * @param zonas lista de zonas disponíveis
     * @param quantidadeCaminhoes total de caminhões a serem criados
     * @param viagensPorCaminhao número de viagens que cada caminhão realizará
     * @return lista com todos os caminhões criados
     */
    public static Lista<CaminhaoPequeno> distribuir(Lista<Zona> zonas, int quantidadeCaminhoes, int viagensPorCaminhao, int capacidadeCaminhao) {
        Lista<CaminhaoPequeno> caminhoes = new Lista<>();
        int quantidadeZonas = zonas.getTamanho();

        for (int i = 0; i < quantidadeCaminhoes; i++) {
            Lista<Zona> rotaCaminhao = new Lista<>();

            // Distribui zonas para cada viagem do caminhão
            for (int j = 0; j < viagensPorCaminhao; j++) {
                Zona zonaAlvo = zonas.getValor((i + j) % quantidadeZonas);
                rotaCaminhao.adicionar(j, zonaAlvo);
            }

            String id = "C" + (i + 1);
            int capacidade = capacidadeCaminhao ; // podemos parametrizar isso futuramente

            CaminhaoPequeno caminhao = new CaminhaoPequeno(id, capacidade, viagensPorCaminhao, rotaCaminhao);
            caminhoes.adicionar(i, caminhao);

            // Agenda o primeiro evento de coleta
            AgendaEventos.adicionarEvento(new EventoColeta(0, caminhao, caminhao.getZonaAlvo()));
        }

        return caminhoes;
    }
}
