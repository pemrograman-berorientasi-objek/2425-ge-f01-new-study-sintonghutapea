package pbo.model;

import javax.persistence.*;
import java.util.*;

@Entity
public class Course {
    @Id
    private String code;

    private String name;
    private int semester;
    private int credit;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<Enrollment> enrollments = new ArrayList<>();

    public Course() {}

    public Course(String code, String name, int semester, int credit) {
        this.code = code;
        this.name = name;
        this.semester = semester;
        this.credit = credit;
    }

    public String getCode() { return code; }
    public String getName() { return name; }
    public int getSemester() { return semester; }
    public int getCredit() { return credit; }

    @Override
    public String toString() {
        return code + "|" + name + "|" + semester + "|" + credit;
    }

    public static void addCourse(EntityManager em, String code, String name, int semester, int credit) {
        em.getTransaction().begin();
        if (em.find(Course.class, code) == null) {
            em.persist(new Course(code, name, semester, credit));
        }
        em.getTransaction().commit();
    }

    public static void showAllCourses(EntityManager em) {
        List<Course> courses = em.createQuery("SELECT c FROM Course c ORDER BY c.semester, c.code", Course.class).getResultList();
        for (Course c : courses) System.out.println(c);
    }
}
