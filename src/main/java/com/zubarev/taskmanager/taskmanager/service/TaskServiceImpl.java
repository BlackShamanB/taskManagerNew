package com.zubarev.taskmanager.taskmanager.service;

import com.zubarev.taskmanager.taskmanager.modal.Task;
import com.zubarev.taskmanager.taskmanager.repos.TaskRepos;
import org.springframework.stereotype.Service;
import com.google.common.collect.Lists;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService{
    private final TaskRepos taskrepos;
    @PostConstruct
    public void initSomeData(){
        taskrepos.save(new Task("Task 1", "Desc 1", new Date(), "contacts 1"));
        taskrepos.save(new Task("Task 2", "Desc 2", new Date(), "contacts 2"));
        taskrepos.save(new Task("Task 3", "Desc 3", new Date(), "contacts 3"));
    }

    public TaskServiceImpl(TaskRepos taskrepos) {
        this.taskrepos = taskrepos;
    }

    @Override
    public Task addTask(Task task) {
        return  taskrepos.save(task);

    }

    @Override
    public void deleteTask(Task task) {
        taskrepos.delete(task);
    }

    @Override
    public Task changeTask(Task task) {
        return taskrepos.save(task);
    }

    @Override
    public List<Task> getAll() {
        return Lists.newArrayList(taskrepos.findAll());
    }

    @Override
    public Task getTask(Long id){
        Optional<Task> task = taskrepos.findById(id);
        return task.orElse(null);
    }

    @Override
    public void deleteTaskId(Long id) {
        taskrepos.deleteById(id);
    }

}
