//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        // Crearea unei instanțe de Library
        Library library = new Library();

        // Crearea cărților
        Book book1 = new Book("Mândrie și Prejudecată", "Jane Austen", "12345");
        Book book2 = new Book("1984", "George Orwell", "67890");

        // Adăugarea cărților în bibliotecă
        library.addBook(book1);
        library.addBook(book2);

        // Afișarea tuturor cărților
        System.out.println("Cărțile din bibliotecă:");
        library.displayAllBooks();

        // Eliminarea unei cărți
        library.removeBook("12345");

        // Afișarea cărților rămase
        System.out.println("După eliminare:");
        library.displayAllBooks();
    }
}
