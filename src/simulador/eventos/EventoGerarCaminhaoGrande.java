package simulador.eventos;

import simulador.estacoes.EstacaoDeTransferencia;


/**
 * Evento responsável por gerar um novo caminhão grande em uma estação de transferência.
 * <p>
 * Este evento é acionado quando os caminhões pequenos atingem o tempo máximo de espera
 * e o caminhão grande atual está indisponível (cheio ou ausente).
 */
public class EventoGerarCaminhaoGrande extends Evento {
    private EstacaoDeTransferencia estacao;

    /**
     * Construtor do evento de geração de caminhão grande.
     *
     * @param tempo    o tempo (em minutos) em que o evento será executado
     * @param estacao  a estação de transferência onde o caminhão será gerado
     */
    public EventoGerarCaminhaoGrande(int tempo, EstacaoDeTransferencia estacao) {
        super(tempo);
        this.estacao = estacao;
    }

    /**
     * Executa a lógica de geração de caminhão grande.
     * <p>
     * Verifica se a estação continua sem caminhão grande disponível e,
     * caso afirmativo, instancia um novo caminhão para atender os caminhões pequenos em fila.
     */
    @Override
    public void executar() {
        // Evita duplicação: só gera se ainda não houver caminhão grande disponível
        if (estacao.temCaminhaoGrandeDisponivel()) return;

        System.out.println("[GERAÇÃO] Tempo máximo de espera atingido. Criando caminhão grande.");
        estacao.gerarNovoCaminhaoGrande(tempo);
    }
}