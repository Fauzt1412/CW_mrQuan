import models.User;
import models.UserRole;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class UserManager {
    private static Map<String, User> users = new HashMap<>();
    private static User currentUser = null;
    private static boolean usersInitialized = false;
    private static Scanner scanner = new Scanner(System.in);

    // Initialize with some default users
    static {
        initializeUsers();
    }

    private static void initializeUsers() {
        if (!usersInitialized) {
            // Add some default users (in real application, this would come from database)
            users.put("admin", new User("admin", "admin123", "Administrator", "admin@bookstore.com", UserRole.ADMIN));
            users.put("john", new User("john", "john123", "John Doe", "john@email.com", UserRole.USER));
            users.put("jane", new User("jane", "jane123", "Jane Smith", "jane@email.com", UserRole.USER));
            users.put("bob", new User("bob", "bob123", "Bob Johnson", "bob@email.com", UserRole.USER));
            users.put("alice", new User("alice", "alice123", "Alice Brown", "alice@email.com", UserRole.USER));
            usersInitialized = true;
        }
    }

    /**
     * Attempt to login with username and password
     * @return true if login successful, false otherwise
     */
    public static boolean login() {
        System.out.println("\n=== LOGIN TO BOOKSTORE ===");
        System.out.println("Available demo accounts:");
        System.out.println("Username: admin, Password: admin123 (Administrator - can see all orders)");
        System.out.println("Username: john,  Password: john123 (Regular User)");
        System.out.println("Username: jane,  Password: jane123 (Regular User)");
        System.out.println("Username: bob,   Password: bob123 (Regular User)");
        System.out.println("Username: alice, Password: alice123 (Regular User)");
        System.out.println("========================");

        System.out.print("Enter username: ");
        String username = scanner.nextLine().trim();

        if (username.isEmpty()) {
            System.out.println("Username cannot be empty.");
            return false;
        }

        System.out.print("Enter password: ");
        String password = scanner.nextLine().trim();

        if (password.isEmpty()) {
            System.out.println("Password cannot be empty.");
            return false;
        }

        User user = users.get(username.toLowerCase());
        if (user != null && user.getPassword().equals(password)) {
            currentUser = user;
            System.out.println("Login successful! Welcome, " + user.getDisplayName() + " (" + user.getRole().getDisplayName() + ")!");
            if (user.isAdmin()) {
                System.out.println("You have administrator privileges - you can view all orders from all users.");
            } else {
                System.out.println("You can view and manage your own orders.");
            }
            return true;
        } else {
            System.out.println("Invalid username or password. Please try again.");
            return false;
        }
    }

    /**
     * Logout the current user
     */
    public static void logout() {
        if (currentUser != null) {
            System.out.println("Goodbye, " + currentUser.getDisplayName() + "! You have been logged out.");
            currentUser = null;
        } else {
            System.out.println("No user is currently logged in.");
        }
    }

    /**
     * Check if a user is currently logged in
     * @return true if user is logged in, false otherwise
     */
    public static boolean isLoggedIn() {
        return currentUser != null;
    }

    /**
     * Get the current logged-in user
     * @return current user or null if not logged in
     */
    public static User getCurrentUser() {
        return currentUser;
    }

    /**
     * Get the current user's display name
     * @return display name or "Guest" if not logged in
     */
    public static String getCurrentUserName() {
        return currentUser != null ? currentUser.getDisplayName() : "Guest";
    }

    /**
     * Check if user is logged in and show error if not
     * @return true if logged in, false if not
     */
    public static boolean requireLogin() {
        if (!isLoggedIn()) {
            System.out.println("You must be logged in to perform this action.");
            return false;
        }
        return true;
    }

    /**
     * Check if current user is an administrator
     * @return true if current user is admin, false otherwise
     */
    public static boolean isCurrentUserAdmin() {
        return currentUser != null && currentUser.isAdmin();
    }
    
    /**
     * Get current user's role
     * @return UserRole of current user or null if not logged in
     */
    public static UserRole getCurrentUserRole() {
        return currentUser != null ? currentUser.getRole() : null;
    }

    /**
     * Display current session information
     */
    public static void displaySessionInfo() {
        if (isLoggedIn()) {
            System.out.println("Current user: " + currentUser.getDisplayName() + " (" + currentUser.getUsername() + ") - " + currentUser.getRole().getDisplayName());
        } else {
            System.out.println("No user logged in.");
        }
    }

    /**
     * Add a new user (for future expansion)
     * @param user User to add
     * @return true if added successfully, false if username already exists
     */
    public static boolean addUser(User user) {
        if (users.containsKey(user.getUsername().toLowerCase())) {
            return false;
        }
        users.put(user.getUsername().toLowerCase(), user);
        return true;
    }

    /**
     * Get all registered users (for admin purposes)
     * @return Map of all users
     */
    public static Map<String, User> getAllUsers() {
        return new HashMap<>(users);
    }
}