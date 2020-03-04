package com.zubarev.taskmanager.taskmanager.swpush.fcm;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.*;
import com.zubarev.taskmanager.taskmanager.TaskmanagerApplication;
import com.zubarev.taskmanager.taskmanager.swpush.FcmSettings;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class FcmClient {

    public FcmClient(FcmSettings settings) {
        Path p = Paths.get(settings.getServiceAccountFile());
        try (InputStream serviceAccount = Files.newInputStream(p)) {
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount)).build();

            FirebaseApp.initializeApp(options);
        } catch (IOException e) {
            TaskmanagerApplication.logger.error("init fcm", e);
        }
    }

    public String send(Map<String, String> data)
            throws InterruptedException, ExecutionException {

        Message message = Message.builder().putAllData(data).setTopic("chuck")
                .setWebpushConfig(WebpushConfig.builder().putHeader("ttl", "300")
                        .setNotification(new WebpushNotification(data.get("taskName"),
                                data.get("descriptionTask")+" "+data.get("contacts"), "mail2.png"))
                        .build())
                .build();
        String response = FirebaseMessaging.getInstance().sendAsync(message).get();
        System.out.println("Sent message: " + response);
        return response;
    }

    public String sendPersonal(Map<String, String> data, String clientToken)
            throws ExecutionException, InterruptedException {
        Message message = Message.builder().setToken(clientToken)
                .setWebpushConfig(WebpushConfig.builder().putHeader("ttl", "300")
                        .setNotification(new WebpushNotification(data.get("taskName"),
                                data.get("descriptionTask")+" "+data.get("contacts"), "mail2.png"))
                        .build())
                .build();
        String response = FirebaseMessaging.getInstance().sendAsync(message).get();
        return response;
    }

    public void subscribe(String topic, String clientToken) {
        try {
            TopicManagementResponse response = FirebaseMessaging.getInstance()
                    .subscribeToTopicAsync(Collections.singletonList(clientToken), topic).get();
            System.out
                    .println(response.getSuccessCount() + " tokens were subscribed successfully");
        } catch (InterruptedException | ExecutionException e) {
            TaskmanagerApplication.logger.error("subscribe", e);
        }
    }
    private WebpushNotification.Builder createBuilder(Map<String, String> data){
        WebpushNotification.Builder builder = WebpushNotification.builder();
        builder.addAction(new WebpushNotification
                .Action(data.get("click_action"), "Открыть"))
                .setImage("mail2.png")
                .setTitle(data.get("descriptionTask"))
                .setBody(data.get("contacts"));
        return builder;
    }

}