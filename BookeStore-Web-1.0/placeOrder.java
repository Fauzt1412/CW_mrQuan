import java.util.ArrayList; // good for extend
import java.util.List; // was used but no longer used but safer choices
import java.util.Scanner;
import models.books;
import models.Orders;
import java.util.UUID;

public class placeOrder {
    private static ArrayList<Orders> ordersList = new ArrayList<>();
    private static ArrayList<books> booksList = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);
    private static boolean booksInitialized = false;
    public placeOrder() {
        if (!UserManager.isLoggedIn()) {
            System.out.println("You must be logged in to place orders.");
            return;
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
    
    public static void initializeBooks() {
        // Only initialize books once to prevent duplication
        if (!booksInitialized) {
            // Initialize with some sample books
            booksList.add(new books("Artists' master series", "Alot", 45.00, 5, 1));
            booksList.add(new books("The Full Stack Developer", "Chris Northwood", 25.99, 5, 2));
            booksList.add(new books("Learn to Program with Assembly", "Jonathan Bartlett", 60.00, 7, 3));
            booksList.add(new books("Design patterns explained", "Alan Shalloway", 10.99, 4, 4));
            booksInitialized = true;
        }
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
        
        ordersList.add(order);
        
        System.out.println("Order placed successfully!");
        System.out.println("Order ID: " + order.getOrderId());
        System.out.println("Book IDs in order: " + order.getBookIds());
        System.out.println("Total: $" + order.getTotalPrice());
        // System.out.println("\n=== DATA STRUCTURES USED ===");
        // System.out.println("ArrayList: Order stored in main list");
    }
    
    private books findBookById(int bookId) {
        for (books book : booksList) {
            if (book.getBookid() == bookId) {
                return book;
            }
        }
        return null;
    }
    

    // Note: Order processing functionality removed
    // Orders are automatically processed when placed
    
    /**
     * Display order history using simple list display
     * Shows all orders in chronological order (most recent last)
     */
    public static void displayOrderHistory() {
        if (!UserManager.isLoggedIn()) {
            System.out.println("You must be logged in to view order history.");
            return;
        }
        
        String currentUserName = UserManager.getCurrentUserName();
        ArrayList<Orders> userOrders = new ArrayList<>();
        
        // Get user's orders or all orders if admin
        if (UserManager.isCurrentUserAdmin()) {
            userOrders = ordersList;
            System.out.println("\n=== ALL ORDER HISTORY (Administrator View) ===");
        } else {
            for (Orders order : ordersList) {
                if (order.getCustomerName().equals(currentUserName)) {
                    userOrders.add(order);
                }
            }
            System.out.println("\n=== MY ORDER HISTORY ===");
        }
        
        if (userOrders.isEmpty()) {
            System.out.println("No order history available.");
            return;
        }
        
        System.out.println("Showing " + userOrders.size() + " order(s) in chronological order:");
        
        for (int i = 0; i < userOrders.size(); i++) {
            Orders order = userOrders.get(i);
            System.out.println((i + 1) + ". Order ID: " + order.getOrderId() + 
                             " | Customer: " + order.getCustomerName() + 
                             " | Total: $" + String.format("%.2f", order.getTotalPrice()));
        }
        System.out.println("=== END OF HISTORY ===");
    }
    
    public static ArrayList<Orders> getOrdersList() {
        return ordersList;
    }
    
    public static ArrayList<books> getBooksList() {
        return booksList;
    }
}
