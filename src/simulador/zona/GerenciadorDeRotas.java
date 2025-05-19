package simulador.zona;

import estruturas.lista.Lista;
import simulador.caminhoes.CaminhaoPequeno;
import simulador.eventos.AgendaEventos;
import simulador.eventos.EventoColeta;
import simulador.util.ConsoleCor;
import simulador.util.TempoUtil;

/**
 * Classe responsável por redirecionar dinamicamente os caminhões
 * para zonas prioritárias com base no acúmulo de lixo e quantidade de caminhões ativos.
 */
public class GerenciadorDeRotas {

    /**
     * Avalia se há necessidade de redirecionar o caminhão para uma nova zona
     * com maior prioridade e agenda nova coleta, se aplicável.
     *
     * @param caminhao Caminhão a ser redirecionado
     * @param tempoAtual Tempo atual da simulação
     */
    public static void redirecionarSeNecessario(CaminhaoPequeno caminhao, int tempoAtual) {
        Zona zonaAtual = caminhao.getZonaAlvo();

        // Se ainda há lixo na zona atual, não redireciona
        if (zonaAtual.getLixoAcumulado() > 0) return;

        Zona novaZona = AvaliadorDePrioridade.calcularZonaPrioritaria(
                MapeadorZonas.getTodasZonas(),
                MapeadorZonas.getCaminhoesAtivos()
        );

        if (novaZona != null && !novaZona.equals(zonaAtual)) {
            System.out.println(ConsoleCor.CIANO + "[GERENCIADOR DE ROTAS] Caminhão " + caminhao.getId()
                    + " redirecionado para zona " + novaZona.getNome());

            caminhao.definirZonaAlvo(novaZona);

            AgendaEventos.adicionarEvento(
                    new EventoColeta(tempoAtual, caminhao, novaZona)
            );
        }
    }

}
