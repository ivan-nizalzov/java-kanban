package ru.yandex.practicum.manager;

import ru.yandex.practicum.tasks.Epic;
import ru.yandex.practicum.tasks.Subtask;
import ru.yandex.practicum.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {

    private static int id = 0;

    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();

    Epic epic;
    Subtask subtask;

    public HashMap<Integer, Epic> getEpics() {
        return epics;
    }

    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    public HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    public static int getId() {
        return id;
    }

    //Получение списка всех задач для каждой HashMap
    public ArrayList<Epic> getEpicValues() {
        return new ArrayList<>(epics.values());
    }

    public ArrayList<Task> getTaskValues() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<Subtask> getSubtaskValues() {
        return new ArrayList<>(subtasks.values());
    }
    //=================================================

    //Удаление всех задач для каждой HashMap
    public void removeAllEpics() {
        epics.clear();
    }

    public void removeAllTasks() {
        tasks.clear();
    }

    public void removeAllSubtasks() {
        subtasks.clear();
    }
    //=================================================

    //Получение задачи по id для каждой HashMap
    public Epic getEpicById(int id) {
        return epics.get(id);
    }

    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    public Subtask getSubTaskById(int id) {
        return subtasks.get(id);
    }
    //=================================================

    //Создание задачи с присвоением id
    public void addEpic(Epic epic) {
        epic.setEpicId(++id);
        epics.put(epic.getEpicId(), epic);
        updateStatusOfEpic(epic);
    }

    public void addTask(Task task) {
        task.setTaskId(++id);
        tasks.put(task.getTaskId(), task);
    }

    public void addSubtask(Subtask subtask) {
        subtask.setSubtaskId(++id);
        subtasks.put(subtask.getSubtaskId(), subtask);
        epic.setSubtaskIds(subtask.getSubtaskId());
        updateStatusOfEpic(getEpicById(subtask.getEpicId()));
    }
    //=================================================

    //Обновление задач
    public void updateEpic(Epic epic) {
        epics.put(epic.getEpicId(), epic);
        updateStatusOfEpic(epic);
    }

    public void updateTask(Task task) {
        tasks.put(task.getTaskId(), task);
    }

    public void updateSubtask(Subtask subtask) {
        subtasks.put(subtask.getSubtaskId(), subtask);
        updateStatusOfEpic(getEpicById(subtask.getEpicId()));
    }
    //=================================================

    //Удаление задачи по идентификатору
    public void removeEpicById(int id) {
        epics.remove(id);
        updateStatusOfEpic(epic);
    }

    public void removeTaskById(int id) {
        tasks.remove(id);
    }

    public void removeSubtaskById(int id) {
        subtasks.remove(id);
        updateStatusOfEpic(getEpicById(subtask.getEpicId()));
    }
    //=================================================

    //Получение списка всех подзадач эпика
    public ArrayList<Subtask> getAllSubtasks(int id) {
        ArrayList<Integer> subtaskListIds = epics.get(id).getSubtaskIds();
        ArrayList<Subtask> subtaskList = new ArrayList<>();
        for (int number : subtaskListIds) {
            subtaskList.add(subtasks.get(number));
        }
        return subtaskList;
    }
    //=================================================

    //Обновление статуса эпика
    public void updateStatusOfEpic(Epic epic) {
        int countNew = 0;
        int countDone = 0;

        if (subtasks.isEmpty()) {
            epic.setTaskStatus(String.valueOf(TaskStatus.NEW));
        } else {
            for (Subtask subtask : subtasks.values()) {
                if (subtask.getTaskStatus().equals(TaskStatus.NEW)) {
                    countNew++;
                } else if (subtask.getTaskStatus().equals(TaskStatus.DONE)) {
                    countDone++;
                }
            }

            if (countNew == subtasks.size()) {
                epic.setTaskStatus(String.valueOf(TaskStatus.NEW));
            } else if (countDone == subtasks.size()) {
                epic.setTaskStatus(String.valueOf(TaskStatus.DONE));
            } else {
                epic.setTaskStatus(String.valueOf(TaskStatus.IN_PROGRESS));
            }
        }
    }
    //=================================================
}
