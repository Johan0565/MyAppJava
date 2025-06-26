package myappjava;

import Models.*;
import Models.Entities.*;

public class MainTest {
    public static void main(String[] args) {
        Students students = Students.getInstance();
        System.out.println("Students:");
        for (Student student : students.getAll()) {
            System.out.println(student);
        }

        Users users = Users.getInstance();
        System.out.println("\nUsers:");
        for (User user : users.getAll()) {
            System.out.println(user);
        }

        Roles roles = Roles.getInstance();
        System.out.println("\nRoles:");
        for (Post role : roles.getAll()) {
            System.out.println(role);
        }

        ElectivePlans electivePlans = ElectivePlans.getInstance();
        System.out.println("\nElectivePlans:");
        for (ElectivePlan plan : electivePlans.getAll()) {
            System.out.println(plan);
        }

        Electives electives = Electives.getInstance();
        System.out.println("\nElectives:");
        for (Elective elective : electives.getAll()) {
            System.out.println(elective);
        }

        Semesters semesters = Semesters.getInstance();
        System.out.println("\nSemesters:");
        for (Semester semester : semesters.getAll()) {
            System.out.println(semester);
        }

        Enrollments enrollments = Enrollments.getInstance();
        System.out.println("\nEnrollments:");
        for (Enrollment enrollment : enrollments.getAll()) {
            System.out.println(enrollment);
        }

        Departments departments = Departments.getInstance();
        System.out.println("\nDepartments:");
        for (Department department : departments.getAll()) {
            System.out.println(department);
        }
    }
}