package estruturas.lista;

public class No<T> {
    private T valor;
    private No<T> prox;
    private No<T> prev;

    public No(T valor) {
        this.valor = valor;
        this.prox = null;
        this.prev = null;
    }

    // Getters e Setters
    public T getValor() {
        return valor;
    }

    public No<T> getProx() {
        return prox;
    }

    public void setProx(No<T> prox) {
        this.prox = prox;
    }

    public No<T> getPrev() {
        return prev;
    }

    public void setPrev(No<T> prev) {
        this.prev = prev;
    }
}