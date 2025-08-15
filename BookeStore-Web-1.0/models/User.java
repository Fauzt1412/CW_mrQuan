package models;

public class User {
    private String username;
    private String password;
    private String displayName;
    private String email;
    private UserRole role;

    public User(String username, String password, String displayName, String email, UserRole role) {
        this.username = username;
        this.password = password;
        this.displayName = displayName;
        this.email = email;
        this.role = role;
    }
    
    // Constructor for backward compatibility (defaults to USER role)
    public User(String username, String password, String displayName, String email) {
        this(username, password, displayName, email, UserRole.USER);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }
    
    public boolean isAdmin() {
        return role == UserRole.ADMIN;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", displayName='" + displayName + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                '}';
    }
}