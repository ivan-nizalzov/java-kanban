package ru.yandex.practicum.tasks;

import ru.yandex.practicum.manager.TaskStatus;

import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {

    private int epicId;
    private ArrayList<Integer> subtaskIds;

    public Epic(String taskName, String taskDescription, String taskStatus) {
        super(taskName, taskDescription, taskStatus);
        taskStatus = String.valueOf(TaskStatus.NEW);
        subtaskIds = new ArrayList<>();
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
        return epicId == epic.epicId && Objects.equals(subtaskIds, epic.subtaskIds);
    }

}
