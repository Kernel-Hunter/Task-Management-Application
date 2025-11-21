package taskmgr;
import java.util.Scanner;

public class Main {
    private static final Scanner in = new Scanner(System.in);
    private static final TaskManager tm = new TaskManager();

    public static void main(String[] args) {
        System.out.println("Choose interface:");
        System.out.println("1) Graphical User Interface (GUI)");
        System.out.println("2) Console Interface");
        System.out.print("Choice: ");
        
        String choice = in.nextLine().strip();
        
        if (choice.equals("1")) {
            // Launch GUI
            TaskManagerGUI.main(args);
        } else {
            // Use console interface
            consoleMain();
        }
    }

    private static void consoleMain() {
        while (true) {
            menu();
            switch (in.nextLine().strip()) {
                case "1" -> addUI();
                case "2" -> completeUI();
                case "3" -> reorderUI();
                case "4" -> statsUI();
                case "5" -> undoUI();
                case "6" -> listUI();
                case "0" -> { System.out.println("Good-bye!"); return; }
                default  -> System.out.println("Invalid choice.");
            }
        }
    }

    private static void menu() {
        System.out.println("""
                ========== Task Manager ==========
                1) Add task
                2) Complete task
                3) Reorder tasks
                4) Statistics
                5) Undo last action
                6) List pending/done
                0) Exit
                > """);
    }

    private static void addUI() {
        System.out.print("Title: ");
        String title = in.nextLine();
        System.out.print("Category (work|personal|shopping): ");
        String cat = in.nextLine();
        tm.addTask(title, cat);
        System.out.println("Added.");
    }

    private static void completeUI() {
        System.out.print("Category to complete from: ");
        String cat = in.nextLine();
        boolean ok = tm.completeTask(cat);
        System.out.println(ok ? "Marked completed." : "Nothing to complete.");
    }

    private static void reorderUI() {
        System.out.print("Category: ");
        String cat = in.nextLine();
        System.out.print("Old index (0-based): ");
        int old = Integer.parseInt(in.nextLine().strip());
        System.out.print("New index: ");
        int nw = Integer.parseInt(in.nextLine().strip());
        boolean ok = tm.reorder(cat, old, nw);
        System.out.println(ok ? "Reordered." : "Invalid indices.");
    }

    private static void statsUI() { tm.printStats(); }

    private static void undoUI() {
        boolean ok = tm.undo();
        System.out.println(ok ? "Undone." : "Nothing to undo.");
    }

    private static void listUI() {
        System.out.print("Category (work|personal|shopping): ");
        String cat = in.nextLine();
        System.out.println("----- Pending -----");
        tm.printPending(cat);
        System.out.println("----- Finished -----");
        tm.printFinished(cat);
        System.out.println("----- Recursive pending -----");
        tm.printPendingRecursive(cat);
    }
}