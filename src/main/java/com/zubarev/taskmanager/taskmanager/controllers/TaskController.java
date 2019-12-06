package com.zubarev.taskmanager.taskmanager.controllers;

import com.zubarev.taskmanager.taskmanager.modal.Task;
import com.zubarev.taskmanager.taskmanager.service.TaskService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Controller
public class TaskController {
    private TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }


    @PostMapping("delete")
    public String delete(@RequestParam Long id, Model model){
        taskService.deleteTaskId(id);
        model.addAttribute("abc",taskService.getAll());
        return "index";
    }

    @PostMapping("change")
    public String change(@RequestParam String id,@RequestParam String taskName, @RequestParam String descriptionTask, @RequestParam String date, @RequestParam String contacts,Model model){
        Date date1=new Date(Integer.parseInt(date.substring(0,3)),Integer.parseInt(date.substring(5,6)),Integer.parseInt(date.substring(8,9)));
        Task task=new Task(taskName,descriptionTask,date1,contacts);
        task.setId(Long.parseLong(id));
        taskService.changeTask(task);
        model.addAttribute("abc",taskService.getAll());
        return "index";
    }


    @GetMapping
    public String getAll(Model model) {

        model.addAttribute("abc", taskService.getAll());
        return "index";
    }

    @PostMapping("add")
    public String add(@RequestParam String taskName, @RequestParam String descriptionTask, @RequestParam String date, @RequestParam String contacts, Model model) {
        System.out.println(date);

        Date date1 = new Date(Integer.parseInt(date.substring(0, 3)), Integer.parseInt(date.substring(5, 6)), Integer.parseInt(date.substring(8, 9)));
        Task task = new Task(taskName, descriptionTask, date1, contacts);
        taskService.addTask(task);
        model.addAttribute("abc", taskService.getAll());
        return "index";
    }
}
