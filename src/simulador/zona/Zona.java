package simulador.zona;

import java.util.Random;

public class Zona {
    private String nome;
    private int lixoMinimo;
    private int lixoMaximo;
    private int lixoAcomulado;

    public Zona(String nome, int lixoMinimo, int lixoMaximo) {
        this.nome = nome;
        this.lixoMinimo = lixoMinimo;
        this.lixoMaximo = lixoMaximo;
        this.lixoAcomulado = 0;
    }

    public void gerarLixoDiario() {
        this.lixoAcomulado = new Random().nextInt(lixoMaximo - lixoMinimo + 1) + lixoMinimo;
        System.out.println("[Zona] " + nome + " gerou " + lixoAcomulado + " toneladas de lixo.");
    }


    public int coletarLixo(int quantidade) {
        int coletado = Math.min(quantidade, lixoAcomulado);
        lixoAcomulado -= coletado;
        return coletado;
    }

    public boolean temLixoRestante() {
        return lixoAcomulado > 0;
    }

    public int getLixoAcumulado() {
        return lixoAcomulado;
    }

    public boolean estaLimpa() {
        return lixoAcomulado == 0;
    }

    public boolean precisaDeColeta(int limiteMinimo) {
        return lixoAcomulado >= limiteMinimo;
    }

    public String getNome() {
        return nome;
    }

}