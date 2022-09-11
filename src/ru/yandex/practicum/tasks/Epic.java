package ru.yandex.practicum.tasks;

import ru.yandex.practicum.manager.TaskStatus;

import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {

    //РЕВЬЮЕРУ: я его оставил, т.к. наследую конструктор Task, а если taskId добавить в конструктор,
    // то придется при создании объектов в Main каждый раз его указывать
    private int epicId;
    private ArrayList<Subtask> subtasks; //РЕВЬЮЕРУ: изменил тип ArrayList на <Subtask>

    public Epic(String taskName, String taskDescription, String taskStatus) {
        super(taskName, taskDescription, taskStatus);
        taskStatus = String.valueOf(TaskStatus.NEW);
        subtasks = new ArrayList<>();
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

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    public ArrayList<Subtask> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(ArrayList<Subtask> subtasks) {
        this.subtasks = subtasks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Epic)) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return epicId == epic.epicId && subtasks.equals(epic.subtasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicId, subtasks);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "epicId=" + epicId +
                ", subtasks=" + subtasks +
                "} " + super.toString();
    }
}
