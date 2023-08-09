package LinkedList;
import java.util.StringJoiner;

public class LinkedList {
    Node head;
    public class Node {
        int data;
        Node next;

        public Node(int data) {
            this.data = data;
        }
    }
    public void append(int data){
        if(head == null){
            head = new Node(data);
            return;
        }
        Node current = head;
        while(current.next != null){
            current = current.next;
        }
        current.next = new Node(data);
    }

    public void prepend(int data){
        if(head == null) return;
        Node newHead = new Node(data);
        newHead.next = head;
        head = newHead;
    }

    public void deleteNode(int data){
        if(head == null) return;
        if(head.data == data) {
            head = head.next;
            return;
        }
        Node current = head;
        while(current.next != null){
           if(current.next.data == data) {
               current.next = current.next.next;
               return;
           }
           current = current.next;
        }
    }

    public Node reverseList(){
        if(head == null) return null;
        Node current = head;
        Node prev = null;
        while(current != null){
            Node next = current.next;
            current.next = prev;
            prev = current;
            current = next;
        }
        return prev;
    }

    public String toString(){
        Node current = head;
//        StringBuilder str = new StringBuilder();
        StringJoiner str = new StringJoiner(" -> ");
        while(current.next != null){
            str.add(String.valueOf(current.data));
            current = current.next;
        }
        str.add(String.valueOf(current.data));
        str.add("null");
        return str.toString();
    }
    public String toString(Node list){
        Node current = list;
//        StringBuilder str = new StringBuilder();
        StringJoiner str = new StringJoiner(" -> ");
        while(current.next != null){
            str.add(String.valueOf(current.data));
            current = current.next;
        }
        str.add(String.valueOf(current.data));
        str.add("null");
        return str.toString();
    }
}
