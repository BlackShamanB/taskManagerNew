package com.zubarev.taskmanager.taskmanager.controllers;

import com.zubarev.taskmanager.taskmanager.modal.Task;
import com.zubarev.taskmanager.taskmanager.service.TaskService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Controller
public class TaskController {
    private TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/getAllTasks")
    public @ResponseBody
    List<Task> getAllTasks(){
        return taskService.getAll();
    }



    @PostMapping("/addTask")
    public @ResponseBody
    Task create(@RequestBody Task task){
        return taskService.addTask(task);
    }



    @DeleteMapping("/deleteTask/{id}")
    public String delete(@PathVariable Long id, Model model){
        taskService.deleteTaskId(id);
        model.addAttribute("abc",taskService.getAll());
        return "index";
    }

    @PutMapping("/index")
    public String updateTask(@PathVariable Long id,@RequestBody Task task,Model model){
        task.setId(id);
        taskService.changeTask(task);
        model.addAttribute("abc",taskService.getAll());
        return "index";
    }



    @GetMapping("/index")
    public  String getAll(Model model){

        model.addAttribute("abc",taskService.getAll());
        return "index";
    }
    @PostMapping("/index")
    public String add(@RequestParam String taskName, @RequestParam String descriptionTask, @RequestParam Date date, @RequestParam String contacts, Model model){
        Task task=new Task(taskName,descriptionTask,date,contacts);
        taskService.addTask(task);
        model.addAttribute("abc",taskService.getAll());
        return "index";
    }
}
