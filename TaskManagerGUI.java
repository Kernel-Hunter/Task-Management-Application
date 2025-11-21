package taskmgr;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TaskManagerGUI {
    private TaskManager tm;
    private JFrame frame;
    private JComboBox<String> categoryCombo;
    private JTextArea outputArea;
    private JTextField titleField;
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

        outputArea = new JTextArea(20, 40);
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        outputArea.setBorder(BorderFactory.createTitledBorder("Output"));
        JScrollPane scrollPane = new JScrollPane(outputArea);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        frame.add(mainPanel, BorderLayout.CENTER);
        frame.pack();
        frame.setLocationRelativeTo(null); 
        frame.setVisible(true);
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

        JButton addButton = new JButton("Add Task");
        addButton.addActionListener(new AddTaskListener());
        addTaskPanel.add(addButton);

        panel.add(addTaskPanel);
        panel.add(Box.createVerticalStrut(10));

        JButton completeButton = new JButton("Complete Task");
        completeButton.addActionListener(new CompleteTaskListener());
        panel.add(completeButton);
        panel.add(Box.createVerticalStrut(10));

        JPanel reorderPanel = new JPanel();
        reorderPanel.setLayout(new BoxLayout(reorderPanel, BoxLayout.Y_AXIS));
        reorderPanel.setBorder(BorderFactory.createTitledBorder("Reorder Tasks"));

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

        JButton reorderButton = new JButton("Reorder");
        reorderButton.addActionListener(new ReorderListener());
        reorderPanel.add(reorderButton);

        panel.add(reorderPanel);
        panel.add(Box.createVerticalStrut(10));

        JButton statsButton = new JButton("Show Statistics");
        statsButton.addActionListener(new StatsListener());
        panel.add(statsButton);
        panel.add(Box.createVerticalStrut(5));

        JButton undoButton = new JButton("Undo Last Action");
        undoButton.addActionListener(new UndoListener());
        panel.add(undoButton);
        panel.add(Box.createVerticalStrut(5));

        JButton listButton = new JButton("List Tasks");
        listButton.addActionListener(new ListListener());
        panel.add(listButton);
        panel.add(Box.createVerticalStrut(5));

        JButton clearButton = new JButton("Clear Output");
        clearButton.addActionListener(e -> outputArea.setText(""));
        panel.add(clearButton);

        return panel;
    }

    private void appendOutput(String text) {
        outputArea.append(text + "\n");
        outputArea.setCaretPosition(outputArea.getDocument().getLength());
    }

    private class AddTaskListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String title = titleField.getText().trim();
            String category = (String) categoryCombo.getSelectedItem();
            
            if (title.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please enter a task title", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            tm.addTask(title, category);
            appendOutput("✓ Added task: [" + category + "] " + title);
            titleField.setText("");
        }
    }

    private class CompleteTaskListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String category = (String) categoryCombo.getSelectedItem();
            boolean success = tm.completeTask(category);
            
            if (success) {
                appendOutput("✓ Completed a task from " + category + " category");
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
            
            boolean success = tm.reorder(category, oldIndex, newIndex);
            
            if (success) {
                appendOutput("✓ Reordered task in " + category + " category: " + 
                           oldIndex + " → " + newIndex);
            } else {
                appendOutput("✗ Invalid indices for reordering");
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
                appendOutput("✓ Undone last action");
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