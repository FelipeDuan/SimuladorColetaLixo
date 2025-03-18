package edu.icev.structures.list;

public class No {
    private String value;
    private No next;

    public No(String newValue) {
        this.value = newValue;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public No getNext() {
        return next;
    }

    public void setNext(No next) {
        this.next = next;
    }
}
