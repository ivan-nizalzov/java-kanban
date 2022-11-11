package ru.yandex.practicum.tasks;

import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {

    private ArrayList<Subtask> subtasks;

    public Epic(int id, TaskType taskType, String epicName, TaskStatus taskStatus, String epicDescription) {
        super(id, taskType, epicName, taskStatus, epicDescription);
        subtasks = new ArrayList<>();
    }

    public ArrayList<Subtask> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(Subtask subtask) {
        subtasks.add(subtask);
    }

    public void removeSubtask(Subtask subtask) {
        subtasks.remove(subtask);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Epic)) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return subtasks.equals(epic.subtasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtasks);
    }

    @Override
    public String toString() {
        return "Epic: {" +
                "taskId=" + getId() +
                ", taskType=" + getTaskType() +
                ", taskName=" + getTaskName() +
                ", taskStatus=" + getTaskStatus() +
                ", taskDescription='" + getTaskDescription() + "'}" + System.lineSeparator();
    }
}
