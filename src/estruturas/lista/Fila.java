package estruturas.lista;

/**
 * Implementação de uma fila genérica (FIFO) com base em nós encadeados.
 *
 * @param <T> o tipo de elementos armazenados na fila
 */
public class Fila<T> {

    /**
     * Classe interna que representa um nó da fila.
     * Contém o valor armazenado e uma referência para o próximo nó.
     */
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


    /**
     * Construtor da fila. Inicializa a estrutura vazia.
     */
    public Fila() {
        head = null;
        tail = null;
        tamanho = 0;
    }

    /**
     * Insere um elemento no final da fila.
     *
     * @param valor o valor a ser adicionado
     * @return {@code true} se a inserção for bem-sucedida
     */
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

    /**
     * (Não utilizado)
     *
     * Remove e retorna o elemento no início da fila.
     * Exibe uma mensagem se a fila estiver vazia.
     *
     * @return o valor removido, ou {@code null} se a fila estiver vazia
     */
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

    /**
     * Versão silenciosa do {@link #dequeue()}, sem exibir mensagens.
     *
     * @return o valor removido, ou {@code null} se a fila estiver vazia
     */
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

    /**
     * Verifica se a fila está vazia.
     *
     * @return {@code true} se a fila estiver vazia, {@code false} caso contrário
     */
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
