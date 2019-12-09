package com.zubarev.taskmanager.taskmanager.controllers;

import com.zubarev.taskmanager.taskmanager.modal.Task;
import com.zubarev.taskmanager.taskmanager.service.TaskService;
import org.checkerframework.checker.units.qual.Time;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Controller
public class TaskController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping(value = "/tasks")
    public String getAll(Model model) {

        model.addAttribute("tasks", taskService.getAll());
        return "taskList";
    }

    @PostMapping("tasks/add")
    //public String add(@RequestParam String taskName, @RequestParam String descriptionTask, @RequestParam String date, @RequestParam String time, @RequestParam String contacts, Model model) {
    public String addTask(Model model, @ModelAttribute("tasks") Task task) {
       try {
//           LocalDate date1= LocalDate.of(Integer.parseInt(date.substring(0,4)),Integer.parseInt(date.substring(6,7)),Integer.parseInt(date.substring(9,10)));
//           LocalTime time1= LocalTime.of(Integer.parseInt(time.substring(0,1)),Integer.parseInt(time.substring(3,4)));
//           Task newTask=new Task(taskName,descriptionTask,date1,time1,contacts);
           Task newTask =taskService.addTask(task);
           return "redirect:/tasks/" + newTask.getId();
       }
       catch (Exception ex){
           String errorMessage=ex.getMessage();
           logger.error(errorMessage);
           model.addAttribute("errorMessage",errorMessage);

           model.addAttribute("add",true);
           return "taskEdit";
       }
    }

    @GetMapping(value = {"/tasks/add"})
    public String showAddTask(Model model) {
            Task task = new Task("","",LocalDate.now(),LocalTime.now(),"");
        model.addAttribute("add", true);
        model.addAttribute("tasks", task);

        return "taskEdit";
    }


    @GetMapping(value = {"/tasks/{taskId}/edit"})
    public String showEditTask(Model model, @PathVariable long taskId) {
        Task task = null;
        try {
            task = taskService.getTask(taskId);
        } catch (Exception ex) {
            model.addAttribute("errorMessage", ex.getMessage());
        }
        model.addAttribute("add", false);
        model.addAttribute("tasks", task);
        return "taskEdit";
    }

    @PostMapping(value = {"/tasks/{taskId}/edit"})
    public String updateTask(@ModelAttribute("tasks") Task task,Model model,
                             @PathVariable long taskId) {
        try {
            task.setId(taskId);
            taskService.changeTask(task);
            return "redirect:/tasks/" + task.getId();
        } catch (Exception ex) {
            String errorMessage = ex.getMessage();
            logger.error(errorMessage);
            model.addAttribute("errorMessage", errorMessage);

            model.addAttribute("add", false);
            return "taskEdit";
        }
    }

    @GetMapping(value = {"/tasks/{taskId}/delete"})
    public String showDeleteTask(
            Model model, @PathVariable long taskId) {
        Task task = null;
        try {
            task = taskService.getTask(taskId);
        } catch (Exception ex) {
            model.addAttribute("errorMessage", ex.getMessage());
        }
        model.addAttribute("allowDelete", true);
        model.addAttribute("tasks", task);
        return "task";
    }

    @PostMapping(value = {"/tasks/{taskId}/delete"})
    public String deleteTaskById(
            Model model, @PathVariable long taskId) {
        try {
            taskService.deleteTaskId(taskId);
            return "redirect:/tasks";
        } catch (Exception ex) {
            String errorMessage = ex.getMessage();
            logger.error(errorMessage);
            model.addAttribute("errorMessage", errorMessage);
            return "task";
        }
    }

    @GetMapping(value = "/tasks/{taskId}")
    public String getTaskById(Model model, @PathVariable String taskId) {
        Task task = null;
        try {
            task = taskService.getTask(Long.parseLong(taskId));
            model.addAttribute("allowDelete", false);
        } catch (Exception ex) {
            model.addAttribute("errorMessage", ex.getMessage());
        }
        model.addAttribute("tasks", task);
        return "task";
    }
}
