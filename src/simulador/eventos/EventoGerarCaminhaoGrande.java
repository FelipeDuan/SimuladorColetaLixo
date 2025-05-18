package simulador.eventos;

import simulador.estacoes.EstacaoDeTransferencia;

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