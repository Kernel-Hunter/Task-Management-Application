package taskmgr;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class TaskManagerGUI {
    private TaskManager tm;
    private JFrame frame;
    private JComboBox<String> categoryCombo;
    private JComboBox<Task.Priority> priorityCombo;
    private JTextArea outputArea;
    private JTextField titleField;
    private JTextField deadlineField;
    private JSpinner oldIndexSpinner;
    private JSpinner newIndexSpinner;

    public TaskManagerGUI() {
        tm = new TaskManager();
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Task Management Application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10));
        frame.getContentPane().setBackground(Color.WHITE);

        JLabel headerLabel = new JLabel("Task Manager", JLabel.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setForeground(new Color(0, 102, 204));
        headerLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
        frame.add(headerLabel, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel controlPanel = createControlPanel();
        mainPanel.add(controlPanel, BorderLayout.WEST);

        outputArea = new JTextArea(20, 50);
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        outputArea.setBorder(BorderFactory.createTitledBorder("Output"));
        JScrollPane scrollPane = new JScrollPane(outputArea);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        frame.add(mainPanel, BorderLayout.CENTER);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        
        appendOutput("🚀 Task Manager Started!");
        appendOutput("💡 Features: Priority Queues, Deadlines, Undo, Categories");
        appendOutput("");
    }

    private JPanel createControlPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder("Controls"));

        JPanel categoryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        categoryPanel.add(new JLabel("Category:"));
        String[] categories = {"work", "personal", "shopping"};
        categoryCombo = new JComboBox<>(categories);
        categoryPanel.add(categoryCombo);
        panel.add(categoryPanel);

        panel.add(Box.createVerticalStrut(10));

        JPanel addTaskPanel = new JPanel();
        addTaskPanel.setLayout(new BoxLayout(addTaskPanel, BoxLayout.Y_AXIS));
        addTaskPanel.setBorder(BorderFactory.createTitledBorder("Add Task"));

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.add(new JLabel("Title:"));
        titleField = new JTextField(15);
        titlePanel.add(titleField);
        addTaskPanel.add(titlePanel);

        JPanel deadlinePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        deadlinePanel.add(new JLabel("Deadline (YYYY-MM-DD):"));
        deadlineField = new JTextField(10);
        deadlineField.setToolTipText("Optional: Format YYYY-MM-DD");
        deadlinePanel.add(deadlineField);
        addTaskPanel.add(deadlinePanel);

        JPanel priorityPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        priorityPanel.add(new JLabel("Priority:"));
        priorityCombo = new JComboBox<>(Task.Priority.values());
        priorityPanel.add(priorityCombo);
        addTaskPanel.add(priorityPanel);

        JButton addButton = new JButton("Add Task");
        addButton.addActionListener(new AddTaskListener());
        addTaskPanel.add(addButton);

        panel.add(addTaskPanel);
        panel.add(Box.createVerticalStrut(10));

        JPanel quickPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        quickPanel.setBorder(BorderFactory.createTitledBorder("Quick Actions"));
        
        JButton completeButton = new JButton("Complete Task");
        completeButton.addActionListener(new CompleteTaskListener());
        quickPanel.add(completeButton);

        JButton nextTaskButton = new JButton("Show Next Task");
        nextTaskButton.addActionListener(e -> showNextTask());
        quickPanel.add(nextTaskButton);

        JButton overdueButton = new JButton("Show Overdue");
        overdueButton.addActionListener(e -> showOverdueTasks());
        quickPanel.add(overdueButton);

        JButton dueTodayButton = new JButton("Due Today");
        dueTodayButton.addActionListener(e -> showDueToday());
        quickPanel.add(dueTodayButton);

        JButton reorderButton = new JButton("Reorder Tasks");
        reorderButton.addActionListener(new ReorderListener());
        quickPanel.add(reorderButton);

        JButton listByPriorityButton = new JButton("List by Priority");
        listByPriorityButton.addActionListener(e -> listByPriority());
        quickPanel.add(listByPriorityButton);

        panel.add(quickPanel);
        panel.add(Box.createVerticalStrut(10));

        JPanel reorderPanel = new JPanel();
        reorderPanel.setLayout(new BoxLayout(reorderPanel, BoxLayout.Y_AXIS));
        reorderPanel.setBorder(BorderFactory.createTitledBorder("Manual Reorder"));

        JPanel indexPanel1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        indexPanel1.add(new JLabel("Old Index:"));
        oldIndexSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
        indexPanel1.add(oldIndexSpinner);
        reorderPanel.add(indexPanel1);

        JPanel indexPanel2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        indexPanel2.add(new JLabel("New Index:"));
        newIndexSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
        indexPanel2.add(newIndexSpinner);
        reorderPanel.add(indexPanel2);

        panel.add(reorderPanel);
        panel.add(Box.createVerticalStrut(10));

        JPanel advancedPanel = new JPanel();
        advancedPanel.setLayout(new BoxLayout(advancedPanel, BoxLayout.Y_AXIS));
        advancedPanel.setBorder(BorderFactory.createTitledBorder("Advanced"));

        JButton statsButton = new JButton("Show Statistics");
        statsButton.addActionListener(new StatsListener());
        advancedPanel.add(statsButton);
        advancedPanel.add(Box.createVerticalStrut(5));

        JButton undoButton = new JButton("Undo Last Action");
        undoButton.addActionListener(new UndoListener());
        advancedPanel.add(undoButton);
        advancedPanel.add(Box.createVerticalStrut(5));

        JButton listButton = new JButton("List All Tasks");
        listButton.addActionListener(new ListListener());
        advancedPanel.add(listButton);
        advancedPanel.add(Box.createVerticalStrut(5));

        JButton clearButton = new JButton("Clear Output");
        clearButton.addActionListener(e -> outputArea.setText(""));
        advancedPanel.add(clearButton);

        panel.add(advancedPanel);

        return panel;
    }

    private void appendOutput(String text) {
        outputArea.append(text + "\n");
        outputArea.setCaretPosition(outputArea.getDocument().getLength());
    }

    private void showNextTask() {
        String category = (String) categoryCombo.getSelectedItem();
        Task next = tm.getNextTask(category);
        if (next != null) {
            appendOutput("⭐ Next task in " + category + ": " + next);
        } else {
            appendOutput("✅ No pending tasks in " + category);
        }
    }

    private void showOverdueTasks() {
        java.util.List<Task> overdue = tm.getOverdueTasks();
        appendOutput("🔴 OVERDUE TASKS (" + overdue.size() + "):");
        if (overdue.isEmpty()) {
            appendOutput("  No overdue tasks! 🎉");
        } else {
            for (int i = 0; i < overdue.size(); i++) {
                appendOutput("  " + (i + 1) + ". " + overdue.get(i));
            }
        }
    }

    private void showDueToday() {
        java.util.List<Task> dueToday = tm.getTasksDueToday();
        appendOutput("📅 TASKS DUE TODAY (" + dueToday.size() + "):");
        if (dueToday.isEmpty()) {
            appendOutput("  No tasks due today! 🎉");
        } else {
            for (int i = 0; i < dueToday.size(); i++) {
                appendOutput("  " + (i + 1) + ". " + dueToday.get(i));
            }
        }
    }

    private void listByPriority() {
        String category = (String) categoryCombo.getSelectedItem();
        appendOutput("📊 TASKS BY PRIORITY - " + category.toUpperCase());
        
        java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
        java.io.PrintStream ps = new java.io.PrintStream(baos);
        java.io.PrintStream old = System.out;
        System.setOut(ps);
        
        tm.printPendingByPriority(category);
        
        System.out.flush();
        System.setOut(old);
        appendOutput(baos.toString());
    }

    private class AddTaskListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String title = titleField.getText().trim();
            String category = (String) categoryCombo.getSelectedItem();
            String deadlineStr = deadlineField.getText().trim();
            Task.Priority priority = (Task.Priority) priorityCombo.getSelectedItem();
            
            if (title.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please enter a task title", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            LocalDate deadline = null;
            if (!deadlineStr.isEmpty()) {
                try {
                    deadline = LocalDate.parse(deadlineStr);
                    if (deadline.isBefore(LocalDate.now())) {
                        int result = JOptionPane.showConfirmDialog(frame, 
                            "This deadline is in the past. Continue anyway?", 
                            "Past Deadline", JOptionPane.YES_NO_OPTION);
                        if (result != JOptionPane.YES_OPTION) {
                            return;
                        }
                    }
                } catch (DateTimeParseException ex) {
                    JOptionPane.showMessageDialog(frame, 
                        "Invalid date format. Use YYYY-MM-DD", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            
            tm.addTask(title, category, deadline, priority);
            String message = "✓ Added: " + title;
            if (deadline != null) {
                message += " (Due: " + deadline.format(DateTimeFormatter.ofPattern("MMM dd")) + ")";
            }
            message += " [" + priority + "]";
            appendOutput(message);
            
            titleField.setText("");
            deadlineField.setText("");
        }
    }

    private class CompleteTaskListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String category = (String) categoryCombo.getSelectedItem();
            Task nextTask = tm.getNextTask(category);
            
            if (nextTask != null) {
                int confirm = JOptionPane.showConfirmDialog(frame,
                    "Complete this task?\n" + nextTask,
                    "Confirm Completion", JOptionPane.YES_NO_OPTION);
                
                if (confirm == JOptionPane.YES_OPTION) {
                    boolean success = tm.completeTask(category);
                    if (success) {
                        appendOutput("✅ Completed: " + nextTask.getTitle());
                    }
                }
            } else {
                appendOutput("✗ No pending tasks in " + category + " category");
            }
        }
    }

    private class ReorderListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String category = (String) categoryCombo.getSelectedItem();
            int oldIndex = (Integer) oldIndexSpinner.getValue();
            int newIndex = (Integer) newIndexSpinner.getValue();
            
            int taskCount = tm.getPendingCount(category);
            ((SpinnerNumberModel) oldIndexSpinner.getModel()).setMaximum(Math.max(0, taskCount - 1));
            ((SpinnerNumberModel) newIndexSpinner.getModel()).setMaximum(Math.max(0, taskCount));
            
            boolean success = tm.reorder(category, oldIndex, newIndex);
            
            if (success) {
                appendOutput("↷ Reordered task in " + category + " category: " + 
                           oldIndex + " → " + newIndex);
                listByPriority();
            } else {
                appendOutput("✗ Invalid indices for reordering. Max index: " + (taskCount - 1));
            }
        }
    }

    private class StatsListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            appendOutput("=== STATISTICS ===");
            java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
            java.io.PrintStream ps = new java.io.PrintStream(baos);
            java.io.PrintStream old = System.out;
            System.setOut(ps);
            
            tm.printStats();
            
            System.out.flush();
            System.setOut(old);
            appendOutput(baos.toString());
            appendOutput("==================");
        }
    }

    private class UndoListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            boolean success = tm.undo();
            if (success) {
                appendOutput("↶ Undone last action");
            } else {
                appendOutput("✗ Nothing to undo");
            }
        }
    }

    private class ListListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String category = (String) categoryCombo.getSelectedItem();
            
            appendOutput("=== TASKS FOR: " + category.toUpperCase() + " ===");
            appendOutput("--- Pending ---");
            
            java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
            java.io.PrintStream ps = new java.io.PrintStream(baos);
            java.io.PrintStream old = System.out;
            System.setOut(ps);
            
            tm.printPending(category);
            
            System.out.flush();
            System.setOut(old);
            appendOutput(baos.toString());
            
            appendOutput("--- Finished ---");
            
            baos = new java.io.ByteArrayOutputStream();
            ps = new java.io.PrintStream(baos);
            old = System.out;
            System.setOut(ps);
            
            tm.printFinished(category);
            
            System.out.flush();
            System.setOut(old);
            appendOutput(baos.toString());
            
            appendOutput("--- Recursive View ---");
            
            baos = new java.io.ByteArrayOutputStream();
            ps = new java.io.PrintStream(baos);
            old = System.out;
            System.setOut(ps);
            
            tm.printPendingRecursive(category);
            
            System.out.flush();
            System.setOut(old);
            appendOutput(baos.toString());
            appendOutput("=====================");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TaskManagerGUI());
    }
}