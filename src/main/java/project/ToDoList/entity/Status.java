package project.ToDoList.entity;

public enum Status {
    CREATED("Created"),
    IN_PROGRESS("In progress"),
    OVERDUE("Overdue"),
    COMPLETED("Completed"),
    INACTIVE("Inactive");

    private final String displayName;

    Status(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}




