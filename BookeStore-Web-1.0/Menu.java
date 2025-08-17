import java.util.ArrayList;
import java.util.Scanner;
import models.Orders;

public class Menu{
    private static Scanner scanner = new Scanner(System.in);
    
    public static void displayMenu() {
        System.out.println("\n=== BookStore Management System ===");
        
        if (UserManager.isLoggedIn()) {
            System.out.println("Welcome, " + UserManager.getCurrentUserName() + " (" + UserManager.getCurrentUserRole().getDisplayName() + ")!");
            System.out.println("Please select an option:");
            System.out.println("1. Place Orders");
            System.out.println("2. Search Orders");
            System.out.println("3. Search Books (Inventory)");
            System.out.println("4. Sort Books in Orders (Custom Algorithms)");
            if (UserManager.isCurrentUserAdmin()) {
                System.out.println("5. Display All Orders (Admin)");
            } else {
                System.out.println("5. Display My Orders");
            }
            System.out.println("6. Order History (Stack ADT)");
            System.out.println("7. Logout");
            System.out.println("8. Exit");
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
                        if (UserManager.isCurrentUserAdmin()) {
                            System.out.println("\n=== All Orders (Administrator View) ===");
                            placeOrder.displayAllOrders();
                        } else {
                            System.out.println("\n=== My Orders ===");
                            placeOrder.displayMyOrders();
                        }
                        break;
                    case 6:
                        System.out.println("\n=== Order History (Stack ADT) ===");
                        placeOrder.displayOrderHistory();
                        break;
                    case 7:
                        System.out.println("\n=== Logout ===");
                        UserManager.logout();
                        break;
                    case 8:
                        System.out.println("Exiting...");
                        if (UserManager.isLoggedIn()) {
                            UserManager.logout();
                        }
                        scanner.close();
                        return; // Exit the program
                    default:
                        System.out.println("Invalid choice. Please select 1-8.");
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
        
        ArrayList<Orders> orders = placeOrder.getOrdersList();
        if (orders.isEmpty()) {
            System.out.println("No orders found in the system.");
            return;
        }
        
        // Check if user has any orders to sort
        ArrayList<Orders> userOrders = new ArrayList<>();
        if (UserManager.isCurrentUserAdmin()) {
            userOrders = orders; // Admin can sort all orders
        } else {
            String currentUserName = UserManager.getCurrentUserName();
            for (Orders order : orders) {
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
        SortBook.displayOrdersWithBookIds(orders);
        
        SortBook.sortBooks(orders, sortChoice);
        
        System.out.println("\nAfter sorting:");
        SortBook.displayOrdersWithBookIds(orders);
    }
}