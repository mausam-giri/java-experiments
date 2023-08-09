package Objects.DataStructures;

public class MStack {
    final int size;
    int top;
    Integer[] arr;

    public MStack(int size){
        this.size = size;
        this.arr = new Integer[size];
        this.top = -1;
    }
    public int pop(){
        if(isEmpty()) return 0;
        return this.arr[top--];
    }
    public int peek(){
        if(isEmpty()) return 0;
        return this.arr[top];
    }
    public int push(int data){
        if(isFull()) return 0;
        return this.arr[++top] = data;
    }
    public int seek(int location){
        if(isEmpty()) return 0;
        return this.arr[top - (location - 1)];
    }

    public boolean isEmpty(){
        return top == -1;
    }
    public boolean isFull(){
        return top > this.arr.length;
    }
    public int getSize(){
        return top + 1;
    }
    public void view(){
       if (isEmpty()) System.out.println("No element in Stack");
       else {
           System.out.print("[ ");
           for (int i = top; i >= 0; i--) {
               System.out.print(this.arr[i] + " ");
           }
           System.out.print("]\n");
       }
    }
}
