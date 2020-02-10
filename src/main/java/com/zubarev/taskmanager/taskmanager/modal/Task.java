package com.zubarev.taskmanager.taskmanager.modal;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
public class Task implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String taskName;
    private String descriptionTask;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime time;
    private String contacts;

    protected Task() {
    }

    public Task(String taskName, String descriptionTask, LocalDate date, LocalTime time, String contacts){
        this.taskName=taskName;
        this.descriptionTask=descriptionTask;
        this.date=date;
        this.time=time;
        this.contacts=contacts;
    }


    public Long getId() {
        return id;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getDescriptionTask() {
        return descriptionTask;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getContacts() {
        return contacts;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setDescriptionTask(String descriptionTask) {
        this.descriptionTask = descriptionTask;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }


    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", taskName='" + taskName + '\'' +
                ", descriptionTask='" + descriptionTask + '\'' +
                ", date=" + date +
                ", time=" + time +
                ", contacts='" + contacts + '\'' +
                '}';
    }
}
