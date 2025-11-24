package taskmgr;
import java.util.*;
import java.time.LocalDate;

public class TaskManager {
    private final Map<String, PriorityQueue<Task>> pending = new HashMap<>();
    private final Map<String, ArrayDeque<Task>> finished = new HashMap<>();

    private record Action(String type, Task task, String category, int oldPos) {}
    private final ArrayDeque<Action> history = new ArrayDeque<>();

    public TaskManager() {
        for (String cat : List.of("work", "personal", "shopping")) {
            pending.put(cat, new PriorityQueue<>());
            finished.put(cat, new ArrayDeque<>());
        }
    }

    public void addTask(String title, String category) {
        addTask(title, category, null, Task.Priority.MEDIUM);
    }

    public void addTask(String title, String category, LocalDate deadline, Task.Priority priority) {
        Task t = new Task(title, category, deadline, priority);
        pending.get(t.getCategory()).offer(t);
        history.push(new Action("ADD", t, category, -1));
    }

    public boolean completeTask(String category) {
        PriorityQueue<Task> pq = pending.get(category);
        if (pq.isEmpty()) return false;
        Task t = pq.poll();
        finished.get(category).push(t);
        history.push(new Action("COMPLETE", t, category, -1));
        return true;
    }

    public boolean reorder(String category, int oldIndex, int newPos) {
        PriorityQueue<Task> pq = pending.get(category);
        
        List<Task> tasks = new ArrayList<>(pq);
        if (oldIndex < 0 || oldIndex >= tasks.size() || newPos < 0 || newPos > tasks.size()) {
            return false;
        }
        
        Task movedTask = tasks.get(oldIndex);
        
        tasks.remove(oldIndex);
        tasks.add(newPos, movedTask);
        
        pq.clear();
        pq.addAll(tasks);
        
        history.push(new Action("REORDER", movedTask, category, oldIndex));
        return true;
    }

    public List<Task> getOverdueTasks() {
        List<Task> overdue = new ArrayList<>();
        LocalDate today = LocalDate.now();
        
        for (String category : pending.keySet()) {
            for (Task task : pending.get(category)) {
                if (task.isOverdue()) {
                    overdue.add(task);
                }
            }
        }
        return overdue;
    }

    public List<Task> getTasksDueToday() {
        List<Task> dueToday = new ArrayList<>();
        LocalDate today = LocalDate.now();
        
        for (String category : pending.keySet()) {
            for (Task task : pending.get(category)) {
                if (task.getDeadline() != null && task.getDeadline().equals(today)) {
                    dueToday.add(task);
                }
            }
        }
        return dueToday;
    }

    public void printStats() {
        int totalPending = pending.values().stream().mapToInt(PriorityQueue::size).sum();
        int totalDone = finished.values().stream().mapToInt(ArrayDeque::size).sum();
        int overdueCount = getOverdueTasks().size();
        int dueTodayCount = getTasksDueToday().size();
        
        System.out.println("------------- Stats -------------");
        System.out.println("Pending : " + totalPending);
        System.out.println("Done    : " + totalDone);
        System.out.println("Total   : " + (totalPending + totalDone));
        System.out.println("Overdue : " + overdueCount);
        System.out.println("Due Today: " + dueTodayCount);
        
        for (String cat : pending.keySet()) {
            int catOverdue = (int) pending.get(cat).stream().filter(Task::isOverdue).count();
            System.out.printf("  %-9s -> pending: %2d (overdue: %d) done: %2d%n",
                    cat, pending.get(cat).size(), catOverdue, finished.get(cat).size());
        }
    }

    public boolean undo() {
        if (history.isEmpty()) return false;
        Action last = history.pop();
        
        switch (last.type) {
            case "COMPLETE":
                finished.get(last.category).pop();
                pending.get(last.category).offer(last.task);
                break;
            case "ADD":
                pending.get(last.category).remove(last.task);
                break;
            case "REORDER":
                PriorityQueue<Task> pq = pending.get(last.category);
                List<Task> tasks = new ArrayList<>(pq);
                pq.clear();
                int currentIndex = tasks.indexOf(last.task);
                if (currentIndex != -1) {
                    tasks.remove(currentIndex);
                    tasks.add(last.oldPos, last.task);
                    pq.addAll(tasks);
                }
                break;
        }
        return true;
    }

    public Task getNextTask(String category) {
        return pending.get(category).peek();
    }

    public void printPendingByPriority(String category) {
        List<Task> tasks = new ArrayList<>(pending.get(category));
        tasks.sort(Comparator.reverseOrder());
        
        System.out.println("--- " + category.toUpperCase() + " Tasks by Priority ---");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + ". " + tasks.get(i));
        }
    }

    public int getPendingCount(String category) {
        return pending.get(category).size();
    }

    public void printPending(String category) {
        List<Task> tasks = new ArrayList<>(pending.get(category));
        int i = 1;
        for (Task t : tasks) System.out.println(i++ + ". " + t);
    }
    
    public void printFinished(String category) {
        int i = 1;
        for (Task t : finished.get(category)) System.out.println(i++ + ". " + t);
    }

    public void printPendingRecursive(String category) {
        printRec(new ArrayDeque<>(pending.get(category)), 1);
    }
    
    private void printRec(ArrayDeque<Task> q, int level) {
        if (q.isEmpty()) return;
        Task t = q.pollFirst();
        System.out.println("  ".repeat(level - 1) + level + ". " + t);
        printRec(q, level + 1);
        q.offerFirst(t);
    }
}