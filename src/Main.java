// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
import Objects.DataStructures.ExStack;
import Objects.DataStructures.MStack;
import Objects.ShapeObject.Shape;
import Objects.ShapeObject.ShapeType;
import Objects.ShapeObject.Shapes.Circle;
import Objects.ShapeObject.Shapes.Rectangle;
import Objects.ShapeObject.Shapes.Square;
import Objects.ShapeObject.Shapes.Triangle;
import String.LString;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.print("Enter the first string: ");
        String first = input.nextLine();
        System.out.print("Enter the second string: ");
        String second = input.nextLine();

        LString string = new LString();
        System.out.println(string.getLongestSub(first, second));

    }


    public static void ExtendedStack(){
        ExStack<Integer> stack = new ExStack<Integer>(10);

        System.out.println(stack.isEmpty());
        stack.push(5);
        stack.push(6);
        stack.push(7);

        System.out.println(stack.peek());
        System.out.println(stack);
        System.out.println(stack.pop());
        System.out.println(stack.search(7));
        System.out.println(stack.isFull());

    }
    public static void mStack(){
        MStack stack = new MStack(10);
        stack.push(1);
        stack.push(2);
        stack.push(3);
        stack.push(4);

        System.out.println("Current no of element: " + stack.getSize());
        stack.view();

        System.out.println("Stack peek: " + stack.peek());
        System.out.println("Element at location 2: " + stack.seek(2));

        stack.pop();
        stack.view();

    }
    public void shapeArea(){
        Shape s;
        ShapeType st;

        double length = 5, breadth = 6;
        st = ShapeType.SQUARE;

        switch(st){
            case SQUARE:
                s = new Square(length);
                break;
            case RECTANGLE:
                s = new Rectangle(length, breadth);
                break;
            case TRIANGLE:
                s = new Triangle(length, breadth);
                break;
            case CIRCLE:
                s = new Circle(length);
                break;
            default:
                s = null;
                break;
        }

        System.out.println(s.getArea());
    }
}