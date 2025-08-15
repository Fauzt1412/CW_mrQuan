package models;

/**
 * Enum to define user roles in the system
 */
public enum UserRole {
    ADMIN("Administrator"),
    USER("Regular User");
    
    private final String displayName;
    
    UserRole(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    @Override
    public String toString() {
        return displayName;
    }
}