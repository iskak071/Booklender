package kg.attractor.java.lesson44.models;

public class EmployeeRecord {
    private Book book;
    private long issueDate;
    private Long returnDate;

    public EmployeeRecord(Book book, IssueRecord record) {
        this.book = book;
        this.issueDate = record.getIssueDate();
        this.returnDate = record.getReturnDate();
    }

    public Book getBook() { return book; }
    public long getIssueDate() { return issueDate; }
    public Long getReturnDate() { return returnDate; }

    public boolean isCurrentlyBorrowed() {
        return returnDate == null;
    }
}