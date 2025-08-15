import models.Orders;
import java.util.ArrayList;
import java.util.List;

/**
 * Simple Stack ADT implementation for Order History
 * Implements LIFO (Last In, First Out) operations
 * 
 * Time Complexity Analysis:
 * - push(): O(1)
 * - pop(): O(1) 
 * - peek(): O(1)
 * - isEmpty(): O(1)
 * - size(): O(1)
 * 
 * Space Complexity: O(n) where n is the number of elements
 */
public class OrderHistoryStack {
    private List<Orders> stack;
    private static final int MAX_HISTORY = 10; // Keep last 10 orders for simplicity
    
    /**
     * Constructor - Initialize empty stack
     */
    public OrderHistoryStack() {
        this.stack = new ArrayList<>();
    }
    
    /**
     * Push operation - Add order to top of stack
     * @param order Order to add to history
     */
    public void push(Orders order) {
        // If stack is full, remove oldest entry (bottom of stack)
        if (stack.size() >= MAX_HISTORY) {
            stack.remove(0); // Remove from bottom
        }
        stack.add(order); // Add to top
        System.out.println("Order " + order.getOrderId() + " added to history stack.");
    }
    
    /**
     * Pop operation - Remove and return top order from stack
     * @return Top order from stack, or null if empty
     */
    public Orders pop() {
        if (isEmpty()) {
            System.out.println("Order history stack is empty.");
            return null;
        }
        Orders topOrder = stack.remove(stack.size() - 1); // Remove from top
        System.out.println("Order " + topOrder.getOrderId() + " removed from history stack.");
        return topOrder;
    }
    
    /**
     * Peek operation - Return top order without removing it
     * @return Top order from stack, or null if empty
     */
    public Orders peek() {
        if (isEmpty()) {
            System.out.println("Order history stack is empty.");
            return null;
        }
        return stack.get(stack.size() - 1); // Return top element
    }
    
    /**
     * Check if stack is empty
     * @return true if stack is empty, false otherwise
     */
    public boolean isEmpty() {
        return stack.isEmpty();
    }
    
    /**
     * Get current size of stack
     * @return Number of orders in stack
     */
    public int size() {
        return stack.size();
    }
    
    /**
     * Display all orders in stack (from top to bottom)
     */
    public void displayHistory() {
        if (isEmpty()) {
            System.out.println("No order history available.");
            return;
        }
        
        System.out.println("\n=== ORDER HISTORY (LIFO Stack) ===");
        System.out.println("Showing last " + stack.size() + " orders (most recent first):");
        
        // Display from top to bottom (LIFO order)
        for (int i = stack.size() - 1; i >= 0; i--) {
            Orders order = stack.get(i);
            System.out.println((stack.size() - i) + ". Order ID: " + order.getOrderId() + 
                             " | Customer: " + order.getCustomerName() + 
                             " | Total: $" + String.format("%.2f", order.getTotalPrice()));
        }
        System.out.println("=== END OF HISTORY ===");
    }
    
    /**
     * Clear all history
     */
    public void clearHistory() {
        stack.clear();
        System.out.println("Order history cleared.");
    }
    
    /**
     * Get stack information for debugging
     */
    public void getStackInfo() {
        System.out.println("\n=== STACK ADT INFORMATION ===");
        System.out.println("Data Structure: Stack (LIFO - Last In, First Out)");
        System.out.println("Implementation: ArrayList-based");
        System.out.println("Current Size: " + size());
        System.out.println("Maximum Capacity: " + MAX_HISTORY);
        System.out.println("Is Empty: " + isEmpty());
        if (!isEmpty()) {
            System.out.println("Top Element: Order " + peek().getOrderId());
        }
        System.out.println("=== END STACK INFO ===");
    }
}