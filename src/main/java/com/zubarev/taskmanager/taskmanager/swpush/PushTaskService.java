package com.zubarev.taskmanager.taskmanager.swpush;

import com.zubarev.taskmanager.taskmanager.TaskmanagerApplication;
import com.zubarev.taskmanager.taskmanager.modal.Task;
import com.zubarev.taskmanager.taskmanager.service.TaskService;
import com.zubarev.taskmanager.taskmanager.swpush.fcm.FcmClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class PushTaskService {

    private final FcmClient fcmClient;

    private final WebClient webClient;

    private  final TaskService taskService;

    public PushTaskService(FcmClient fcmClient, WebClient webClient, TaskService taskService) {
        this.fcmClient = fcmClient;
        this.webClient = webClient;
        this.taskService = taskService;
    }

    private int seq = 0;


    @Scheduled(fixedDelay = 30_000)
    public void sendChuckQuotes() {
        Task task = new Task("aaa", "bbb", LocalDate.now(), LocalTime.now(), "ccc");

        try {
            sendPushMessage(task);
        } catch (InterruptedException | ExecutionException e) {
            TaskmanagerApplication.logger.error("send chuck joke", e);
        }
    }

    void sendPushMessage(Task task) throws InterruptedException, ExecutionException {
        Map<String, String> data = new HashMap<>();
        data.put("id", String.valueOf(task.getId()));
        data.put("taskName", task.getTaskName());
//        data.put("descriptionTask", task.getDescriptionTask());
//        data.put("date", String.valueOf(task.getDate()));
//        data.put("contacts", task.getContacts());
//        data.put("time", String.valueOf(task.getTime()));
        data.put("seq", String.valueOf(this.seq++));
        data.put("ts", String.valueOf(System.currentTimeMillis()));

        System.out.println("Sending chuck joke...");
        this.fcmClient.send(data);
    }
}
