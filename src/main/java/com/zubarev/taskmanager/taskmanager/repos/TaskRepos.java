package com.zubarev.taskmanager.taskmanager.repos;

import com.zubarev.taskmanager.taskmanager.modal.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepos extends JpaRepository<Task, Long> {
}
