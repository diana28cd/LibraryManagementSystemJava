public class Book {
    private String title;
    private String author;
    private String ISBN;

    // Constructor
    public Book(String title, String author, String ISBN) {
        this.title = title;
        this.author = author;
        this.ISBN = ISBN;
    }

    // Getters
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getISBN() { return ISBN; }

    // Method to display book details
    public void displayBookInfo() {
        System.out.println("Titlu: " + title + ", Autor: " + author + ", ISBN: " + ISBN);
    }
}
