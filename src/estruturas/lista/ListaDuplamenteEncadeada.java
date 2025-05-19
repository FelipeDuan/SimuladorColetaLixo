package estruturas.lista;

/**
     * Implementação de uma lista duplamente encadeada
     */
    public class ListaDuplamenteEncadeada<T> {
        private No<T> primeiro;
        private No<T> ultimo;
        private int tamanho;

        public ListaDuplamenteEncadeada() {
            this.primeiro = null;
            this.ultimo = null;
            this.tamanho = 0;
        }

        public void adicionar(T valor) {
            No<T> novoNo = new No<>(valor);

            if (estaVazia()) {
                primeiro = novoNo;
                ultimo = novoNo;
            } else {
                ultimo.setProx(novoNo);
                novoNo.setPrev(ultimo);
                ultimo = novoNo;
            }

            tamanho++;
        }

        public void adicionarInicio(T valor) {
            No<T> novoNo = new No<>(valor);

            if (estaVazia()) {
                primeiro = novoNo;
                ultimo = novoNo;
            } else {
                novoNo.setProx(primeiro);
                primeiro.setPrev(novoNo);
                primeiro = novoNo;
            }

            tamanho++;
        }

        public T remover(int indice) {
            if (estaVazia() || indice < 0 || indice >= tamanho) {
                return null;
            }

            No<T> noAtual;

            if (indice == 0) {
                // Remoção do primeiro elemento
                noAtual = primeiro;
                primeiro = primeiro.getProx();

                if (primeiro != null) {
                    primeiro.setPrev(null);
                } else {
                    ultimo = null;
                }
            } else if (indice == tamanho - 1) {
                // Remoção do último elemento
                noAtual = ultimo;
                ultimo = ultimo.getPrev();
                ultimo.setProx(null);
            } else {
                // Remoção de um elemento intermediário
                noAtual = primeiro;
                for (int i = 0; i < indice; i++) {
                    noAtual = noAtual.getProx();
                }

                noAtual.getPrev().setProx(noAtual.getProx());
                noAtual.getProx().setPrev(noAtual.getPrev());
            }

            tamanho--;
            return noAtual.getValor();
        }

        public T removerInicio() {
            return remover(0);
        }

        public T removerFim() {
            return remover(tamanho - 1);
        }

        public T obter(int indice) {
            if (estaVazia() || indice < 0 || indice >= tamanho) {
                return null;
            }

            No<T> noAtual = primeiro;
            for (int i = 0; i < indice; i++) {
                noAtual = noAtual.getProx();
            }

            return noAtual.getValor();
        }

        public boolean estaVazia() {
            return tamanho == 0;
        }

        public int tamanho() {
            return tamanho;
        }

        public No<T> getPrimeiro() {
            return primeiro;
        }

        public No<T> getUltimo() {
            return ultimo;
        }
    }
