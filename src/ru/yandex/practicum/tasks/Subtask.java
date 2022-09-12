package ru.yandex.practicum.tasks;

import ru.yandex.practicum.manager.TaskStatus;
import java.util.Objects;

public class Subtask extends Task {

    private int epicId;

    public Subtask(String taskName, String taskDescription, TaskStatus taskStatus, int id, int epicId) {
        super(taskName, taskDescription, taskStatus, id);
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
        return "Subtask{" +
                "epicId=" + epicId +
                ", taskStatus=" + taskStatus +
                "} " + super.toString();
    }
}
