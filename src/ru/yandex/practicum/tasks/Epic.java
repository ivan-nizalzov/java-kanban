package ru.yandex.practicum.tasks;

import ru.yandex.practicum.manager.TaskStatus;

import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {

    //РЕВЬЮЕРУ: я его оставил, т.к. наследую конструктор Epic, а если epicId добавить в конструктор,
    // то придется при создании объектов в Main каждый раз его указывать
    private int epicId;

    //РЕВЬЮЕРУ: у меня храрение сабтасков реализовано в классе TaskManager, если создать список
    // ArrayList<Subtask> subtasks, то придется сюда их копировать, что усложнит код
    private ArrayList<Integer> subtaskIds;

    public Epic(String taskName, String taskDescription, String taskStatus) {
        super(taskName, taskDescription, taskStatus);
        taskStatus = String.valueOf(TaskStatus.NEW);
        subtaskIds = new ArrayList<>();
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

    public ArrayList<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    public void setSubtaskIds(int subtaskId) {
        subtaskIds.add(subtaskId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Epic)) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return epicId == epic.epicId && subtaskIds.equals(epic.subtaskIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(epicId, subtaskIds);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "epicId=" + epicId +
                ", subtaskIds=" + subtaskIds +
                "} " + super.toString();
    }
}
