package pbo;

/*
 * 12S23009 - Sintong Hutapea
 * 12S23038 - Alya Triswani
 */

import javax.persistence.*;
import java.util.Scanner;
import pbo.model.Student;
import pbo.model.Course;
import pbo.model.Enrollment;

public class App {
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("f01");
    private static final EntityManager em = emf.createEntityManager();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String input = scanner.nextLine().trim();
            if (input.equals("---")) break;

            if (input.startsWith("student-add#")) {
                String[] tokens = input.split("#");
                Student.addStudent(em, tokens[1], tokens[2], tokens[3]);
            } else if (input.equals("student-show-all")) {
                Student.showAllStudents(em);
            } else if (input.startsWith("course-add#")) {
                String[] tokens = input.split("#");
                Course.addCourse(em, tokens[1], tokens[2], Integer.parseInt(tokens[3]), Integer.parseInt(tokens[4]));
            } else if (input.equals("course-show-all")) {
                Course.showAllCourses(em);
            } else if (input.startsWith("enroll#")) {
                String[] tokens = input.split("#");
                Enrollment.enrollStudent(em, tokens[1], tokens[2]);
            } else if (input.startsWith("student-show#")) {
                String nim = input.split("#")[1];
                Student.showStudentDetail(em, nim);
            }
        }
        em.close();
        emf.close();
    }
}
