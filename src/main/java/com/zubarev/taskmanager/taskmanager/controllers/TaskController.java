package com.zubarev.taskmanager.taskmanager.controllers;

import com.zubarev.taskmanager.taskmanager.modal.Task;
import com.zubarev.taskmanager.taskmanager.service.TaskService;
import com.zubarev.taskmanager.taskmanager.swpush.fcm.FcmClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalTime;

@Controller
public class TaskController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private TaskService taskService;
    private final FcmClient fcmClient;

    public TaskController(TaskService taskService, FcmClient fcmClient) {
        this.taskService = taskService;
        this.fcmClient = fcmClient;
    }


    @PostMapping("/register")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> register(@RequestBody Mono<String> token) {
        return token.doOnNext(t -> this.fcmClient.subscribe("chuck", t)).then();
    }

    @GetMapping(value = "/tasks")
    public String getAll(Model model) {

        model.addAttribute("tasks", taskService.getAll());
        return "taskList";
    }

    @PostMapping("tasks/add")
    public String addTask(Model model, @ModelAttribute("tasks") Task task) {
        try {
            Task newTask = taskService.addTask(task);
            return "redirect:/tasks/" + newTask.getId();
        } catch (Exception ex) {
            String errorMessage = ex.getMessage();
            logger.error(errorMessage);
            model.addAttribute("errorMessage", errorMessage);

            model.addAttribute("add", true);
            return "taskEdit";
        }
    }

    @GetMapping(value = {"/tasks/add"})
    public String showAddTask(Model model) {
        Task task = new Task("", "", LocalDate.now(), LocalTime.now(), "");
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
    public String updateTask(@ModelAttribute("tasks") Task task, Model model,
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
