package ru.yandex.practicum.manager;

import ru.yandex.practicum.tasks.*;

public class CSVTaskFormat {

    public static Task fromStringToTask(String[] value, TaskType taskType) {
        int id = Integer.parseInt(value[0]);
        String taskName = value[2];
        TaskStatus taskStatus = TaskStatus.valueOf(value[3]);
        String taskDescription = value[4];

        return new Task(id, taskType, taskName, taskStatus, taskDescription);
    }

    public static Epic fromStringToEpic(String[] value, TaskType taskType) {
        int id = Integer.parseInt(value[0]);
        String taskName = value[2];
        TaskStatus taskStatus = TaskStatus.valueOf(value[3]);
        String taskDescription = value[4];

        return new Epic(id, taskType, taskName, taskStatus, taskDescription);
    }

    public static Subtask fromStringToSubtask(String[] value, TaskType taskType) {
        int id = Integer.parseInt(value[0]);
        String taskName = value[2];
        TaskStatus taskStatus = TaskStatus.valueOf(value[3]);
        String taskDescription = value[4];
        int epicId = Integer.parseInt(value[5]);

        return new Subtask(id, taskType, taskName, taskStatus, taskDescription, epicId);
    }

}
