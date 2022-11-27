package ru.yandex.practicum.kanban.manager;

import ru.yandex.practicum.kanban.tasks.*;

import java.time.LocalDateTime;
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
        int taskDuration = Integer.parseInt(value[5]);
        LocalDateTime taskStartTime = LocalDateTime.parse(value[6]);

        return new Task(id, taskType, taskName, taskStatus, taskDescription, taskDuration, taskStartTime);
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
        int subTaskDuration = Integer.parseInt(value[5]);
        LocalDateTime subTaskStartTime = LocalDateTime.parse(value[6]);
        int epicId = Integer.parseInt(value[7]);

        return new Subtask(id, taskType, taskName, taskStatus, taskDescription,
                subTaskDuration, subTaskStartTime, epicId);
    }
    //=================================================

    //Реализация собственного метода toString()
    public static String toStringTask(Task task) {
        return String.join(",",
                String.valueOf(task.getId()),
                String.valueOf(task.getTaskType()),
                task.getTaskName(),
                String.valueOf(task.getTaskStatus()),
                task.getTaskDescription(),
                String.valueOf(task.getDuration()),
                String.valueOf(task.getStartTime()));
    }
    //=================================================

    //Перегрузка метода toString() для экземпляров класса Subtask
    public static String toStringSubtask(Subtask subtask) {
        return String.join(",",
                String.valueOf(subtask.getId()),
                String.valueOf(subtask.getTaskType()),
                subtask.getTaskName(),
                String.valueOf(subtask.getTaskStatus()),
                subtask.getTaskDescription(),
                String.valueOf(subtask.getDuration()),
                String.valueOf(subtask.getStartTime()),
                String.valueOf(subtask.getEpicId()));
    }
    //=================================================

    //Перегрузка метода toString() для экземпляров класса Epic
    public static String toStringEpic(Epic epic) {
        return String.join(",",
                String.valueOf(epic.getId()),
                String.valueOf(epic.getTaskType()),
                epic.getTaskName(),
                String.valueOf(epic.getTaskStatus()),
                epic.getTaskDescription(),
                String.valueOf(epic.getEpicDuration()),
                String.valueOf(epic.getEpicStartTime()));
    }
    //=================================================

    //Представление всех задач в String
    public static String tasksToString(HashMap<Integer, Task> tasks,
                                       HashMap<Integer, Epic> epics,
                                       HashMap<Integer, Subtask> subtasks) {
        List<Task> tempListTasks = new ArrayList<>();
        List<Subtask> tempListSubTasks = new ArrayList<>();
        List<Epic> tempListEpics = new ArrayList<>();
        StringBuilder sb = new StringBuilder();

        for (Task task : tasks.values()) {
            tempListTasks.add(task);
        }
        for (Epic epic : epics.values()) {
            tempListEpics.add(epic);
        }
        for (Subtask subtask : subtasks.values()) {
            tempListSubTasks.add(subtask);
        }

        for (int i = 0; i < tempListTasks.size(); i++) {
            sb.append(CSVTaskFormat.toStringTask(tempListTasks.get(i))).append(System.lineSeparator());
        }
        for (int i = 0; i < tempListEpics.size(); i++) {
            sb.append(CSVTaskFormat.toStringEpic(tempListEpics.get(i))).append(System.lineSeparator());
        }
        for (int i = 0; i < tempListSubTasks.size(); i++) {
            sb.append(CSVTaskFormat.toStringSubtask(tempListSubTasks.get(i))).append(System.lineSeparator());
        }

        tempListTasks.clear();
        tempListEpics.clear();
        tempListSubTasks.clear();

        return sb.toString();
    }
    //=================================================

}
