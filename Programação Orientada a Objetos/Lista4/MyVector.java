import java.util.Collections;
import java.util.Scanner;
import java.util.Vector;

public class MyVector {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Vector<String> students = new Vector<>();

        int studentsAmount = scanner.nextInt();
        for (int i = 0; i < studentsAmount; i++) {
            String student = scanner.next();
            students.add(student);
        }
        Collections.sort(students);

        System.out.print(students);
        scanner.close();
    }
}