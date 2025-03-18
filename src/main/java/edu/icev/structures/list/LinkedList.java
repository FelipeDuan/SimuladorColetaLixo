package edu.icev.structures.list;

public class LinkedList<TYPE> {
    private No<TYPE> first;
    private No<TYPE> last;
    private int size;

    public LinkedList (){
        this.size = 0;
    }

    public No<TYPE> getFirst() {
        return first;
    }

    public void setFirst(No<TYPE> first) {
        this.first = first;
    }

    public No<TYPE> getLast() {
        return last;
    }

    public void setLast(No<TYPE> last) {
        this.last = last;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void add(TYPE newValue){
        No<TYPE> newNo = new No<TYPE>(newValue);
        if (this.first == null && this.last == null){
            this.first = newNo;
            this.last = newNo;
        }else{
            this.last.setNext(newNo);
            this.last = newNo;
        }
        this.size++;
    }

    public void remove(TYPE searchValue){
        No<TYPE> previous = null;
        No<TYPE> current = this.first;
        for (int i=0; i < this.getSize(); i++){
            if (current.getValue().equals(searchValue)){
                if (this.size == 1) {
                    this.first = null;
                    this.last = null;
                } else if (current == first) {
                    this.first = first.getNext();
                    current.setNext(null);
                } else if (current == last) {
                    this.last = previous;
                    previous.setNext(null);
                } else {
                    previous.setNext(current.getNext());
                    current = null;
                }
                this.size--;
                break;
            }
            previous = current;
            current = current.getNext();
        }
    }

    public No<TYPE> get(int pos){
        No<TYPE>  current = this.first;
        for (int i=0; i <pos; i++){
            if (current.getNext() !=null){
                current = current.getNext();
            }
        }
        return current;
    }
}
