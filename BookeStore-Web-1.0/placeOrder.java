import java.util.ArrayList; // for books inventory
import java.util.Queue;
import java.util.LinkedList;
import java.util.Scanner;
import models.books;
import models.Orders;
import java.util.UUID;

public class placeOrder {
    private static Queue<Orders> orderQueue = new LinkedList<>();
    private static ArrayList<Orders> processedOrders = new ArrayList<>(); // For completed orders
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
        
        System.out.println("\n=== All Orders (Administrator View) ===");
        
        // Display pending orders in queue
        if (!orderQueue.isEmpty()) {
            System.out.println("\n--- PENDING ORDERS (In Queue) ---");
            int count = 1;
            for (Orders order : orderQueue) {
                System.out.println(count + ". [PENDING] Order ID: " + order.getOrderId());
                System.out.println("   Customer: " + order.getCustomerName());
                System.out.println("   Books: " + order.getBookTitle());
                System.out.println("   Total: $" + order.getTotalPrice());
                System.out.println("   ---");
                count++;
            }
        }
        
        // Display processed orders
        if (!processedOrders.isEmpty()) {
            System.out.println("\n--- PROCESSED ORDERS ---");
            for (Orders order : processedOrders) {
                System.out.println("[COMPLETED] Order ID: " + order.getOrderId());
                System.out.println("Customer: " + order.getCustomerName());
                System.out.println("Shipping Address: " + order.getShippingAddress());
                System.out.println("Books: " + order.getBookTitle());
                System.out.println("Book IDs: " + order.getBookIds());
                System.out.println("Total Amount: " + order.getTotalAmount());
                System.out.println("Total Price: $" + order.getTotalPrice());
                System.out.println("---");
            }
        }
        
        if (orderQueue.isEmpty() && processedOrders.isEmpty()) {
            System.out.println("No orders found in the system.");
        } else {
            System.out.println("Total pending orders: " + orderQueue.size());
            System.out.println("Total processed orders: " + processedOrders.size());
        }
    }
    
    public static void displayMyOrders() {
        if (!UserManager.isLoggedIn()) {
            System.out.println("You must be logged in to view your orders.");
            return;
        }
        
        String currentUserName = UserManager.getCurrentUserName();
        boolean foundOrders = false;
        
        System.out.println("\n=== My Orders - " + currentUserName + " ===");
        
        // Check pending orders in queue
        System.out.println("\n--- PENDING ORDERS ---");
        for (Orders order : orderQueue) {
            if (order.getCustomerName().equals(currentUserName)) {
                System.out.println("[PENDING] Order ID: " + order.getOrderId());
                System.out.println("Shipping Address: " + order.getShippingAddress());
                System.out.println("Books: " + order.getBookTitle());
                System.out.println("Book IDs: " + order.getBookIds());
                System.out.println("Total Amount: " + order.getTotalAmount());
                System.out.println("Total Price: $" + order.getTotalPrice());
                System.out.println("---");
                foundOrders = true;
            }
        }
        
        // Check processed orders
        System.out.println("\n--- COMPLETED ORDERS ---");
        for (Orders order : processedOrders) {
            if (order.getCustomerName().equals(currentUserName)) {
                System.out.println("[COMPLETED] Order ID: " + order.getOrderId());
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
        
        // Add order to queue for processing
        orderQueue.offer(order);
        
        System.out.println("Order placed successfully and added to processing queue!");
        System.out.println("Order ID: " + order.getOrderId());
        System.out.println("Book IDs in order: " + order.getBookIds());
        System.out.println("Total: $" + order.getTotalPrice());
        System.out.println("Status: PENDING (Position in queue: " + orderQueue.size() + ")");
        System.out.println("\n=== DATA STRUCTURES USED ===");
        System.out.println("Queue: Order added to processing queue (FIFO)");
        System.out.println("\nNote: Your order is now in the processing queue.");
        System.out.println("An administrator will process it soon. Check 'My Order Status' for updates.");
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
     * Process the next order in the queue (FIFO)
     */
    public static void processNextOrder() {
        if (orderQueue.isEmpty()) {
            System.out.println("No orders in queue to process.");
            return;
        }
        
        Orders order = orderQueue.poll(); // Remove and get the first order
        processedOrders.add(order); // Add to processed orders
        
        System.out.println("\n=== ORDER PROCESSED ===");
        System.out.println("Order ID: " + order.getOrderId() + " has been processed and completed!");
        System.out.println("Customer: " + order.getCustomerName());
        System.out.println("Status: COMPLETED");
        System.out.println("Remaining orders in queue: " + orderQueue.size());
    }
    
    /**
     * Display order history using processed orders
     * Shows all completed orders in chronological order
     */
    public static void displayOrderHistory() {
        if (!UserManager.isLoggedIn()) {
            System.out.println("You must be logged in to view order history.");
            return;
        }
        
        String currentUserName = UserManager.getCurrentUserName();
        ArrayList<Orders> userOrders = new ArrayList<>();
        
        // Get user's processed orders or all processed orders if admin
        if (UserManager.isCurrentUserAdmin()) {
            userOrders = processedOrders;
            System.out.println("\n=== ALL ORDER HISTORY (Administrator View) ===");
        } else {
            for (Orders order : processedOrders) {
                if (order.getCustomerName().equals(currentUserName)) {
                    userOrders.add(order);
                }
            }
            System.out.println("\n=== MY ORDER HISTORY ===");
        }
        
        if (userOrders.isEmpty()) {
            System.out.println("No completed order history available.");
            return;
        }
        
        System.out.println("Showing " + userOrders.size() + " completed order(s) in chronological order:");
        
        for (int i = 0; i < userOrders.size(); i++) {
            Orders order = userOrders.get(i);
            System.out.println((i + 1) + ". [COMPLETED] Order ID: " + order.getOrderId() + 
                             " | Customer: " + order.getCustomerName() + 
                             " | Total: $" + String.format("%.2f", order.getTotalPrice()));
        }
        System.out.println("=== END OF HISTORY ===");
    }
    
    /**
     * Get the current order queue status
     */
    public static void displayQueueStatus() {
        System.out.println("\n=== ORDER QUEUE STATUS ===");
        System.out.println("Orders pending in queue: " + orderQueue.size());
        System.out.println("Orders completed: " + processedOrders.size());
        
        if (!orderQueue.isEmpty()) {
            System.out.println("\nNext order to be processed:");
            Orders nextOrder = orderQueue.peek();
            System.out.println("Order ID: " + nextOrder.getOrderId());
            System.out.println("Customer: " + nextOrder.getCustomerName());
        }
    }
    
    public static Queue<Orders> getOrderQueue() {
        return orderQueue;
    }
    
    public static ArrayList<Orders> getProcessedOrders() {
        return processedOrders;
    }
    
    public static ArrayList<books> getBooksList() {
        return booksList;
    }
}
