# Task Management Application

This project is a Java-based task management system built using queues and stacks.  
It includes both a console interface and a graphical interface (Swing).  
The goal of publishing this project is to allow others to read, improve, refactor, or expand the code.  
Contributions and better ideas are welcome.

---

## Overview

The application manages three categories of tasks:
- work  
- personal  
- shopping  

Each category maintains:
- a queue for pending tasks  
- a stack for completed tasks  

The program supports adding tasks, completing tasks, reordering tasks, undoing actions, viewing statistics, and printing tasks recursively.

Both interfaces (console and GUI) rely on the same core logic implemented in `TaskManager`.

---

## Features

### Add Tasks  
New tasks are appended to the pending queue of the selected category.

### Complete Tasks  
The oldest pending task in a category is removed from the queue and pushed to the finished stack.

### Reorder Tasks  
A task can be moved from one position to another within its pending queue.

### Undo  
The last action (complete or reorder) can be reverted.

### Statistics  
The program prints total pending tasks, finished tasks, and per-category counts.

### Recursive Listing  
Pending tasks of a category can be displayed using a recursive method.

### GUI  
A Swing-based interface is included for easier interaction.

---

## Project Files

src/
taskmgr/
Main.java
Task.java
TaskManager.java
TaskManagerGUI.java

