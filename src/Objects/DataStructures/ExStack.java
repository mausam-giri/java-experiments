package Objects.DataStructures;

import java.util.Arrays;

public class ExStack<T>{
    private Object[] arr;
    private T val;
    private int size;
    private int top = -1;
    public ExStack(int size){
        this.size = size;
        this.arr = new Object[size];
    }
    public void push(T val){
        if(isFull()) throw new IllegalCallerException("Stack is Full");
        this.arr[++top]  = val;
    }
    public T pop(){
        if(isEmpty()) throw new IllegalCallerException("Stack is Empty");
        return (T)this.arr[top--];
    }
    public T peek(){
        if(isEmpty()) throw  new IllegalCallerException("Stack is Empty");
        return (T)this.arr[top];
    }
    public int search(T o){
        int i;
        for(i = top; i >= 0; i--)
            if(o.equals(this.arr[i]))
                return i;
        return -1;
    }
    public boolean isEmpty(){
        return this.top == -1;
    }
    public boolean isFull(){
        return this.top >= size;
    }

    @Override
    public String toString() {
        String data = Arrays.toString(Arrays.copyOfRange(arr, 0, top + 1));
        return "ExStack {" +
                "arr=" + data +
                ", val=" + val +
                ", size=" + size +
                ", top=" + top +
                '}';
    }
}
