package pbo.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;

@Entity
@Table(name = "krs")
@IdClass(Enrollment.EnrollmentId.class)  
public class Enrollment {

    @Id
    @ManyToOne
    private Student student;  

    @Id
    @ManyToOne
    private Course course; 

    private String semester;     

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public static class EnrollmentId implements java.io.Serializable {
        private String nim;
        private String courseCode;

        public EnrollmentId() {}

        public EnrollmentId(String nim, String courseCode) {
            this.nim = nim;
            this.courseCode = courseCode;
        }

        public String getNim() {
            return nim;
        }

        public void setNim(String nim) {
            this.nim = nim;
        }

        public String getCourseCode() {
            return courseCode;
        }

        public void setCourseCode(String courseCode) {
            this.courseCode = courseCode;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            EnrollmentId enrollmentId = (EnrollmentId) o;
            return nim.equals(enrollmentId.nim) && courseCode.equals(enrollmentId.courseCode);
        }

        @Override
        public int hashCode() {
            return 31 * nim.hashCode() + courseCode.hashCode();
        }
    }
}
