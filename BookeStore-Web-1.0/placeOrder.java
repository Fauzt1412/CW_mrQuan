import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Scanner;
import models.books;
import models.Orders;
import java.util.UUID;

public class placeOrder {
    private static ArrayList<Orders> ordersList = new ArrayList<>();
    private static ArrayList<books> booksList = new ArrayList<>();
    private static Queue<Orders> orderQueue = new LinkedList<>();
    private static OrderHistoryStack orderHistory = new OrderHistoryStack(); // Stack ADT for order history
    private static Scanner scanner = new Scanner(System.in);
    private static boolean booksInitialized = false; // Flag to prevent duplicate initialization
        
    public placeOrder() {
        if (!UserManager.isLoggedIn()) {
            System.out.println("You must be logged in to place orders.");
            return;
        }
        
        if (!booksInitialized) {
            initializeBooks();
            booksInitialized = true;
        }
        createOrder();
    }
    
    public static void displayAllOrders() {
        // Check if user is admin
        if (!UserManager.isCurrentUserAdmin()) {
            System.out.println("Access denied. Only administrators can view all orders.");
            System.out.println("Use 'Display My Orders' to see your own orders.");
            return;
        }
        
        if (ordersList.isEmpty()) {
            System.out.println("No orders found in the system.");
            return;
        }
        
        System.out.println("\n=== All Orders (Administrator View) ===");
        for (Orders order : ordersList) {
            System.out.println("Order ID: " + order.getOrderId());
            System.out.println("Customer: " + order.getCustomerName());
            System.out.println("Shipping Address: " + order.getShippingAddress());
            System.out.println("Books: " + order.getBookTitle());
            System.out.println("Book IDs: " + order.getBookIds());
            System.out.println("Total Amount: " + order.getTotalAmount());
            System.out.println("Total Price: $" + order.getTotalPrice());
            System.out.println("---");
        }
        System.out.println("Total orders in system: " + ordersList.size());
    }
    
    public static void displayMyOrders() {
        if (!UserManager.isLoggedIn()) {
            System.out.println("You must be logged in to view your orders.");
            return;
        }
        
        String currentUserName = UserManager.getCurrentUserName();
        boolean foundOrders = false;
        
        System.out.println("\n=== My Orders " + currentUserName);
        
        for (Orders order : ordersList) {
            if (order.getCustomerName().equals(currentUserName)) {
                System.out.println("Order ID: " + order.getOrderId());
                System.out.println("Shipping Address: " + order.getShippingAddress());
                System.out.println("Books: " + order.getBookTitle());
                System.out.println("Book IDs: " + order.getBookIds());
                System.out.println("Total Amount: " + order.getTotalAmount());
                System.out.println("Total Price: $" + order.getTotalPrice());
                System.out.println("---");
                foundOrders = true;
            }
        }
        
        if (!foundOrders) {
            System.out.println("You haven't placed any orders yet.");
            System.out.println("Use 'Place Orders' to create your first order!");
        }
    }
    
    private void initializeBooks() {
        // Initialize with some sample books
        booksList.add(new books("Book A", "Author A", 10.99, 5, 1));
        booksList.add(new books("Book B", "Author B", 12.99, 3, 2));
        booksList.add(new books("Book C", "Author C", 15.99, 7, 3));
        booksList.add(new books("Book D", "Author D", 8.99, 2, 4));
    }
    
    private void createOrder() {
        System.out.println("Available Books:");
        for (int i = 0; i < booksList.size(); i++) {
            books book = booksList.get(i);
            System.out.println("Book ID: " + book.getBookid() + ", Title: " + book.getTitle() + ", Author: " + book.getAuthor() + ", Price: $" + book.getPrice() + ", Quantity: " + book.getQuantity());
        }
        
        // Use logged-in user's name instead of asking for input
        String customerName = UserManager.getCurrentUserName();
        System.out.println("Creating order for: " + customerName);
        
        // Ask for shipping address as per requirement
        System.out.print("Enter shipping address: ");
        String shippingAddress = scanner.nextLine().trim();
        if (shippingAddress.isEmpty()) {
            shippingAddress = "Default Address - Not Specified";
        }
        
        String orderId = "ORD-" + UUID.randomUUID().toString().substring(0, 8);
        Orders order = new Orders(orderId, customerName, shippingAddress, "", 0, 0.0);
        
        System.out.print("How many different books do you want to order? ");
        int numBooks;
        try {
            numBooks = scanner.nextInt();
            if (numBooks <= 0) {
                System.out.println("Number of books must be positive. Order cancelled.");
                return;
            }
        } catch (Exception e) {
            System.out.println("Invalid input. Please enter a valid number. Order cancelled.");
            scanner.nextLine(); // Clear invalid input
            return;
        }
        
        double totalPrice = 0.0;
        int totalAmount = 0;
        StringBuilder bookTitles = new StringBuilder();
        
        for (int i = 0; i < numBooks; i++) {
            System.out.print("Select book ID for book " + (i + 1) + ": ");
            int bookid;
            try {
                bookid = scanner.nextInt();
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a valid book ID.");
                scanner.nextLine(); // Clear invalid input
                i--; // Retry this book
                continue;
            }
            
            books selectedBook = findBookById(bookid);
            if (selectedBook != null) {
                if (selectedBook.getQuantity() <= 0) {
                    System.out.println("Book '" + selectedBook.getTitle() + "' is out of stock!");
                    i--; // Retry this book
                    continue;
                }
                
                System.out.print("Enter quantity for " + selectedBook.getTitle() + " (Available: " + selectedBook.getQuantity() + "): ");
                int quantity;
                try {
                    quantity = scanner.nextInt();
                    if (quantity <= 0) {
                        System.out.println("Quantity must be positive. Please try again.");
                        i--; // Retry this book
                        continue;
                    }
                } catch (Exception e) {
                    System.out.println("Invalid input. Please enter a valid quantity.");
                    scanner.nextLine(); // Clear invalid input
                    i--; // Retry this book
                    continue;
                }
                
                if (quantity <= selectedBook.getQuantity()) {
                    order.addBookId(bookid);
                    totalPrice += selectedBook.getPrice() * quantity;
                    totalAmount += quantity;
                    
                    if (bookTitles.length() > 0) {
                        bookTitles.append(", ");
                    }
                    bookTitles.append(selectedBook.getTitle());
                    
                    // Update book quantity
                    selectedBook.setQuantity(selectedBook.getQuantity() - quantity);
                    
                    System.out.println("Added " + quantity + " copies of " + selectedBook.getTitle() + " to order.");
                } else {
                    System.out.println("Not enough stock! Available: " + selectedBook.getQuantity());
                    i--; // Retry this book
                }
            } else {
                System.out.println("Book not found! Please try again.");
                i--; // Retry this book
            }
        }
        
        order.setBookTitle(bookTitles.toString());
        order.setTotalAmount(totalAmount);
        order.setTotalPrice(totalPrice);
        
        orderQueue.offer(order);
        ordersList.add(order);
        orderHistory.push(order); // Add to Stack ADT for history tracking
        
        System.out.println("Order placed successfully!");
        System.out.println("Order ID: " + order.getOrderId());
        System.out.println("Book IDs in order: " + order.getBookIds());
        System.out.println("Total: $" + order.getTotalPrice());
        // System.out.println("\n=== DATA STRUCTURES USED ===");
        // System.out.println("Queue (FIFO): Order added to processing queue");
        // System.out.println("ArrayList: Order stored in main list");
        // System.out.println("Stack (LIFO): Order added to history stack");
    }
    
    private books findBookById(int bookId) {
        for (books book : booksList) {
            if (book.getBookid() == bookId) {
                return book;
            }
        }
        return null;
    }
    

    // Note: Order processing functionality removed as per user request
    // Orders are automatically processed when placed
    
    /**
     * Display order history using Stack ADT (LIFO)
     * Demonstrates Stack operations: peek, isEmpty, size
     */
    public static void displayOrderHistory() {
        System.out.println("\n=== ORDER HISTORY (Stack ADT - LIFO) ===");
        orderHistory.displayHistory();
        orderHistory.getStackInfo();
    }
    
    /**
     * Get the order history stack for external access
     * @return OrderHistoryStack instance
     */
    public static OrderHistoryStack getOrderHistory() {
        return orderHistory;
    }
    
    public static ArrayList<Orders> getOrdersList() {
        return ordersList;
    }
    
    public static ArrayList<books> getBooksList() {
        return booksList;
    }
}
