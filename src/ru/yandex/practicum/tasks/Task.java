package ru.yandex.practicum.tasks;

import java.util.Objects;

public class Task {

    private int id; //Уникальный идентификационный номер задачи
    private TaskType taskType; //Тип задачи
    private String taskName; //Наименование задачи
    private String taskDescription; //Описание задачи
    private TaskStatus taskStatus; //Статус задачи

    public Task(int id, TaskType taskType, String taskName, TaskStatus taskStatus, String taskDescription) {
        this.id = id;
        this.taskType = taskType;
        this.taskName = taskName;
        this.taskStatus = taskStatus;
        this.taskDescription = taskDescription;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }
    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task)) return false;
        Task task = (Task) o;
        return id == task.id && taskName.equals(task.taskName) && taskDescription.equals(task.taskDescription)
                && taskStatus.equals(task.taskStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskName, taskDescription, taskStatus, id);
    }

    @Override
    public String toString() {
        return "Task: {" +
                "taskId=" + getId() +
                ", taskType=" + getTaskType() +
                ", taskName=" + getTaskName() +
                ", taskStatus=" + getTaskStatus() +
                ", taskDescription= '" + getTaskDescription() + "'}" + System.lineSeparator();

    }
}
