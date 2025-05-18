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

    public EventoGerarCaminhaoGrande(int tempo, EstacaoDeTransferencia estacao) {
        super(tempo);
        this.estacao = estacao;
    }

    @Override
    public void executar() {

        // Garante que não gere duplicado
        if (estacao.temCaminhaoGrandeDisponivel()) return;

        System.out.println("[GERAÇÃO] Tempo máximo de espera atingido. Criando caminhão grande.");
        estacao.gerarNovoCaminhaoGrande(tempo);
    }
}