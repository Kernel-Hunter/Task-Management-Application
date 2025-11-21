# Task Management Application

This project is a Java-based task management system that uses queues and stacks to organize tasks.
It includes both a console interface and a graphical interface (Swing).
The project is published on GitHub so others can improve it, refactor it, or add new features.  
Contributions and better ideas are welcome.

---

## Overview

The application manages three categories of tasks:

- work  
- personal  
- shopping  

Each category contains:
- a queue for pending tasks  
- a stack for finished tasks  

The program supports adding tasks, completing tasks, reordering tasks, undoing actions, printing statistics, and printing tasks recursively.

Both the console mode and the GUI use the same core logic implemented in `TaskManager.java`.

---

## Project Files

```text
src/
  taskmgr/
    Main.java
    Task.java
    TaskManager.java
    TaskManagerGUI.java
