package taskmgr;
public class Task {
    private final String title;
    private final String category; // work | personal | shopping

    public Task(String title, String category) {
        this.title = title.strip();
        this.category = category.strip().toLowerCase();
    }
    public String getTitle()  { return title; }
    public String getCategory() { return category; }
    @Override
    public String toString() { return "[" + category + "] " + title; }
}