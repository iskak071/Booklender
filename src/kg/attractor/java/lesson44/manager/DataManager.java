package kg.attractor.java.lesson44.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import kg.attractor.java.lesson44.models.Book;
import kg.attractor.java.lesson44.models.DataWrapper;
import kg.attractor.java.lesson44.models.Employee;
import kg.attractor.java.lesson44.models.IssueRecord;

import java.io.FileReader;
import java.io.IOException;
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

    public List<Book> getAllBooks() {
        return data.getBooks();
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
}
