package com.zubarev.taskmanager.taskmanager.service;

import com.zubarev.taskmanager.taskmanager.modal.Task;

import java.util.List;

public interface TaskService {
    public Task addTask(Task task);
    public void deleteTask(Task task);
    public Task changeTask(Task task);
    public List<Task> getAll();
    public Task getTask(Long id);
    public void deleteTaskId(Long id);
}
