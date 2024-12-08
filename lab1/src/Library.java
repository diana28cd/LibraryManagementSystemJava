import java.util.ArrayList;

public class Library {
    private ArrayList<Book> books;

    // Constructor
    public Library() {
        books = new ArrayList<>();
    }

    // Method to add a book
    public void addBook(Book book) {
        books.add(book);
        System.out.println("Cartea a fost adăugată: " + book.getTitle());
    }

    // Method to remove a book by ISBN
    public void removeBook(String ISBN) {
        for (Book book : books) {
            if (book.getISBN().equals(ISBN)) {
                books.remove(book);
                System.out.println("Cartea cu ISBN " + ISBN + " a fost eliminată.");
                return;
            }
        }
        System.out.println("Cartea nu a fost găsită.");
    }

    // Method to display all books
    public void displayAllBooks() {
        if (books.isEmpty()) {
            System.out.println("Biblioteca este goală.");
        } else {
            for (Book book : books) {
                book.displayBookInfo();
            }
        }
    }
}
