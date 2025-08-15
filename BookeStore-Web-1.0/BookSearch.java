import models.books;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Dedicated class for searching books in the inventory
 */
public class BookSearch {
    private static Scanner scanner = new Scanner(System.in);
    
    /**
     * Main method to search books with different criteria
     */
    public static void searchBooks() {
        ArrayList<books> booksList = placeOrder.getBooksList();
        if (booksList.isEmpty()) {
            System.out.println("No books found in inventory.");
            return;
        }
        
        System.out.println("\n=== BOOK INVENTORY SEARCH ===");
        System.out.println("Search Options:");
        System.out.println("1. Search by Book ID");
        System.out.println("2. Search by Book Title");
        System.out.println("3. Search by Author");
        System.out.println("4. Show all available books");
        System.out.print("Enter your choice: ");
        
        int choice;
        try {
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            if (choice < 1 || choice > 4) {
                System.out.println("Invalid choice. Please select 1, 2, 3, or 4.");
                return;
            }
        } catch (Exception e) {
            System.out.println("Invalid input. Please enter a number between 1 and 4.");
            scanner.nextLine(); // Clear invalid input
            return;
        }
        
        switch (choice) {
            case 1:
                searchBookById(booksList);
                break;
            case 2:
                searchBookByTitle(booksList);
                break;
            case 3:
                searchBookByAuthor(booksList);
                break;
            case 4:
                showAllBooks(booksList);
                break;
        }
    }
    
    /**
     * Search for a specific book by its ID
     */
    private static void searchBookById(ArrayList<books> booksList) {
        System.out.print("Enter Book ID to search: ");
        int bookId;
        try {
            bookId = scanner.nextInt();
        } catch (Exception e) {
            System.out.println("Invalid input. Please enter a valid book ID.");
            scanner.nextLine(); // Clear invalid input
            return;
        }
        
        for (books book : booksList) {
            if (book.getBookid() == bookId) {
                System.out.println("\nBook found:");
                displayBookDetails(book);
                return;
            }
        }
        System.out.println("Book not found with ID: " + bookId);
    }
    
    /**
     * Search for books by title (partial match)
     */
    private static void searchBookByTitle(ArrayList<books> booksList) {
        System.out.print("Enter Book Title to search: ");
        String title = scanner.nextLine().trim();
        
        if (title.isEmpty()) {
            System.out.println("Book title cannot be empty.");
            return;
        }
        
        boolean found = false;
        System.out.println("\nBooks found with title containing '" + title + "':");
        
        for (books book : booksList) {
            if (book.getTitle().toLowerCase().contains(title.toLowerCase())) {
                displayBookDetails(book);
                found = true;
            }
        }
        
        if (!found) {
            System.out.println("No books found with title containing: " + title);
        }
    }
    
    /**
     * Search for books by author (partial match)
     */
    private static void searchBookByAuthor(ArrayList<books> booksList) {
        System.out.print("Enter Author Name to search: ");
        String author = scanner.nextLine().trim();
        
        if (author.isEmpty()) {
            System.out.println("Author name cannot be empty.");
            return;
        }
        
        boolean found = false;
        System.out.println("\nBooks found by author containing '" + author + "':");
        
        for (books book : booksList) {
            if (book.getAuthor().toLowerCase().contains(author.toLowerCase())) {
                displayBookDetails(book);
                found = true;
            }
        }
        
        if (!found) {
            System.out.println("No books found by author: " + author);
        }
    }
    
    /**
     * Show all books in the inventory
     */
    private static void showAllBooks(ArrayList<books> booksList) {
        System.out.println("\n=== ALL BOOKS IN INVENTORY ===");
        for (books book : booksList) {
            displayBookDetails(book);
        }
        System.out.println("Total books in inventory: " + booksList.size());
    }
    
    /**
     * Display detailed information about a book
     */
    private static void displayBookDetails(books book) {
        System.out.println("\n--- Book Details ---");
        System.out.println("Book ID: " + book.getBookid());
        System.out.println("Title: " + book.getTitle());
        System.out.println("Author: " + book.getAuthor());
        System.out.println("Price: $" + String.format("%.2f", book.getPrice()));
        System.out.println("Available Quantity: " + book.getQuantity());
        System.out.println("Status: " + (book.getQuantity() > 0 ? "In Stock" : "Out of Stock"));
        System.out.println("-------------------");
    }
}