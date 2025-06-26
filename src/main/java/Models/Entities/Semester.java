package Models.Entities;

public class Semester {
    private int semesterId;
    private int year;
    private int semesterNumber;

    public Semester(int semesterId, int year, int semesterNumber) {
        this.semesterId = semesterId;
        this.year = year;
        this.semesterNumber = semesterNumber;
    }

    public int getSemesterId() { return semesterId; }
    public void setSemesterId(int semesterId) { this.semesterId = semesterId; }
    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }
    public int getSemesterNumber() { return semesterNumber; }
    public void setSemesterNumber(int semesterNumber) { this.semesterNumber = semesterNumber; }

    @Override
    public String toString() {
        return "Semester{id=" + semesterId + ", year=" + year + ", number=" + semesterNumber + "}";
    }
}
