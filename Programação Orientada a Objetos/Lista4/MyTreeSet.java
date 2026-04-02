import java.util.Scanner;
import java.util.TreeSet;

public class MyTreeSet {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        TreeSet<String> students = new TreeSet<>();

        int studentsAmount = scanner.nextInt();
        for (int i = 0; i < studentsAmount; i++) {
            String student = scanner.next();
            students.add(student);
        }

        System.out.print(students);
        scanner.close();
    }
}