import java.io.*;
import java.util.*;

class Record<T> {
    private T data;
    public Record(T data) { this.data = data; }
    public T getData() { return data; }
    public void setData(T data) { this.data = data; }
}

// Student class
class Student {
    private String id;
    private String name;

    public Student(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() { return id; }
    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    @Override
    public String toString() {
        return "ID: " + id + ", Name: " + name;
    }
}

// Interface for actions
interface Manageable {
    void viewStudents();
}

// Base User class
abstract class User implements Manageable {
    protected List<Record<Student>> records;

    public User(List<Record<Student>> records) {
        this.records = records;
    }

    public void viewStudents() {
        System.out.println("\n--- Student List ---");
        for (Record<Student> record : records) {
            System.out.println(record.getData());
        }
    }
}

// Admin class
class Admin extends User {
    public Admin(List<Record<Student>> records) {
        super(records);
    }

    public void addStudent(String id, String name) {
        records.add(new Record<>(new Student(id, name)));
        System.out.println("Student added.");
    }

    public void editStudent(String id, String newName) {
        for (Record<Student> record : records) {
            if (record.getData().getId().equals(id)) {
                record.getData().setName(newName);
                System.out.println("Student updated.");
                return;
            }
        }
        System.out.println("Student not found.");
    }

    public void deleteStudent(String id) {
        records.removeIf(record -> record.getData().getId().equals(id));
        System.out.println("Student deleted if found.");
    }
}

// Teacher class
class Teacher extends User {
    public Teacher(List<Record<Student>> records) {
        super(records);
    }

    public void editStudent(String id, String newName) {
        for (Record<Student> record : records) {
            if (record.getData().getId().equals(id)) {
                record.getData().setName(newName);
                System.out.println("Student updated.");
                return;
            }
        }
        System.out.println("Student not found.");
    }
}

// StudentView class
class StudentView extends User {
    public StudentView(List<Record<Student>> records) {
        super(records);
    }
}

// Main Application
public class kem {
    private static final String FILE_NAME = "students.txt";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Record<Student>> records = loadFromFile();

        while (true) {
            System.out.println("\nEnter UserName: (Choices: Admin, Teacher, Student)");
            String role = scanner.nextLine();

            switch (role) {
                case "Admin":
                    Admin admin = new Admin(records);
                    adminMenu(scanner, admin);
                    break;
                case "Teacher":
                    Teacher teacher = new Teacher(records);
                    teacherMenu(scanner, teacher);
                    break;
                case "Student":
                    StudentView student = new StudentView(records);
                    student.viewStudents();
                    break;
                default:
                    System.out.println("Invalid role.");
            }
        }
    }

    // Admin Menu
    public static void adminMenu(Scanner scanner, Admin admin) {
        while (true) {
            System.out.println("\nAdmin Menu: Add, Edit, View, Delete, Exit");
            String choice = scanner.nextLine();

            switch (choice) {
                case "Add":
                    System.out.print("Enter ID: ");
                    String id = scanner.nextLine();
                    System.out.print("Enter Name: ");
                    String name = scanner.nextLine();
                    admin.addStudent(id, name);
                    saveToFile(admin.records);
                    break;
                case "Edit":
                    System.out.print("Enter ID to edit: ");
                    id = scanner.nextLine();
                    System.out.print("Enter new name: ");
                    name = scanner.nextLine();
                    admin.editStudent(id, name);
                    saveToFile(admin.records);
                    break;
                case "View":
                    admin.viewStudents();
                    break;
                case "Delete":
                    System.out.print("Enter ID to delete: ");
                    id = scanner.nextLine();
                    admin.deleteStudent(id);
                    saveToFile(admin.records);
                    break;
                case "Exit":
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    // Teacher Menu
    public static void teacherMenu(Scanner scanner, Teacher teacher) {
        while (true) {
            System.out.println("\nTeacher Menu: Edit, View, Exit");
            String choice = scanner.nextLine();

            switch (choice) {
                case "Edit":
                    System.out.print("Enter ID to edit: ");
                    String id = scanner.nextLine();
                    System.out.print("Enter new name: ");
                    String name = scanner.nextLine();
                    teacher.editStudent(id, name);
                    saveToFile(teacher.records);
                    break;
                case "View":
                    teacher.viewStudents();
                    break;
                case "Exit":
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    // Save to file
    public static void saveToFile(List<Record<Student>> records) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Record<Student> record : records) {
                Student s = record.getData();
                writer.write(s.getId() + "," + s.getName());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }

    // Load from file
    public static List<Record<Student>> loadFromFile() {
        List<Record<Student>> records = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", 2);
                if (parts.length == 2) {
                    records.add(new Record<>(new Student(parts[0], parts[1])));
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("No existing data found. Starting fresh.");
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return records;
    }
}
