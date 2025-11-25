package kg.attractor.java.lesson44.models;

public class IssueRecord {
    private int bookId;
    private int employeeId;
    private long issueDate;
    private Long returnDate;

    public IssueRecord() {}

    public boolean isCurrentlyBorrowed() {
        return returnDate == null;
    }


    public int getBookId() {
        return bookId;
    }
    public int getEmployeeId() {
        return employeeId;
    }
    public long getIssueDate() {
        return issueDate;
    }
    public Long getReturnDate() {
        return returnDate;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }
    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }
    public void setIssueDate(long issueDate) {
        this.issueDate = issueDate;
    }
    public void setReturnDate(Long returnDate) {
        this.returnDate = returnDate;
    }
}