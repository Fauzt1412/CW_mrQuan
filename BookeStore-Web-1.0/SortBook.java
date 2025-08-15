import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import models.Orders;
import models.books;

/**
 * SortBook class implements custom sorting algorithms as required:
 * - Insertion Sort: Simple and efficient for small datasets
 * - Selection Sort: Easy to understand and implement
 * - Built-in sort for comparison
 * 
 * Time Complexity Analysis:
 * - Insertion Sort: O(n²) worst case, O(n) best case
 * - Selection Sort: O(n²) in all cases
 * - Collections.sort(): O(n log n) average case (TimSort)
 */
public class SortBook {
    
    /**
     * CUSTOM INSERTION SORT ALGORITHM
     * Sorts a list of integers in ascending order using insertion sort
     * Time Complexity: O(n²) worst case, O(n) best case
     * Space Complexity: O(1)
     */
    public static void insertionSort(List<Integer> list) {
        for (int i = 1; i < list.size(); i++) {
            int key = list.get(i);
            int j = i - 1;
            
            // Move elements greater than key one position ahead
            while (j >= 0 && list.get(j) > key) {
                list.set(j + 1, list.get(j));
                j--;
            }
            list.set(j + 1, key);
        }
    }
    
    /**
     * CUSTOM SELECTION SORT ALGORITHM
     * Sorts a list of integers in ascending order using selection sort
     * Time Complexity: O(n²) in all cases
     * Space Complexity: O(1)
     */
    public static void selectionSort(List<Integer> list) {
        for (int i = 0; i < list.size() - 1; i++) {
            int minIndex = i;
            
            // Find the minimum element in remaining array
            for (int j = i + 1; j < list.size(); j++) {
                if (list.get(j) < list.get(minIndex)) {
                    minIndex = j;
                }
            }
            
            // Swap the found minimum element with the first element
            if (minIndex != i) {
                int temp = list.get(i);
                list.set(i, list.get(minIndex));
                list.set(minIndex, temp);
            }
        }
    }
    
    /**
     * Sorts the book IDs within each order in ascending order using INSERTION SORT
     * Only processes orders that the current user has access to
     * @param ordersList List of orders to sort book IDs for
     */
    public static void sortBookIdsInOrders(ArrayList<Orders> ordersList) {
        if (ordersList == null || ordersList.isEmpty()) {
            System.out.println("No orders to sort.");
            return;
        }
        
        // Filter orders based on user permissions
        ArrayList<Orders> sortableOrders = getSortableOrders(ordersList);
        if (sortableOrders.isEmpty()) {
            if (UserManager.isCurrentUserAdmin()) {
                System.out.println("No orders found in the system to sort.");
            } else {
                System.out.println("You have no orders to sort.");
            }
            return;
        }
        
        System.out.println("Using INSERTION SORT algorithm...");
        if (UserManager.isCurrentUserAdmin()) {
            System.out.println("(Administrator: Sorting all orders in system)");
        } else {
            System.out.println("(Sorting your orders only)");
        }
        
        for (Orders order : sortableOrders) {
            if (order.getBookIds() != null && !order.getBookIds().isEmpty()) {
                // Create a copy to avoid modifying the original during sorting
                List<Integer> bookIds = new ArrayList<>(order.getBookIds());
                insertionSort(bookIds);
                order.setBookIds(bookIds);
                System.out.println("Sorted book IDs for order " + order.getOrderId() + ": " + order.getBookIds());
            } else {
                System.out.println("Order " + order.getOrderId() + " has no book IDs to sort.");
            }
        }
    }
    
    /**
     * CUSTOM SELECTION SORT FOR DESCENDING ORDER
     * Sorts a list of integers in descending order using selection sort
     * Time Complexity: O(n²) in all cases
     * Space Complexity: O(1)
     */
    public static void selectionSortDescending(List<Integer> list) {
        for (int i = 0; i < list.size() - 1; i++) {
            int maxIndex = i;
            
            // Find the maximum element in remaining array
            for (int j = i + 1; j < list.size(); j++) {
                if (list.get(j) > list.get(maxIndex)) {
                    maxIndex = j;
                }
            }
            
            // Swap the found maximum element with the first element
            if (maxIndex != i) {
                int temp = list.get(i);
                list.set(i, list.get(maxIndex));
                list.set(maxIndex, temp);
            }
        }
    }
    
    /**
     * Sorts the book IDs within each order in descending order using SELECTION SORT
     * Only processes orders that the current user has access to
     * @param ordersList List of orders to sort book IDs for
     */
    public static void sortBookIdsInOrdersDescending(ArrayList<Orders> ordersList) {
        if (ordersList == null || ordersList.isEmpty()) {
            System.out.println("No orders to sort.");
            return;
        }
        
        // Filter orders based on user permissions
        ArrayList<Orders> sortableOrders = getSortableOrders(ordersList);
        if (sortableOrders.isEmpty()) {
            if (UserManager.isCurrentUserAdmin()) {
                System.out.println("No orders found in the system to sort.");
            } else {
                System.out.println("You have no orders to sort.");
            }
            return;
        }
        
        System.out.println("Using SELECTION SORT algorithm (descending)...");
        if (UserManager.isCurrentUserAdmin()) {
            System.out.println("(Administrator: Sorting all orders in system)");
        } else {
            System.out.println("(Sorting your orders only)");
        }
        
        for (Orders order : sortableOrders) {
            if (order.getBookIds() != null && !order.getBookIds().isEmpty()) {
                // Create a copy to avoid modifying the original during sorting
                List<Integer> bookIds = new ArrayList<>(order.getBookIds());
                selectionSortDescending(bookIds);
                order.setBookIds(bookIds);
                System.out.println("Sorted book IDs (descending) for order " + order.getOrderId() + ": " + order.getBookIds());
            } else {
                System.out.println("Order " + order.getOrderId() + " has no book IDs to sort.");
            }
        }
    }
    
    /**
     * Sorts orders by the number of books in each order
     * Only processes orders that the current user has access to
     * @param ordersList List of orders to sort
     */
    public static void sortOrdersByBookCount(ArrayList<Orders> ordersList) {
        if (ordersList == null || ordersList.isEmpty()) {
            System.out.println("No orders to sort.");
            return;
        }
        
        // Filter orders based on user permissions
        ArrayList<Orders> sortableOrders = getSortableOrders(ordersList);
        if (sortableOrders.isEmpty()) {
            if (UserManager.isCurrentUserAdmin()) {
                System.out.println("No orders found in the system to sort.");
            } else {
                System.out.println("You have no orders to sort.");
            }
            return;
        }
        
        if (UserManager.isCurrentUserAdmin()) {
            System.out.println("(Administrator: Sorting all orders in system)");
        } else {
            System.out.println("(Sorting your orders only)");
        }
        
        // Sort the filtered orders
        Collections.sort(sortableOrders, new Comparator<Orders>() {
            @Override
            public int compare(Orders o1, Orders o2) {
                int count1 = (o1.getBookIds() != null) ? o1.getBookIds().size() : 0;
                int count2 = (o2.getBookIds() != null) ? o2.getBookIds().size() : 0;
                return Integer.compare(count1, count2);
            }
        });
        
        // FIXED: Update the original ordersList with the sorted order
        if (UserManager.isCurrentUserAdmin()) {
            // Admin: Replace the entire list with sorted orders
            ordersList.clear();
            ordersList.addAll(sortableOrders);
        } else {
            // Regular user: Update only their orders in the original list
            String currentUserName = UserManager.getCurrentUserName();
            
            // Remove user's old orders from the original list
            ordersList.removeIf(order -> order.getCustomerName().equals(currentUserName));
            
            // Add back the sorted user orders
            ordersList.addAll(sortableOrders);
        }
        
        System.out.println("Orders sorted by book count:");
        for (Orders order : sortableOrders) {
            int bookCount = (order.getBookIds() != null) ? order.getBookIds().size() : 0;
            System.out.println("Order " + order.getOrderId() + " (" + order.getCustomerName() + ") has " + bookCount + " books");
        }
    }
    
    /**
     * Main sorting function that provides options for different sorting methods
     * Implements custom sorting algorithms as required by the specification
     * @param ordersList List of orders to sort
     * @param sortType Type of sorting: 1=ascending book IDs, 2=descending book IDs, 3=by book count
     */
    public static void sortBooks(ArrayList<Orders> ordersList, int sortType) {
        System.out.println("\n=== CUSTOM SORTING ALGORITHMS IMPLEMENTATION ===");
        switch (sortType) {
            case 1:
                System.out.println("Sorting book IDs in ascending order using INSERTION SORT...");
                System.out.println("Algorithm: Insertion Sort | Time Complexity: O(n²) | Space: O(1)");
                sortBookIdsInOrders(ordersList);
                break;
            case 2:
                System.out.println("Sorting book IDs in descending order using SELECTION SORT...");
                System.out.println("Algorithm: Selection Sort | Time Complexity: O(n²) | Space: O(1)");
                sortBookIdsInOrdersDescending(ordersList);
                break;
            case 3:
                System.out.println("Sorting orders by book count using built-in sort...");
                System.out.println("Algorithm: TimSort (Java built-in) | Time Complexity: O(n log n) | Space: O(n)");
                sortOrdersByBookCount(ordersList);
                break;
            default:
                System.out.println("Invalid sort type. Using default INSERTION SORT (ascending).");
                sortBookIdsInOrders(ordersList);
                break;
        }
        System.out.println("=== SORTING COMPLETE ===");
    }
    
    /**
     * Filter orders based on user role and permissions
     * Admin: can sort all orders
     * Regular user: can only sort their own orders
     */
    private static ArrayList<Orders> getSortableOrders(ArrayList<Orders> allOrders) {
        if (!UserManager.isLoggedIn()) {
            return new ArrayList<>(); // Empty list if not logged in
        }
        
        if (UserManager.isCurrentUserAdmin()) {
            // Admin can sort all orders
            return new ArrayList<>(allOrders);
        } else {
            // Regular user can only sort their own orders
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
     * Utility method to display all orders and their book IDs
     * Respects user permissions - shows only accessible orders
     * @param ordersList List of orders to display
     */
    public static void displayOrdersWithBookIds(ArrayList<Orders> ordersList) {
        if (ordersList == null || ordersList.isEmpty()) {
            System.out.println("No orders to display.");
            return;
        }
        
        // Filter orders based on user permissions
        ArrayList<Orders> displayableOrders = getSortableOrders(ordersList);
        if (displayableOrders.isEmpty()) {
            if (UserManager.isCurrentUserAdmin()) {
                System.out.println("No orders found in the system.");
            } else {
                System.out.println("You have no orders to display.");
            }
            return;
        }
        
        if (UserManager.isCurrentUserAdmin()) {
            System.out.println("\n=== All Orders and Book IDs (Administrator View) ===");
        } else {
            System.out.println("\n=== Your Orders and Book IDs ===");
        }
        
        for (Orders order : displayableOrders) {
            System.out.println("Order ID: " + order.getOrderId());
            System.out.println("Customer: " + order.getCustomerName());
            System.out.println("Book IDs: " + order.getBookIds());
            System.out.println("Total Amount: " + order.getTotalAmount());
            System.out.println("Total Price: $" + order.getTotalPrice());
            System.out.println("---");
        }
    }
}