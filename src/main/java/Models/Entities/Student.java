package Models.Entities;

import javafx.beans.binding.BooleanExpression;

public class Student {
    private int studentId;
    private String name;
    private String lastName;
    private String patronymic;
    private String phone;
    private String address;

    public Student(int studentId, String name, String lastName, String patronymic, String phone, String address) {
        this.studentId = studentId;
        this.name = name;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.phone = phone;
        this.address = address;
    }

    // Геттеры и сеттеры
    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getPatronymic() { return patronymic; }
    public void setPatronymic(String patronymic) { this.patronymic = patronymic; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    @Override
    public String toString() {
        return "Student{id=" + studentId + ", name='" + name + "', lastName='" + lastName + "'}";
    }


}
