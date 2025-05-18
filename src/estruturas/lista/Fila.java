package estruturas.lista;

/**
 * Implementação de uma fila genérica (FIFO) com base em nós encadeados.
 *
 * @param <T> o tipo de elementos armazenados na fila
 */
public class Fila<T> {
    private static class No<T> {
        T valor;
        No<T> prox;

        No(T valor) {
            this.valor = valor;
            this.prox = null;
        }
    }

    private No<T> head, tail;
    private int tamanho;

    public Fila() {
        head = null;
        tail = null;
        tamanho = 0;
    }

    public boolean enqueue(T valor) {
        No<T> novo = new No<>(valor);
        if (tail == null) {
            head = novo;
            tail = novo;
        } else {
            tail.prox = novo;
            tail = novo;
        }
        tamanho++;
        return true;
    }

    public T dequeue() {
        if (head == null) {
            System.out.println("Fila vazia");
            return null;
        }
        T valor = head.valor;
        head = head.prox;
        if (head == null) {
            tail = null;
        }
        tamanho--;
        return valor;
    }

    public T poll() {
        if (head == null) {
            return null;
        }
        T valor = head.valor;
        head = head.prox;
        if (head == null) {
            tail = null;
        }
        tamanho--;
        return valor;
    }

    public boolean isEmpty() {
        return head == null;
    }

    public void printQueue() {
        if (head == null) {
            System.out.println("Fila vazia");
            return;
        }
        No<T> atual = head;
        while (atual != null) {
            System.out.print(atual.valor + " -> ");
            atual = atual.prox;
        }
        System.out.println("[EXIT]");
    }

    public int size() {
        return tamanho;
    }
}
