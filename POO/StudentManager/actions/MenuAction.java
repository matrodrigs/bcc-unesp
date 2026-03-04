package actions;

import models.Student;

public interface MenuAction {
    int execute(Student[] students, int totalStudents);
}