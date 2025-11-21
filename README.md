# Task Management Application

This project is a task-management system implemented using stacks and queues.  
It was created for educational purposes to demonstrate data-structure concepts through task handling, completion tracking, reordering, and basic statistics.

I am publishing this project to GitHub so that others can improve it, reorganize it, or suggest better ideas. Anyone is free to edit the code, submit changes, or propose new features.

---

## Features

### 1. Task Addition
Tasks are added to a queue, representing the order in which they should be completed.

### 2. Task Completion
When a task is marked as completed, it is removed from the queue and pushed onto a stack. This allows the user to review completed tasks later.

### 3. Task Reordering
Tasks can be removed and reinserted at selected positions in the queue to adjust priorities.

### 4. Task Statistics
The program can display basic statistics such as:
- Number of remaining tasks  
- Number of completed tasks  
- Other relevant metrics  

### 5. Undo Operation
The system supports undoing the most recent action, such as reverting a task that was mistakenly marked as completed.

### 6. Task Categories
Tasks may be assigned categories (for example: work, personal, shopping).  
Each category is managed using its own queue and stack.

### 7. Recursion
A recursive method is integrated into one of the features to reinforce understanding of recursion.

---

## Optional Extensions
The following are not required but can be added to improve the application:
- Graphical user interface  
- Priority queue for tasks with deadlines  
- Data persistence across sessions  

---

## How to Run

1. Clone the repository:
   ```bash
   git clone https://github.com/Kernel-Hunter/task-management-application
