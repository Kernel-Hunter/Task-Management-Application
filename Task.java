package taskmgr;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Task implements Comparable<Task> {
    private final String title;
    private final String category; // work | personal | shopping
    private final LocalDate deadline;
    private final Priority priority;

    public enum Priority {
        LOW(1), MEDIUM(2), HIGH(3), URGENT(4);
        
        private final int value;
        Priority(int value) { this.value = value; }
        public int getValue() { return value; }
    }

    public Task(String title, String category) {
        this(title, category, null, Priority.MEDIUM);
    }

    public Task(String title, String category, LocalDate deadline, Priority priority) {
        this.title = title.strip();
        this.category = category.strip().toLowerCase();
        this.deadline = deadline;
        this.priority = priority;
    }

    public String getTitle() { return title; }
    public String getCategory() { return category; }
    public LocalDate getDeadline() { return deadline; }
    public Priority getPriority() { return priority; }
    
    public boolean hasDeadline() { return deadline != null; }
    public boolean isOverdue() { 
        return deadline != null && deadline.isBefore(LocalDate.now()); 
    }

    @Override
    public String toString() {
        String base = "[" + category + "] " + title;
        if (deadline != null) {
            String deadlineStr = deadline.format(DateTimeFormatter.ofPattern("MMM dd"));
            String priorityStr = priority.name().charAt(0) + "";
            if (isOverdue()) {
                return base + " ⏰ " + deadlineStr + " (OVERDUE!) [" + priorityStr + "]";
            }
            return base + " ⏰ " + deadlineStr + " [" + priorityStr + "]";
        }
        return base + " [" + priority.name().charAt(0) + "]";
    }

    @Override
    public int compareTo(Task other) {
        if (this.deadline != null && other.deadline != null) {
            int dateCompare = this.deadline.compareTo(other.deadline);
            if (dateCompare != 0) return dateCompare;
        } else if (this.deadline != null) {
            return -1;
        } else if (other.deadline != null) {
            return 1;
        }
        
        return Integer.compare(other.priority.getValue(), this.priority.getValue());
    }
}