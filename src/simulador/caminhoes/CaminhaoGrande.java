package caminhoes;/*
* - Capacidade fixa de 20 toneladas
- Tem tolerância de Espera
    - Se for excedida, parte para o aterro sanitário
    - Se estiver vazio, aguarda até ser carregado
* */

public class CaminhaoGrande {
    public int id;
    public int numeroViagens;
    public boolean emViagem;
    protected int capacidadeMaxima = 20000;
    protected int cargaAtual;
    public boolean carregando;

    public CaminhaoGrande() {
        this.cargaAtual = 0;
    }

    public boolean prontoParaPartir() {
        return cargaAtual >= capacidadeMaxima;
    }

    public void receberCarga(int quantidade) {
        cargaAtual += quantidade;
    }

    public void descarregar() {
        System.out.println("Caminhão grande partiu para o aterro com " + cargaAtual + "kg.");
        cargaAtual = 0;
        // vai até o aterro
        /*
         * - Tem tolerância de Espera
         * - Se for excedida, parte para o aterro sanitário
         * - Se estiver vazio, aguarda até ser carregado
         */
    }

    public boolean aguardarCarregamento() {
        // esperando o carregamento
        return true;
    }

}
