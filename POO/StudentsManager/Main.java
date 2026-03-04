import models.Student;

public class Main {
    public static void main(String[] args) {
        Student[] students = new Student[100];
        MenuHandler menu = new MenuHandler();

        int totalStudents = 0;
        int maxStudents = 100;

        menu.start(students, totalStudents, maxStudents);
    }
}