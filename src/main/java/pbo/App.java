package pbo;

import pbo.model.Student;
import pbo.model.Course;
import pbo.model.Enrollment;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            String input = scanner.nextLine();
            
            if (input.startsWith("student-add")) {
                String[] parts = input.split("#");
                if (parts.length == 4) {
                    String nim = parts[1];
                    String name = parts[2];
                    String major = parts[3];
                    addStudent(nim, name, major);
                }
            } else if (input.equals("student-show-all")) {
                showAllStudents();
            } else if (input.startsWith("course-add")) {
                String[] parts = input.split("#");
                if (parts.length == 5) {
                    String courseCode = parts[1];
                    String courseName = parts[2];
                    String semester = parts[3];
                    int credits = Integer.parseInt(parts[4]);
                    addCourse(courseCode, courseName, semester, credits);
                }
            } else if (input.equals("course-show-all")) {
                showAllCourses();
            } else if (input.startsWith("enroll")) {
                String[] parts = input.split("#");
                if (parts.length == 3) {
                    String nim = parts[1];
                    String courseCode = parts[2];
                    enrollStudent(nim, courseCode);
                }
            } else if (input.startsWith("student-detail")) {
                String[] parts = input.split("#");
                if (parts.length == 2) {
                    String nim = parts[1];
                    showStudentDetail(nim);
                }
            } else if (input.equals("---")) {
                break;
            }
        }
        scanner.close();
    }

    private static void addStudent(String nim, String name, String major) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("KrsPU");
        EntityManager em = emf.createEntityManager();

        Student student = new Student();
        student.setNim(nim);
        student.setName(name);
        student.setMajor(major);

        try {
            em.getTransaction().begin();
            em.persist(student);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
            emf.close();
        }
    }

    private static void showAllStudents() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("KrsPU");
        EntityManager em = emf.createEntityManager();

        String queryStr = "SELECT s FROM Student s ORDER BY s.nim ASC";
        TypedQuery<Student> query = em.createQuery(queryStr, Student.class);

        try {
            List<Student> studentList = query.getResultList();
            for (Student s : studentList) {
                System.out.println(s.getNim() + "|" + s.getName() + "|" + s.getMajor());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
            emf.close();
        }
    }

    private static void addCourse(String courseCode, String courseName, String semester, int credits) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("KrsPU");
        EntityManager em = emf.createEntityManager();

        Course course = new Course();
        course.setCourseCode(courseCode);
        course.setCourseName(courseName);
        course.setSemester(semester);
        course.setCredits(credits);

        try {
            em.getTransaction().begin();
            em.persist(course);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
            emf.close();
        }
    }

    private static void showAllCourses() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("KrsPU");
        EntityManager em = emf.createEntityManager();

        String queryStr = "SELECT c FROM Course c ORDER BY c.semester ASC, c.courseCode ASC";
        TypedQuery<Course> query = em.createQuery(queryStr, Course.class);

        try {
            List<Course> courseList = query.getResultList();
            for (Course c : courseList) {
                System.out.println(c.getCourseCode() + "|" + c.getCourseName() + "|" + c.getSemester() + "|" + c.getCredits());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
            emf.close();
        }
    }

    private static void enrollStudent(String nim, String courseCode) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("KrsPU");
        EntityManager em = emf.createEntityManager();

        Student student = em.find(Student.class, nim);
        Course course = em.find(Course.class, courseCode);

        if (student != null && course != null) {
            String queryStr = "SELECT e FROM Enrollment e WHERE e.student = :student AND e.course = :course";
            long count = em.createQuery(queryStr, Enrollment.class)
                           .setParameter("student", student)
                           .setParameter("course", course)
                           .getResultList().size();

            if (count == 0) {
                Enrollment enrollment = new Enrollment();
                enrollment.setStudent(student);
                enrollment.setCourse(course);

                try {
                    em.getTransaction().begin();
                    em.persist(enrollment);
                    em.getTransaction().commit();
                } catch (Exception e) {
                    if (em.getTransaction().isActive()) {
                        em.getTransaction().rollback();
                    }
                    e.printStackTrace();
                }
            } else {
                System.out.println("Mahasiswa sudah terdaftar pada mata kuliah ini.");
            }
        } else {
            System.out.println("Mahasiswa atau mata kuliah tidak ditemukan.");
        }

        em.close();
        emf.close();
    }

    private static void showStudentDetail(String nim) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("KrsPU");
        EntityManager em = emf.createEntityManager();

        Student student = em.find(Student.class, nim);
        if (student != null) {
            System.out.println(student.getNim() + "|" + student.getName() + "|" + student.getMajor());

            String queryStr = "SELECT e FROM Enrollment e WHERE e.student = :student ORDER BY e.course.semester ASC, e.course.code ASC";
            TypedQuery<Enrollment> query = em.createQuery(queryStr, Enrollment.class);
            query.setParameter("student", student);

            List<Enrollment> enrollments = query.getResultList();
            for (Enrollment enrollment : enrollments) {
                Course course = enrollment.getCourse();
                System.out.println(course.getCourseCode() + "|" + course.getCourseName() + "|" + course.getSemester() + "|" + course.getCredits());
            }
        } else {
            System.out.println("Student with NIM " + nim + " not found.");
        }

        em.close();
        emf.close();
    }
}
