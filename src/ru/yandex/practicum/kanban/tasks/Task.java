package ru.yandex.practicum.kanban.tasks;

import java.time.LocalDateTime;
import java.util.Objects;

public class Task {

    private int id; //Уникальный идентификационный номер задачи
    private TaskType taskType; //Тип задачи
    private String taskName; //Наименование задачи
    private String taskDescription; //Описание задачи
    private TaskStatus taskStatus; //Статус задачи
    private int duration; //Продолжительность задачи
    private LocalDateTime startTime; //Дата, когда предполагается приступить к задаче
    private LocalDateTime endTime; //Время завершения задачи (расчетное, startTime + duration)

    public Task(int id, TaskType taskType, String taskName, TaskStatus taskStatus,
                String taskDescription, int duration, LocalDateTime startTime) {
        this.id = id;
        this.taskType = taskType;
        this.taskName = taskName;
        this.taskStatus = taskStatus;
        this.taskDescription = taskDescription;
        this.duration = duration;
        this.startTime = startTime;
    }

    public Task(int id, TaskType taskType, String taskName, TaskStatus taskStatus,
                String taskDescription) {
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

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
         return endTime = startTime.plusMinutes(duration);
    }

    @Override
    public String toString() {
        return "Task: {" +
                "taskId=" + getId() +
                ", taskType=" + getTaskType() +
                ", taskName=" + getTaskName() +
                ", taskStatus=" + getTaskStatus() +
                ", taskDescription='" + getTaskDescription() +
                ", taskDuration='" + getDuration() +
                ", taskStartTime='" + getStartTime() +
                "'}" + System.lineSeparator();

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task)) return false;
        Task task = (Task) o;
        return id == task.id && duration == task.duration && taskType == task.taskType
                && Objects.equals(taskName, task.taskName) && Objects.equals(taskDescription, task.taskDescription)
                && taskStatus == task.taskStatus && Objects.equals(startTime, task.startTime)
                && Objects.equals(endTime, task.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, taskType, taskName, taskDescription, taskStatus, duration, startTime, endTime);
    }
}
