import java.util.ArrayList;
import java.util.Queue;
import java.util.Scanner;
import models.Orders;

public class Menu{
    private static Scanner scanner = new Scanner(System.in);
    
    public static void displayMenu() {
        System.out.println("\n=== BookStore Management System ===");
        placeOrder.initializeBooks();

        if (UserManager.isLoggedIn()) {
            System.out.println("Welcome, " + UserManager.getCurrentUserName() + " (" + UserManager.getCurrentUserRole().getDisplayName() + ")!");
            System.out.println("Please select an option:");
            
            if (UserManager.isCurrentUserAdmin()) {
                // Admin menu - no place orders option
                System.out.println("1. Search Orders");
                System.out.println("2. Search Books (Inventory)");
                System.out.println("3. Sort Books in Orders (Custom Algorithms)");
                System.out.println("4. Display All Orders (Admin)");
                System.out.println("5. Process Pending Orders (Admin)");
                System.out.println("6. Order Queue Status");
                System.out.println("7. Order History");
                System.out.println("8. Logout");
                System.out.println("9. Exit");
            } else {
                // Regular user menu - includes place orders
                System.out.println("1. Place Orders");
                System.out.println("2. Search Orders");
                System.out.println("3. Search Books (Inventory)");
                System.out.println("4. Sort Books in Orders (Custom Algorithms)");
                System.out.println("5. Display My Orders");
                System.out.println("6. Check My Order Status");
                System.out.println("7. Order History");
                System.out.println("8. Logout");
                System.out.println("9. Exit");
            }
        } else {
            System.out.println("Please login to continue:");
            System.out.println("1. Login");
            System.out.println("2. Search Books (Inventory) - Guest Access");
            System.out.println("3. Exit");
        }
    }
    public static void main(String[] args) {
        System.out.println("Welcome to BookStore Management System!");
        while (true) {
            displayMenu();
            int choice;
            System.out.print("Enter your choice: ");
            try {
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.nextLine(); // Clear invalid input
                continue;
            }
            
            if (UserManager.isLoggedIn()) {
                // Menu for logged-in users
                if (UserManager.isCurrentUserAdmin()) {
                    // Admin menu handling
                    switch (choice) {
                        case 1:
                            System.out.println("\n=== Searching Orders ===");
                            OrderSearch.searchOrders();
                            break;
                        case 2:
                            System.out.println("\n=== Searching Books ===");
                            BookSearch.searchBooks();
                            break;
                        case 3:
                            System.out.println("\n=== Sorting Books in Orders ===");
                            sortBooksMenu();
                            break;
                        case 4:
                            System.out.println("\n=== All Orders (Administrator View) ===");
                            placeOrder.displayAllOrders();
                            break;
                        case 5:
                            System.out.println("\n=== Process Pending Orders ===");
                            processOrdersMenu();
                            break;
                        case 6:
                            System.out.println("\n=== Order Queue Status ===");
                            placeOrder.displayQueueStatus();
                            break;
                        case 7:
                            System.out.println("\n=== Order History ===");
                            placeOrder.displayOrderHistory();
                            break;
                        case 8:
                            System.out.println("\n=== Logout ===");
                            UserManager.logout();
                            break;
                        case 9:
                            System.out.println("Exiting...");
                            if (UserManager.isLoggedIn()) {
                                UserManager.logout();
                            }
                            scanner.close();
                            return; // Exit the program
                        default:
                            System.out.println("Invalid choice. Please select 1-9.");
                    }
                } else {
                    // Regular user menu handling
                    switch (choice) {
                        case 1:
                            System.out.println("\n=== Placing Orders ===");
                            new placeOrder();
                            break;
                        case 2:
                            System.out.println("\n=== Searching Orders ===");
                            OrderSearch.searchOrders();
                            break;
                        case 3:
                            System.out.println("\n=== Searching Books ===");
                            BookSearch.searchBooks();
                            break;
                        case 4:
                            System.out.println("\n=== Sorting Books in Orders ===");
                            sortBooksMenu();
                            break;
                        case 5:
                            System.out.println("\n=== My Orders ===");
                            placeOrder.displayMyOrders();
                            break;
                        case 6:
                            System.out.println("\n=== My Order Status ===");
                            checkMyOrderStatus();
                            break;
                        case 7:
                            System.out.println("\n=== Order History ===");
                            placeOrder.displayOrderHistory();
                            break;
                        case 8:
                            System.out.println("\n=== Logout ===");
                            UserManager.logout();
                            break;
                        case 9:
                            System.out.println("Exiting...");
                            if (UserManager.isLoggedIn()) {
                                UserManager.logout();
                            }
                            scanner.close();
                            return; // Exit the program
                        default:
                            System.out.println("Invalid choice. Please select 1-9.");
                    }
                }
            } else {
                // Menu for non-logged-in users
                switch (choice) {
                    case 1:
                        System.out.println("\n=== Login ===");
                        UserManager.login();
                        break;
                    case 2:
                        System.out.println("\n=== Searching Books (Guest Access) ===");
                        BookSearch.searchBooks();
                        break;
                    case 3:
                        System.out.println("Exiting...");
                        scanner.close();
                        return; // Exit the program
                    default:
                        System.out.println("Invalid choice. Please select 1-3.");
                }
            }
        }
    }

    
    private static void sortBooksMenu() {
        if (!UserManager.isLoggedIn()) {
            System.out.println("You must be logged in to sort orders.");
            return;
        }
        
        // Get both pending and processed orders
        Queue<Orders> orderQueue = placeOrder.getOrderQueue();
        ArrayList<Orders> processedOrders = placeOrder.getProcessedOrders();
        
        // Combine all orders for sorting
        ArrayList<Orders> allOrders = new ArrayList<>();
        allOrders.addAll(orderQueue); // Add pending orders
        allOrders.addAll(processedOrders); // Add processed orders
        
        if (allOrders.isEmpty()) {
            System.out.println("No orders found in the system.");
            return;
        }
        
        // Check if user has any orders to sort
        ArrayList<Orders> userOrders = new ArrayList<>();
        if (UserManager.isCurrentUserAdmin()) {
            userOrders = allOrders; // Admin can sort all orders
        } else {
            String currentUserName = UserManager.getCurrentUserName();
            for (Orders order : allOrders) {
                if (order.getCustomerName().equals(currentUserName)) {
                    userOrders.add(order);
                }
            }
        }
        
        if (userOrders.isEmpty()) {
            if (UserManager.isCurrentUserAdmin()) {
                System.out.println("No orders found in the system to sort.");
            } else {
                System.out.println("You have no orders to sort.");
            }
            return;
        }
        
        System.out.println("\n=== CUSTOM SORTING ALGORITHMS ===");
        System.out.println("1. Sort book IDs in ascending order (Insertion Sort - O(n²))");
        System.out.println("2. Sort book IDs in descending order (Selection Sort - O(n²))");
        System.out.println("3. Sort orders by book count (Built-in TimSort - O(n log n))");
        System.out.print("Enter your choice: ");
        int sortChoice;
        try {
            sortChoice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            if (sortChoice < 1 || sortChoice > 3) {
                System.out.println("Invalid choice. Please select 1, 2, or 3.");
                return;
            }
        } catch (Exception e) {
            System.out.println("Invalid input. Please enter a number between 1 and 3.");
            scanner.nextLine(); // Clear invalid input
            return;
        }
        
        System.out.println("\nBefore sorting:");
        SortBook.displayOrdersWithBookIds(userOrders);
        
        SortBook.sortBooks(userOrders, sortChoice);
        
        System.out.println("\nAfter sorting:");
        SortBook.displayOrdersWithBookIds(userOrders);
    }
    
    /**
     * Admin menu for processing pending orders
     */
    private static void processOrdersMenu() {
        if (!UserManager.isCurrentUserAdmin()) {
            System.out.println("Access denied. Only administrators can process orders.");
            return;
        }
        
        Queue<Orders> orderQueue = placeOrder.getOrderQueue();
        if (orderQueue.isEmpty()) {
            System.out.println("No pending orders to process.");
            return;
        }
        
        System.out.println("\n=== ORDER PROCESSING MENU ===");
        System.out.println("Pending orders in queue: " + orderQueue.size());
        System.out.println("\n1. Process next order (FIFO)");
        System.out.println("2. Process all pending orders");
        System.out.println("3. View pending orders");
        System.out.println("4. Process specific order by ID");
        System.out.println("5. Return to main menu");
        
        System.out.print("Enter your choice: ");
        int choice;
        try {
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            if (choice < 1 || choice > 5) {
                System.out.println("Invalid choice. Please select 1-5.");
                return;
            }
        } catch (Exception e) {
            System.out.println("Invalid input. Please enter a number between 1 and 5.");
            scanner.nextLine(); // Clear invalid input
            return;
        }
        
        switch (choice) {
            case 1:
                placeOrder.processNextOrder();
                break;
            case 2:
                processAllOrders();
                break;
            case 3:
                displayPendingOrders();
                break;
            case 4:
                processSpecificOrder();
                break;
            case 5:
                return;
        }
    }
    
    /**
     * Process all pending orders
     */
    private static void processAllOrders() {
        Queue<Orders> orderQueue = placeOrder.getOrderQueue();
        if (orderQueue.isEmpty()) {
            System.out.println("No pending orders to process.");
            return;
        }
        
        int processedCount = 0;
        int totalOrders = orderQueue.size();
        
        System.out.println("\n=== PROCESSING ALL ORDERS ===");
        System.out.println("Processing " + totalOrders + " pending orders...");
        
        while (!orderQueue.isEmpty()) {
            placeOrder.processNextOrder();
            processedCount++;
        }
        
        System.out.println("\n=== BATCH PROCESSING COMPLETE ===");
        System.out.println("Successfully processed " + processedCount + " orders.");
        System.out.println("All orders have been completed!");
    }
    
    /**
     * Display all pending orders in the queue
     */
    private static void displayPendingOrders() {
        Queue<Orders> orderQueue = placeOrder.getOrderQueue();
        if (orderQueue.isEmpty()) {
            System.out.println("No pending orders in queue.");
            return;
        }
        
        System.out.println("\n=== PENDING ORDERS IN QUEUE ===");
        int position = 1;
        for (Orders order : orderQueue) {
            System.out.println(position + ". Order ID: " + order.getOrderId());
            System.out.println("   Customer: " + order.getCustomerName());
            System.out.println("   Books: " + order.getBookTitle());
            System.out.println("   Total: $" + String.format("%.2f", order.getTotalPrice()));
            System.out.println("   Status: PENDING");
            System.out.println("   ---");
            position++;
        }
        System.out.println("Total pending orders: " + orderQueue.size());
    }
    
    /**
     * Process a specific order by ID
     */
    private static void processSpecificOrder() {
        Queue<Orders> orderQueue = placeOrder.getOrderQueue();
        if (orderQueue.isEmpty()) {
            System.out.println("No pending orders to process.");
            return;
        }
        
        System.out.print("Enter Order ID to process: ");
        String orderId = scanner.nextLine().trim();
        
        if (orderId.isEmpty()) {
            System.out.println("Order ID cannot be empty.");
            return;
        }
        
        // Find and process specific order
        Orders targetOrder = null;
        for (Orders order : orderQueue) {
            if (order.getOrderId().equalsIgnoreCase(orderId)) {
                targetOrder = order;
                break;
            }
        }
        
        if (targetOrder == null) {
            System.out.println("Order not found in pending queue: " + orderId);
            return;
        }
        
        // Remove the specific order and process it
        orderQueue.remove(targetOrder);
        placeOrder.getProcessedOrders().add(targetOrder);
        
        System.out.println("\n=== SPECIFIC ORDER PROCESSED ===");
        System.out.println("Order ID: " + targetOrder.getOrderId() + " has been processed!");
        System.out.println("Customer: " + targetOrder.getCustomerName());
        System.out.println("Status: COMPLETED");
        System.out.println("Remaining orders in queue: " + orderQueue.size());
    }
    
    /**
     * Check order status for regular users
     */
    private static void checkMyOrderStatus() {
        if (!UserManager.isLoggedIn()) {
            System.out.println("You must be logged in to check order status.");
            return;
        }
        
        String currentUserName = UserManager.getCurrentUserName();
        Queue<Orders> orderQueue = placeOrder.getOrderQueue();
        ArrayList<Orders> processedOrders = placeOrder.getProcessedOrders();
        
        // Count user's orders
        int pendingCount = 0;
        int completedCount = 0;
        
        System.out.println("\n=== MY ORDER STATUS - " + currentUserName + " ===");
        
        // Check pending orders
        System.out.println("\n--- PENDING ORDERS ---");
        int position = 1;
        for (Orders order : orderQueue) {
            if (order.getCustomerName().equals(currentUserName)) {
                System.out.println("Position " + position + " in queue:");
                System.out.println("  Order ID: " + order.getOrderId());
                System.out.println("  Books: " + order.getBookTitle());
                System.out.println("  Total: $" + String.format("%.2f", order.getTotalPrice()));
                System.out.println("  Status: PENDING");
                System.out.println("  ---");
                pendingCount++;
            }
            position++;
        }
        
        // Check completed orders
        System.out.println("\n--- COMPLETED ORDERS ---");
        for (Orders order : processedOrders) {
            if (order.getCustomerName().equals(currentUserName)) {
                System.out.println("Order ID: " + order.getOrderId());
                System.out.println("Books: " + order.getBookTitle());
                System.out.println("Total: $" + String.format("%.2f", order.getTotalPrice()));
                System.out.println("Status: COMPLETED");
                System.out.println("---");
                completedCount++;
            }
        }
        
        System.out.println("\n=== SUMMARY ===");
        System.out.println("Pending orders: " + pendingCount);
        System.out.println("Completed orders: " + completedCount);
        System.out.println("Total orders: " + (pendingCount + completedCount));
        
        if (pendingCount > 0) {
            System.out.println("\nNote: Your pending orders will be processed by administrators.");
        }
    }
}