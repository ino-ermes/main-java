package com.mio.entity;

import java.util.Date;

public class Todo {
    private int id;
    private int userId;
    private String taskName;
    private Date deadline;
    private Boolean isCompleted;
    private Date createdAt;
    private Date updatedAt;

    public Todo(int id, int userId, String taskName, Date deadline, Boolean isCompleted, Date createdAt,
            Date updatedAt) {
        this.id = id;
        this.userId = userId;
        this.taskName = taskName;
        this.deadline = deadline;
        this.isCompleted = isCompleted;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public Boolean getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(Boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
