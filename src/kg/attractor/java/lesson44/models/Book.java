package kg.attractor.java.lesson44.models;

public class Book {
    private int id;
    private String title;
    private String author;
    private String image;

    private String currentHolder;

    public Book() {}

    public int getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public String getAuthor() {
        return author;
    }
    public String getImage() {
        return image;
    }
    public String getCurrentHolder() {
        return currentHolder;
    }

    public void setCurrentHolder(String currentHolder) {
        this.currentHolder = currentHolder;
    }
}