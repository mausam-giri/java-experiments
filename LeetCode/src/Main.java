import LinkedList.DoublyLinkedList;
import LinkedList.LinkedList;

import java.util.*;
public class Main {
    public static void main(String[] args) {
//        Pascal_Triangle(5);
//        DuplicateChecker_Sort(new int[]{1, 2, 3, 4});

        LinkedList link = new LinkedList();
        link.append(2);
        link.append(5);
        link.append(6);
        link.prepend(4);
        link.deleteNode(6);
        System.out.println(link);
        LinkedList.Node reverse = link.reverseList();
        System.out.println(link.toString(reverse));

        DoublyLinkedList ddl = new DoublyLinkedList();
        ddl.append(5);
        ddl.append(6);
        ddl.append(7);
        System.out.println(ddl);
    }


    public static void Pascal_Triangle(int numRows){
    //        1
    //        1 1
    //        1 2 1
    //        1 3 3 1
    //        1 4 6 4 1

        List<List<Integer>> triangle = new ArrayList<>();
        List<Integer> first_row = new ArrayList<>();
        first_row.add(1);
        triangle.add(first_row);

        for(int i = 1; i < numRows; i ++) {
            List<Integer> prev_row = triangle.get(i - 1);
            List<Integer> curr_row = new ArrayList<>();
            curr_row.add(1);

            for (int j = 1; j < i; j++) {
                curr_row.add(prev_row.get(j - 1) + prev_row.get(j));
            }
            curr_row.add(1);
            triangle.add(curr_row);
        }

        StringJoiner outer = new StringJoiner("\n");
        for(List<Integer> first: triangle){
            StringJoiner inner = new StringJoiner(" ", "[", "]");
            for(Integer second: first) {
                inner.add(String.valueOf(second));
            }
            outer.add(inner.toString());
        }
        System.out.println(outer.toString());
    }

    public static boolean DuplicateChecker_Sort(int[] nums){
        Arrays.sort(nums);
        for(int i=0; i < nums.length - 1 ; i ++) if(nums[i] == nums[i+1]) return true;
        return false;
    }

    public static boolean DuplicateChecker_HashSet(int[] nums){
        HashSet<Integer> number = new HashSet<>();
        for (int i = 0; i < nums.length - 1; i++) if(number.contains(nums[i])) return true;
        return false;
    }

}