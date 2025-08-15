package models;

public class books {
    private int bookid;
    private String title;
    private String author;
    private double price;
    private int quantity;

    public books(String title, String author, double price, int quantity, int bookid) {
        this.bookid = bookid;
        this.title = title;
        this.author = author;
        this.price = price;
        this.quantity = quantity;
    }
    public int getBookid(){
        return bookid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


}
