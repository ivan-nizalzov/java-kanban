package ru.yandex.practicum.kanban.tasks;

import ru.yandex.practicum.kanban.exception.ManagerSaveException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {

    private ArrayList<Subtask> subtasks = new ArrayList<>();
    int duration;
    LocalDateTime startTime;
    LocalDateTime endTime;

    public Epic(int id, TaskType taskType, String epicName, TaskStatus taskStatus,
                String epicDescription) {
        super(id, taskType, epicName, taskStatus, epicDescription);
        subtasks = new ArrayList<>();
    }

    public int getEpicDuration() {
        int sumOfSubtaskDuration = 0;
        for (Subtask subtask : subtasks) {
            sumOfSubtaskDuration += subtask.duration;
        }
        if (sumOfSubtaskDuration < 0) {
            throw new ManagerSaveException("Продолжительность выполнения задачи не может быть меньше нуля.");
        } else {
            return sumOfSubtaskDuration;
        }
    }

    public LocalDateTime getEpicStartTime() {
        LocalDateTime fistStartTimeOfSubtask = null;
        if (subtasks.isEmpty()) {
            setStartTime(null);
            setDuration(0);
        } else {
            fistStartTimeOfSubtask = subtasks.get(0).getStartTime();
            for (Subtask subtask : subtasks) {
                if (subtask.startTime.isBefore(fistStartTimeOfSubtask)) {
                    fistStartTimeOfSubtask = subtask.startTime;
                }
            }
        }
        return fistStartTimeOfSubtask;
    }

    public LocalDateTime getEpicEndTime() {
        LocalDateTime lastStartTimeOfSubtask = subtasks.get(0).getEndTime();
        for (Subtask subtask : subtasks) {
            if (subtask.getEndTime().isAfter(lastStartTimeOfSubtask)) {
                lastStartTimeOfSubtask = subtask.getEndTime();
            }
        }
        if (lastStartTimeOfSubtask.equals(null)) {
            throw new ManagerSaveException("Некорректно задана даты старта задач. Невозможно определить дату завершения.");
        }
        return lastStartTimeOfSubtask;
    }

    public ArrayList<Subtask> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(Subtask subtask) {
        subtasks.add(subtask);
    }

    public void removeSubtask(Subtask subtask) {
        if (subtask.equals(null)) {
            throw new ManagerSaveException("Нельзя удалить пустой экземпляр класса.");
        } else {
            subtasks.remove(subtask);
        }
    }

    @Override
    public String toString() {
        return "Epic: {" +
                "taskId=" + getId() +
                ", taskType=" + getTaskType() +
                ", taskName=" + getTaskName() +
                ", taskStatus=" + getTaskStatus() +
                ", taskDescription='" + getTaskDescription() +
                ", epicDuration='" + getDuration() +
                ", epicStartTime='" + getStartTime() +
                "'}" + System.lineSeparator();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Epic)) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subtasks, epic.subtasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtasks);
    }
}
