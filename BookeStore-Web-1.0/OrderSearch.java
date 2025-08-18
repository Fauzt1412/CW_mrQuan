import models.Orders;
import java.util.ArrayList;
import java.util.Queue;
import java.util.Scanner;

/**
 * Dedicated class for searching orders
 */
public class OrderSearch {
    private static Scanner scanner = new Scanner(System.in);
    
    /**
     * Main method to search orders with different criteria
     * Regular users can only search their own orders
     * Admin users can search all orders
     */
    public static void searchOrders() {
        if (!UserManager.isLoggedIn()) {
            System.out.println("You must be logged in to search orders.");
            return;
        }
        
        // Get both pending and processed orders
        Queue<Orders> orderQueue = placeOrder.getOrderQueue();
        ArrayList<Orders> processedOrders = placeOrder.getProcessedOrders();
        
        // Combine all orders for searching
        ArrayList<Orders> allOrders = new ArrayList<>();
        allOrders.addAll(orderQueue); // Add pending orders
        allOrders.addAll(processedOrders); // Add processed orders
        
        if (allOrders.isEmpty()) {
            System.out.println("No orders found.");
            return;
        }
        
        // Filter orders based on user role
        ArrayList<Orders> searchableOrders = getSearchableOrders(allOrders);
        if (searchableOrders.isEmpty()) {
            if (UserManager.isCurrentUserAdmin()) {
                System.out.println("No orders found in the system.");
            } else {
                System.out.println("You haven't placed any orders yet.");
            }
            return;
        }
        
        System.out.println("\n=== ORDER SEARCH ===");
        if (UserManager.isCurrentUserAdmin()) {
            System.out.println("Search Options (Administrator - All Orders):");
            System.out.println("1. Search by Order ID");
            System.out.println("2. Search by Customer Name");
            System.out.println("3. Search orders containing specific Book ID");
        } else {
            System.out.println("Search Options (Your Orders Only):");
            System.out.println("1. Search by Order ID");
            System.out.println("2. Search by Order Date/Details");
            System.out.println("3. Search orders containing specific Book ID");
        }
        System.out.print("Enter your choice: ");
        
        int choice;
        try {
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            if (choice < 1 || choice > 3) {
                System.out.println("Invalid choice. Please select 1, 2, or 3.");
                return;
            }
        } catch (Exception e) {
            System.out.println("Invalid input. Please enter a number between 1 and 3.");
            scanner.nextLine(); // Clear invalid input
            return;
        }
        
        if (UserManager.isCurrentUserAdmin()) {
            System.out.println("(Administrator: Searching all orders in system)");
        } else {
            System.out.println("(Searching your orders only)");
        }
        
        switch (choice) {
            case 1:
                searchByOrderId(searchableOrders);
                break;
            case 2:
                searchByCustomerName(searchableOrders);
                break;
            case 3:
                searchOrdersByBookId(searchableOrders);
                break;
        }
    }
    
    /**
     * Filter orders based on user role
     * Admin: can see all orders
     * Regular user: can only see their own orders
     */
    private static ArrayList<Orders> getSearchableOrders(ArrayList<Orders> allOrders) {
        if (UserManager.isCurrentUserAdmin()) {
            // Admin can search all orders
            return new ArrayList<>(allOrders);
        } else {
            // Regular user can only search their own orders
            ArrayList<Orders> userOrders = new ArrayList<>();
            String currentUserName = UserManager.getCurrentUserName();
            
            for (Orders order : allOrders) {
                if (order.getCustomerName().equals(currentUserName)) {
                    userOrders.add(order);
                }
            }
            return userOrders;
        }
    }
    
    /**
     * Search for a specific order by its ID
     */
    private static void searchByOrderId(ArrayList<Orders> ordersList) {
        System.out.print("Enter Order ID to search: ");
        String orderId = scanner.nextLine().trim();
        
        if (orderId.isEmpty()) {
            System.out.println("Order ID cannot be empty.");
            return;
        }
        
        for (Orders order : ordersList) {
            if (order.getOrderId().equalsIgnoreCase(orderId)) {
                displayOrderDetails(order);
                return;
            }
        }
        System.out.println("Order not found with ID: " + orderId);
    }
    
    /**
     * Search for orders by customer name (partial match) - Admin only
     * For regular users, this searches by order details instead
     */
    private static void searchByCustomerName(ArrayList<Orders> ordersList) {
        if (UserManager.isCurrentUserAdmin()) {
            // Admin can search by customer name
            System.out.print("Enter Customer Name to search: ");
            String customerName = scanner.nextLine().trim();
            
            if (customerName.isEmpty()) {
                System.out.println("Customer name cannot be empty.");
                return;
            }
            
            boolean found = false;
            System.out.println("\nOrders found for customer containing '" + customerName + "':");
            
            for (Orders order : ordersList) {
                if (order.getCustomerName().toLowerCase().contains(customerName.toLowerCase())) {
                    displayOrderDetails(order);
                    found = true;
                }
            }
            
            if (!found) {
                System.out.println("No orders found for customer: " + customerName);
            }
        } else {
            // Regular user searches by order details (book titles, etc.)
            System.out.print("Enter search term (book title, order details): ");
            String searchTerm = scanner.nextLine().trim();
            
            if (searchTerm.isEmpty()) {
                System.out.println("Search term cannot be empty.");
                return;
            }
            
            boolean found = false;
            System.out.println("\nYour orders containing '" + searchTerm + "':");
            
            for (Orders order : ordersList) {
                if (order.getBookTitle().toLowerCase().contains(searchTerm.toLowerCase()) ||
                    order.getShippingAddress().toLowerCase().contains(searchTerm.toLowerCase())) {
                    displayOrderDetails(order);
                    found = true;
                }
            }
            
            if (!found) {
                System.out.println("No orders found containing: " + searchTerm);
            }
        }
    }
    
    /**
     * Search for orders that contain a specific book ID
     */
    private static void searchOrdersByBookId(ArrayList<Orders> ordersList) {
        System.out.print("Enter Book ID to search in orders: ");
        int bookId;
        try {
            bookId = scanner.nextInt();
        } catch (Exception e) {
            System.out.println("Invalid input. Please enter a valid book ID.");
            scanner.nextLine(); // Clear invalid input
            return;
        }
        
        boolean found = false;
        System.out.println("\nOrders containing Book ID " + bookId + ":");
        
        for (Orders order : ordersList) {
            if (order.getBookIds().contains(bookId)) {
                displayOrderDetails(order);
                found = true;
            }
        }
        
        if (!found) {
            System.out.println("No orders found containing Book ID: " + bookId);
        }
    }
    
    /**
     * Display detailed information about an order
     */
    private static void displayOrderDetails(Orders order) {
        // Determine order status
        Queue<Orders> orderQueue = placeOrder.getOrderQueue();
        ArrayList<Orders> processedOrders = placeOrder.getProcessedOrders();
        
        String status = "UNKNOWN";
        if (orderQueue.contains(order)) {
            status = "PENDING";
        } else if (processedOrders.contains(order)) {
            status = "COMPLETED";
        }
        
        System.out.println("\n--- Order Details ---");
        System.out.println("Order ID: " + order.getOrderId());
        System.out.println("Status: " + status);
        System.out.println("Customer: " + order.getCustomerName());
        System.out.println("Books: " + order.getBookTitle());
        System.out.println("Book IDs: " + order.getBookIds());
        System.out.println("Total Amount: " + order.getTotalAmount());
        System.out.println("Total Price: $" + String.format("%.2f", order.getTotalPrice()));
        System.out.println("--------------------");
    }
}