package edu.icev.structures.stack;

import edu.icev.structures.list.LinkedList;

public class Stack<TYPE> {
    private LinkedList<TYPE> list;

    public Stack(){
        this.list = new LinkedList<TYPE>();
    }

    public void push(TYPE newValue){
        this.list.addStart( newValue);
    }

    public TYPE pop(){
            if (this.list.getSize()==0){
                throw new IllegalStateException("A pilha está vazia");
            }
            TYPE topValue = this.peek();
            this.list.remove(topValue);
            return topValue;
    }

    public TYPE peek(){
        if (this.list.getSize() == 0){
            throw new IllegalStateException("A pilha está vazia");
        }
        return this.list.getFirst().getValue();
    }

    public boolean isEmpty(){
        return this.list.getSize() ==0;
    }

    public int size(){
        return this.list.getSize();
    }
}
