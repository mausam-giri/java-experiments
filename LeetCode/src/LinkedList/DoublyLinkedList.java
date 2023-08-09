package LinkedList;

import java.util.StringJoiner;

public class DoublyLinkedList {
    Node head;
    public final int size = 0;
    public class Node{

        int data;
        Node prev;
        Node next;
        public Node(int data) {
            this.data = data;
            this.prev = null;
            this.next = null;
        };
    }

    public void append(int data){
        Node temp = new Node(data);
        if(head == null){
            head = temp;
            return;
        }

        Node current = head;
        while(current.next != null){
            current = current.next;
        }
        current.next = temp;
        temp.prev = current;
    }

    public String toString(){
        Node current = head;
        StringJoiner str = new StringJoiner(" -> ");
        while(current != null) {
            str.add(String.valueOf(current.data));
            current = current.next;
        }
        str.add("null");
        return str.toString();
    }
    public String toString(Node list){return "";}
}
