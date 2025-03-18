package edu.icev.structures.list;

public class No<TYPE> {
    private TYPE value;
    private No next;

    public No(TYPE newValue) {
        this.value = newValue;
    }

    public TYPE getValue() {
        return value;
    }

    public void setValue(TYPE value) {
        this.value = value;
    }

    public No<TYPE> getNext() {
        return next;
    }

    public void setNext(No<TYPE> next) {
        this.next = next;
    }
}
