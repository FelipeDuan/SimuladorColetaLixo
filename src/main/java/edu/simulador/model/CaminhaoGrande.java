package edu.simulador.model;

/*
* - Capacidade fixa de 20 toneladas
- Tem tolerância de Espera
    - Se for excedida, parte para o aterro sanitário
    - Se estiver vazio, aguarda até ser carregado
* */

public class CaminhaoGrande {
    public int id;
    public int numeroViagens;
    public boolean emViagem;
    public float capacidade;
    public float cargaAtual;
    public boolean carregando;
    public float tempoEsperaAtual;
    public float tempoMaximoEspera;

    public CaminhaoGrande(){

    }

    public void receberCarga(int quantidade){

    }

    public void descarregarAterro(){
        // vai até o aterro
        /*
        * - Tem tolerância de Espera
            - Se for excedida, parte para o aterro sanitário
            - Se estiver vazio, aguarda até ser carregado
        */
    }

    public boolean aguardarCarregamento(){
        // esperando o carregamento
        return true;
    }

}
