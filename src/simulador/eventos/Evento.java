package simulador.eventos;

public abstract class Evento implements Comparable<Evento> {
    protected int tempo;

    public Evento(int tempo) {
        this.tempo = tempo;
    }

    public int getTempo() {
        return tempo;
    }

    public abstract void executar();

    @Override
    public int compareTo(Evento outro) {
        return Integer.compare(this.tempo, outro.tempo);
    }
}