package Models.Entities;

import java.util.Date;

public class Enrollment {
    private int enrollmentsId;
    private int studentId;
    private int electiveId;
    private int semesterId;
    private int grade;
    private Date gradeDate;

    public Enrollment(int enrollmentsId, int studentId, int electiveId, int semesterId, int grade, Date gradeDate) {
        this.enrollmentsId = enrollmentsId;
        this.studentId = studentId;
        this.electiveId = electiveId;
        this.semesterId = semesterId;
        this.grade = grade;
        this.gradeDate = gradeDate;
    }

    // Геттеры и сеттеры
    public int getEnrollmentsId() { return enrollmentsId; }
    public void setEnrollmentsId(int enrollmentsId) { this.enrollmentsId = enrollmentsId; }
    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }
    public int getElectiveId() { return electiveId; }
    public void setElectiveId(int electiveId) { this.electiveId = electiveId; }
    public int getSemesterId() { return semesterId; }
    public void setSemesterId(int semesterId) { this.semesterId = semesterId; }
    public int getGrade() { return grade; }
    public void setGrade(int grade) { this.grade = grade; }
    public Date getGradeDate() { return gradeDate; }
    public void setGradeDate(Date gradeDate) { this.gradeDate = gradeDate; }

    @Override
    public String toString() {
        return "Enrollment{id=" + enrollmentsId + ", studentId=" + studentId + ", electiveId=" + electiveId + ", grade=" + grade + "}";
    }
}
