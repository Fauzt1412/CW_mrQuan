package models;

import java.util.ArrayList;
import java.util.List;

public class Orders {
    private String orderId;
    private String customerName;
    private String shippingAddress; // Added as per requirement specification
    private String bookTitle;
    private int TotalAmount;
    private double TotalPrice;
    private List<Integer> bookIds; // List to store book IDs in this order

    public Orders(String orderId, String customerName, String shippingAddress, String bookTitle, int TotalAmount, double TotalPrice) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.shippingAddress = shippingAddress;
        this.bookTitle = bookTitle;
        this.TotalAmount = TotalAmount;
        this.TotalPrice = TotalPrice;
        this.bookIds = new ArrayList<>();
    }
    
    // Constructor for backward compatibility
    public Orders(String orderId, String customerName, String bookTitle, int TotalAmount, double TotalPrice) {
        this(orderId, customerName, "Default Address", bookTitle, TotalAmount, TotalPrice);
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }
    public int getTotalAmount() {
        return TotalAmount;
    }
    public void setTotalAmount(int TotalAmount) {
        this.TotalAmount = TotalAmount;
    }
    public double getTotalPrice() {
        return TotalPrice;
    }
    public void setTotalPrice(double TotalPrice) {
        this.TotalPrice = TotalPrice;
    }

    public List<Integer> getBookIds() {
        return bookIds;
    }

    public void setBookIds(List<Integer> bookIds) {
        this.bookIds = bookIds;
    }

    public void addBookId(int bookId) {
        this.bookIds.add(bookId);
    }

    public void removeBookId(int bookId) {
        this.bookIds.remove(Integer.valueOf(bookId));
    }

}
