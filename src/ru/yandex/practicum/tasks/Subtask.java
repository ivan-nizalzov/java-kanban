package ru.yandex.practicum.tasks;

import java.util.Objects;

public class Subtask extends Task {


    //РЕВЬЮЕРУ: аналогично Epic: я его оставил, т.к. наследую конструктор Task, а если taskId добавить в конструктор,
    // то придется при создании объектов в Main каждый раз его указывать
    private int subtaskId;
    private int epicId;

    public Subtask(String taskName, String taskDescription, String taskStatus, int epicId) {
        super(taskName, taskDescription, taskStatus);
        this.epicId = epicId;
    }

    public int getSubtaskId() {
        return subtaskId;
    }

    public void setSubtaskId(int subtaskId) {
        this.subtaskId = subtaskId;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    public String getTaskName() {
        return super.getTaskName();
    }

    public void setTaskName(String taskName) {
        super.setTaskName(taskName);
    }

    public String getTaskDescription() {
        return super.getTaskDescription();
    }

    public void setTaskDescription(String taskDescription) {
        super.setTaskDescription(taskDescription);
    }

    public int getTaskId() {
        return super.getTaskId();
    }

    public void setTaskId(int taskId) {
        super.setTaskId(taskId);
    }

    public String getTaskStatus() {
        return super.getTaskStatus();
    }

    public void setTaskStatus(String taskStatus) {
        super.setTaskStatus(taskStatus);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Subtask)) return false;
        if (!super.equals(o)) return false;
        Subtask subtask = (Subtask) o;
        return subtaskId == subtask.subtaskId && epicId == subtask.epicId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(subtaskId, epicId);
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "subtaskId=" + subtaskId +
                ", epicId=" + epicId +
                "} " + super.toString();
    }
}
