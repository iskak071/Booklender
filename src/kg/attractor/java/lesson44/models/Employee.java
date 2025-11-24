package kg.attractor.java.lesson44.models;

public class Employee {
    private int id;
    private String fullName;

    public Employee() {}

    public int getId() {
        return id;
    }
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    public void setId(int id) {
        this.id = id;
    }
}