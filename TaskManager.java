package taskmgr;
import java.util.*;

public class TaskManager {
    /* One queue + one stack per category */
    private final Map<String, ArrayDeque<Task>> pending = new HashMap<>();
    private final Map<String, ArrayDeque<Task>> finished = new HashMap<>();

    /* For undo */
    private record Action(String type, Task task, String category, int oldPos) {}
    private final ArrayDeque<Action> history = new ArrayDeque<>();

    public TaskManager() {
        for (String cat : List.of("work", "personal", "shopping")) {
            pending.put(cat, new ArrayDeque<>());
            finished.put(cat, new ArrayDeque<>());
        }
    }

    /* 1. Add task */
    public void addTask(String title, String category) {
        Task t = new Task(title, category);
        pending.get(t.getCategory()).offerLast(t);
    }

    /* 2. Complete task (oldest first) */
    public boolean completeTask(String category) {
        ArrayDeque<Task> q = pending.get(category);
        if (q.isEmpty()) return false;
        Task t = q.pollFirst();
        finished.get(category).push(t);
        history.push(new Action("COMPLETE", t, category, -1));
        return true;
    }

    /* 3. Reorder: remove index i (0-based) and insert at newPos */
    public boolean reorder(String category, int oldIndex, int newPos) {
        ArrayDeque<Task> q = pending.get(category);
        if (oldIndex < 0 || oldIndex >= q.size() || newPos < 0 || newPos > q.size())
            return false;
        /* dump to list */
        List<Task> tmp = new ArrayList<>(q);
        q.clear();
        Task t = tmp.remove(oldIndex);
        tmp.add(newPos, t);
        q.addAll(tmp);
        history.push(new Action("REORDER", t, category, oldIndex));
        return true;
    }

    /* 4. Statistics */
    public void printStats() {
        int totalPending = pending.values().stream().mapToInt(ArrayDeque::size).sum();
        int totalDone    = finished.values().stream().mapToInt(ArrayDeque::size).sum();
        System.out.println("------------- Stats -------------");
        System.out.println("Pending : " + totalPending);
        System.out.println("Done    : " + totalDone);
        System.out.println("Total   : " + (totalPending + totalDone));
        for (String cat : pending.keySet()) {
            System.out.printf("  %-9s -> pending: %2d  done: %2d%n",
                    cat, pending.get(cat).size(), finished.get(cat).size());
        }
    }

    /* 5. Undo last COMPLETE or REORDER */
    public boolean undo() {
        if (history.isEmpty()) return false;
        Action last = history.pop();
        if (last.type.equals("COMPLETE")) {
            finished.get(last.category).pop();
            pending.get(last.category).offerFirst(last.task);
        } else { // REORDER
            ArrayDeque<Task> q = pending.get(last.category);
            List<Task> tmp = new ArrayList<>(q);
            q.clear();
            tmp.remove(last.oldPos);          // remove from newPos
            tmp.add(last.oldPos, last.task);  // put back to oldPos
            q.addAll(tmp);
        }
        return true;
    }

    /* 6. Recursive print (depth first) */
    public void printPendingRecursive(String category) {
        printRec(new ArrayDeque<>(pending.get(category)), 1);
    }
    private void printRec(ArrayDeque<Task> q, int level) {
        if (q.isEmpty()) return;
        Task t = q.pollFirst();
        System.out.println("  ".repeat(level - 1) + level + ". " + t);
        printRec(q, level + 1);
        q.offerFirst(t); // restore (optional, we work on a copy)
    }

    /* Helpers for UI */
    public void printPending(String category) {
        int i = 1;
        for (Task t : pending.get(category)) System.out.println(i++ + ". " + t);
    }
    public void printFinished(String category) {
        int i = 1;
        for (Task t : finished.get(category)) System.out.println(i++ + ". " + t);
    }
}