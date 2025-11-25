package kg.attractor.java.lesson44.models;

import java.util.List;

public class DataWrapper {
    private List<Employee> employees;
    private List<Book> books;
    private List<IssueRecord> issueRecords;

    public DataWrapper() {}

    public List<Employee> getEmployees() {
        return employees;
    }
    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }
    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
    public List<IssueRecord> getIssueRecords() {
        return issueRecords;
    }
    public void setIssueRecords(List<IssueRecord> issueRecords) {
        this.issueRecords = issueRecords;
    }
}
