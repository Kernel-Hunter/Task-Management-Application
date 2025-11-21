# Task Management Application

A simple task management application implemented in Java using queues and stacks.
This project was developed for the CS341 lab. The code is intentionally open for improvement. If you have better ideas, cleaner code, or new features, please edit, fork, or submit a pull request.

## Overview

The application implements a basic to-do system with three categories: `work`, `personal`, and `shopping`. Each category has one queue for pending tasks and one stack for finished tasks. The application supports both a console interface and a Swing GUI.

Key behaviours implemented in code
- Add task: new tasks are appended to the selected category queue.
- Complete task: oldest pending task in a category is moved to that category stack.
- Reorder task: remove a task by index and insert it at a new index in the queue.
- Undo: revert the last COMPLETE or REORDER action.
- Statistics: total pending and finished counts per category.
- Recursive view: a recursive routine prints pending tasks in order.

## Project files

