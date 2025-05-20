package pbo.model;

import javax.persistence.*;

@Entity
public class Enrollment {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Student student;

    @ManyToOne
    private Course course;

    public Enrollment() {}

    public Enrollment(Student student, Course course) {
        this.student = student;
        this.course = course;
    }

    public Student getStudent() { return student; }
    public Course getCourse() { return course; }

    public static void enrollStudent(EntityManager em, String nim, String code) {
        em.getTransaction().begin();
        Student student = em.find(Student.class, nim);
        Course course = em.find(Course.class, code);
        if (student != null && course != null) {
            TypedQuery<Enrollment> query = em.createQuery(
                "SELECT e FROM Enrollment e WHERE e.student.nim = :nim AND e.course.code = :code", Enrollment.class);
            query.setParameter("nim", nim);
            query.setParameter("code", code);
            if (query.getResultList().isEmpty()) {
                em.persist(new Enrollment(student, course));
            }
        }
        em.getTransaction().commit();
    }
}
