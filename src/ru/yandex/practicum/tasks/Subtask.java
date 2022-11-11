package ru.yandex.practicum.tasks;

import java.util.Objects;

public class Subtask extends Task {

    private int epicId;

    public Subtask(int id, TaskType taskType, String taskName, TaskStatus taskStatus,
                   String taskDescription, int epicId) {
        super(id, taskType, taskName, taskStatus, taskDescription);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Subtask)) return false;
        if (!super.equals(o)) return false;
        Subtask subtask = (Subtask) o;
        return epicId == subtask.epicId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicId);
    }

    @Override
    public String toString() {
        return "Subtask: {" +
                "taskId=" + getId() +
                ", taskType=" + getTaskType() +
                ", taskName=" + getTaskName() +
                ", taskStatus=" + getTaskStatus() +
                ", taskDescription='" + getTaskDescription() +
                ", epicId='" + getEpicId() + "'}" + System.lineSeparator();
    }
}
