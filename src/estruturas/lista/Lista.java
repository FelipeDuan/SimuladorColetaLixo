package estruturas.lista;

public class Lista<T> {
    private No<T> head;
    private No<T> tail;
    private int tamanho;

    public Lista() {
        head = null;
        tail = null;
        tamanho = 0;
    }

    /**
     * Adiciona um elemento na posição especificada
     * @param pos Posição onde o elemento será inserido (0-based)
     * @param valor Valor a ser inserido
     * @return true se a inserção foi bem-sucedida, false caso contrário
     */
    public boolean adicionar(int pos, T valor) {
        if (pos < 0 || pos > tamanho) {
            return false;
        }

        No<T> novo = new No<>(valor);

        if (tamanho == 0) {  // Lista vazia
            head = novo;
            tail = novo;
        } else if (pos == 0) {  // Inserção no início
            novo.setProx(head);
            head.setPrev(novo);
            head = novo;
        } else if (pos == tamanho) {  // Inserção no final
            novo.setPrev(tail);
            tail.setProx(novo);
            tail = novo;
        } else {  // Inserção no meio
            No<T> atual = getNo(pos - 1);
            novo.setProx(atual.getProx());
            novo.setPrev(atual);
            atual.getProx().setPrev(novo);
            atual.setProx(novo);
        }
        tamanho++;
        return true;
    }

    /**
     * Remove o elemento na posição especificada
     * @param pos Posição do elemento a ser removido (0-based)
     * @return true se a remoção foi bem-sucedida, false caso contrário
     */
    public T removerHead() {
    if (head == null){
        return null;
    }
    T valor = head.getValor();
    head = head.getProx();
    tamanho--;
    return valor;
    }

        public boolean remover(int pos) {
        if (pos < 0 || pos >= tamanho || head == null) {
            return false;
        }

        if (tamanho == 1) {  // Único elemento
            head = null;
            tail = null;
        } else if (pos == 0) {  // Remoção do início
            head = head.getProx();
            head.setPrev(null);
        } else if (pos == tamanho - 1) {  // Remoção do final
            tail = tail.getPrev();
            tail.setProx(null);
        } else {  // Remoção do meio
            No<T> atual = getNo(pos);
            atual.getPrev().setProx(atual.getProx());
            atual.getProx().setPrev(atual.getPrev());
        }
        tamanho--;
        return true;
    }


    /**
     * Obtém o nó na posição especificada
     * @param pos Posição do nó (0-based)
     * @return Nó na posição especificada
     */
    private No<T> getNo(int pos) {
        if (pos < 0 || pos >= tamanho) {
            return null;
        }

        No<T> atual;
        // Decide começar do início ou do fim para melhor performance
        if (pos < tamanho / 2) {
            atual = head;
            for (int i = 0; i < pos; i++) {
                atual = atual.getProx();
            }
        } else {
            atual = tail;
            for (int i = tamanho - 1; i > pos; i--) {
                atual = atual.getPrev();
            }
        }
        return atual;
    }

    /**
     * Imprime todos os elementos da lista do início ao fim
     */
    public void imprimir() {
        No<T> atual = head;
        while (atual != null) {
            System.out.print(atual.getValor() + " ");
            atual = atual.getProx();
        }
        System.out.println("-> NULL");
    }

    /**
     * Imprime todos os elementos da lista do fim ao início
     */
    public void imprimirReverso() {
        No<T> atual = tail;
        while (atual != null) {
            System.out.print(atual.getValor() + " ");
            atual = atual.getPrev();
        }
        System.out.println("-> NULL");
    }

    /**
     * Retorna o tamanho atual da lista
     * @return número de elementos na lista
     */
    public int getTamanho() {
        return tamanho;
    }

    /**
     * Verifica se a lista está vazia
     * @return true se a lista estiver vazia, false caso contrário
     */
    public boolean estaVazia() {
        return tamanho == 0;
    }
}