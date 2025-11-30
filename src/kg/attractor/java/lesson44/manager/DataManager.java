package kg.attractor.java.lesson44.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import kg.attractor.java.lesson44.models.Book;
import kg.attractor.java.lesson44.models.DataWrapper;
import kg.attractor.java.lesson44.models.Employee;
import kg.attractor.java.lesson44.models.IssueRecord;

import java.io.FileReader;
    import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DataManager {
    private static final String DATA_FILE_PATH = "data/data.json";
    private DataWrapper data;

    public DataManager() {
        loadData();
    }

    private void loadData() {
        Gson gson = new GsonBuilder().create();

        try (FileReader reader = new FileReader(DATA_FILE_PATH)) {
            data = gson.fromJson(reader, DataWrapper.class);
            System.out.println("DataManager: Data loaded successfully using Gson.");
        } catch (IOException e) {
            System.out.println("DataManager: ERROR loading data from JSON. Check file path and Gson setup.");
            e.printStackTrace();

            data = new DataWrapper();
            data.setBooks(Collections.emptyList());
            data.setEmployees(Collections.emptyList());
            data.setIssueRecords(Collections.emptyList());
        }
    }

    public void saveData() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try (Writer writer = new FileWriter(DATA_FILE_PATH)) {
            gson.toJson(data, writer);
            System.out.println("DataManager: Data saved successfully!");
        } catch (IOException e) {
            System.out.println("DataManager: ERROR saving data to JSON.");
            e.printStackTrace();
        }
    }

    public Optional<Employee> findEmployeeByEmail(String email) {
        return data.getEmployees().stream()
                .filter(e -> e.getEmail() != null && e.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }

    public boolean addEmployee(Employee employee) {
        if (findEmployeeByEmail(employee.getEmail()).isPresent()) {
            return false;
        }

        int maxID = data.getEmployees().stream()
                .mapToInt(Employee::getId)
                .max()
                .orElse(0);
        employee.setId(maxID + 1);

        data.getEmployees().add(employee);

        saveData();
        return true;
    }



    public List<Book> getAllBooks() {
        return data.getBooks();
    }

    public List<Employee> getAllEmployees() {
        return data.getEmployees();
    }

    public Optional<Book> getBookById(int id) {
        return data.getBooks().stream().filter(b -> b.getId() == id).findFirst();
    }

    public Optional<Employee> getEmployeeById(int id) {
        return data.getEmployees().stream().filter(e -> e.getId() == id).findFirst();
    }

    public Optional<Employee> findCurrentHolder(Book book) {
        Optional<IssueRecord> activeRecord = data.getIssueRecords().stream()
                .filter(r -> r.getBookId() == book.getId() && r.isCurrentlyBorrowed())
                .findFirst();

        return activeRecord.flatMap(record -> getEmployeeById(record.getEmployeeId()));
    }

    public List<IssueRecord> getRecordsForEmployee(int employeeId) {
        return data.getIssueRecords().stream()
                .filter(r -> r.getEmployeeId() == employeeId)
                .collect(Collectors.toList());
    }

    public int getCurrentBooksCount(int employeeId) {
        return (int) data.getIssueRecords().stream()
                .filter(r -> r.getEmployeeId() == employeeId && r.isCurrentlyBorrowed())
                .count();
    }

    public boolean borrowBook(int bookId, int employeeId) {
        Optional<Book> bookOpt = getBookById(bookId);
        if (bookOpt.isEmpty()) {
            return false;
        }

        Optional<Employee> currentHolder = findCurrentHolder(bookOpt.get());
        if (currentHolder.isPresent()) {
            return false;
        }

        if (getCurrentBooksCount(employeeId) >= 2) {
            return false;
        }

        IssueRecord record = new IssueRecord();
        record.setBookId(bookId);
        record.setEmployeeId(employeeId);
        record.setIssueDate(System.currentTimeMillis());
        record.setReturnDate(null);

        data.getIssueRecords().add(record);
        saveData();
        return true;
    }

    public boolean returnBook(int bookId, int employeeId) {
        Optional<IssueRecord> activeRecord = data.getIssueRecords().stream()
                .filter(r -> r.getBookId() == bookId &&
                        r.getEmployeeId() == employeeId &&
                        r.isCurrentlyBorrowed())
                .findFirst();

        if (activeRecord.isEmpty()) {
            return false;
        }

        activeRecord.get().setReturnDate(System.currentTimeMillis());
        saveData();
        return true;
    }
}
