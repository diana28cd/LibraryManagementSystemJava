import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Abstract class representing a Person
abstract class Person implements Serializable {
    private String name;
    private int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    @Override
    public String toString() {
        return "Name: " + name + ", Age: " + age;
    }
}

// Student class inheriting from Person
class Student extends Person {
    private String studentId;

    public Student(String name, int age, String studentId) {
        super(name, age);
        this.studentId = studentId;
    }

    public String getStudentId() {
        return studentId;
    }

    @Override
    public String toString() {
        return super.toString() + ", Student ID: " + studentId;
    }
}

// Faculty class representing a Faculty
class Faculty implements Serializable {
    private String facultyName;
    private List<Student> students;

    public Faculty(String facultyName) {
        this.facultyName = facultyName;
        this.students = new ArrayList<>();
    }

    public String getFacultyName() {
        return facultyName;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void addStudent(Student student) {
        students.add(student);
    }

    @Override
    public String toString() {
        return "Faculty: " + facultyName + ", Students: " + students.size();
    }
}

// SaveManager class for data persistence
class SaveManager {
    private static final String FILE_NAME = "StudentManagementSystem.dat";

    public static void saveData(List<Faculty> faculties) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(faculties);
            System.out.println("Data saved successfully!");
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public static List<Faculty> loadData() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            return (List<Faculty>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No previous data found. Starting fresh.");
            return new ArrayList<>();
        }
    }
}

// Main application class
public class StudentManagementSystem {
    private static List<Faculty> faculties = SaveManager.loadData();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean running = true;

        while (running) {
            System.out.println("\n--- TUM Student Management System ---");
            System.out.println("1. Add Faculty");
            System.out.println("2. Add Student");
            System.out.println("3. View Faculties");
            System.out.println("4. Save and Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1 -> addFaculty();
                case 2 -> addStudent();
                case 3 -> viewFaculties();
                case 4 -> {
                    SaveManager.saveData(faculties);
                    running = false;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void addFaculty() {
        System.out.print("Enter faculty name: ");
        String facultyName = scanner.nextLine();
        faculties.add(new Faculty(facultyName));
        System.out.println("Faculty added successfully.");
    }

    private static void addStudent() {
        System.out.print("Enter faculty name: ");
        String facultyName = scanner.nextLine();

        Faculty faculty = faculties.stream()
                .filter(f -> f.getFacultyName().equalsIgnoreCase(facultyName))
                .findFirst()
                .orElse(null);

        if (faculty == null) {
            System.out.println("Faculty not found!");
            return;
        }

        System.out.print("Enter student name: ");
        String name = scanner.nextLine();
        System.out.print("Enter student age: ");
        int age = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter student ID: ");
        String studentId = scanner.nextLine();

        faculty.addStudent(new Student(name, age, studentId));
        System.out.println("Student added successfully.");
    }

    private static void viewFaculties() {
        if (faculties.isEmpty()) {
            System.out.println("No faculties available.");
        } else {
            faculties.forEach(faculty -> {
                System.out.println(faculty);
                faculty.getStudents().forEach(student -> System.out.println("  " + student));
            });
        }
    }
}
