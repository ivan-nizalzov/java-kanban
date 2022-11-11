package ru.yandex.practicum.manager;

import ru.yandex.practicum.tasks.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CSVTaskFormat {

    //Создание экземпляров классов Task, Epic, Subtask из строки
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
    //=================================================

    //Реализация собственного метода toString()
    public static String toString(Task task) {
        return String.join(",",
                String.valueOf(task.getId()),
                String.valueOf(task.getTaskType()),
                task.getTaskName(),
                String.valueOf(task.getTaskStatus()),
                task.getTaskDescription());
    }
    //=================================================

    //Перегрузка метода toString() для экземпляров класса Subtask
    public static String toString(Subtask subtask) {
        return String.join(",",
                String.valueOf(subtask.getId()),
                String.valueOf(subtask.getTaskType()),
                subtask.getTaskName(),
                String.valueOf(subtask.getTaskStatus()),
                subtask.getTaskDescription(),
                String.valueOf(subtask.getEpicId()));
    }
    //=================================================

    //Представление всех задач в String
    public static String tasksToString(HashMap<Integer, Task> tasks,
                                       HashMap<Integer, Epic> epics,
                                       HashMap<Integer, Subtask> subtasks) {
        List<Task> tempList = new ArrayList<>();
        List<Subtask> tempListSubTask = new ArrayList<>();
        StringBuilder sb = new StringBuilder();

        for (Task task : tasks.values()) {
            tempList.add(task);
        }
        for (Epic epic : epics.values()) {
            tempList.add(epic);
        }

        for (int i = 0; i < tempList.size(); i++) {
            sb.append(CSVTaskFormat.toString(tempList.get(i))).append(System.lineSeparator());
        }

        tempList.clear();

        for (Subtask subtask : subtasks.values()) {
            tempListSubTask.add(subtask);
        }

        for (Subtask subtask : tempListSubTask) {
            sb.append(CSVTaskFormat.toString(subtask)).append(System.lineSeparator());
        }

        tempListSubTask.clear();

        return sb.toString();
    }
    //=================================================

}
