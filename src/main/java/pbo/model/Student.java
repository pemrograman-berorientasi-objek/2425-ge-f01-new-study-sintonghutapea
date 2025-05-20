package pbo.model;

import javax.persistence.*;
import java.util.*;

@Entity
public class Student {
    @Id
    private String nim;

    private String name;
    private String program;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<Enrollment> enrollments = new ArrayList<>();

    public Student() {}

    public Student(String nim, String name, String program) {
        this.nim = nim;
        this.name = name;
        this.program = program;
    }

    public String getNim() { return nim; }
    public String getName() { return name; }
    public String getProgram() { return program; }

    @Override
    public String toString() {
        return nim + "|" + name + "|" + program;
    }

    public List<Enrollment> getEnrollments() {
        return enrollments;
    }

    public static void addStudent(EntityManager em, String nim, String name, String program) {
        em.getTransaction().begin();
        if (em.find(Student.class, nim) == null) {
            em.persist(new Student(nim, name, program));
        }
        em.getTransaction().commit();
    }

    public static void showAllStudents(EntityManager em) {
        List<Student> students = em.createQuery("SELECT s FROM Student s ORDER BY s.nim", Student.class).getResultList();
        for (Student s : students) System.out.println(s);
    }

    public static void showStudentDetail(EntityManager em, String nim) {
        Student student = em.find(Student.class, nim);
        if (student != null) {
            System.out.println(student);
            List<Enrollment> enrollments = em.createQuery(
                "SELECT e FROM Enrollment e WHERE e.student.nim = :nim ORDER BY e.course.semester, e.course.code", Enrollment.class)
                .setParameter("nim", nim).getResultList();
            for (Enrollment e : enrollments) {
                System.out.println(e.getCourse());
            }
        }
    }
}
