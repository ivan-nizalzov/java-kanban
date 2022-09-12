package ru.yandex.practicum.manager;

import ru.yandex.practicum.tasks.Epic;
import ru.yandex.practicum.tasks.Subtask;
import ru.yandex.practicum.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {

    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();

    public HashMap<Integer, Epic> getEpics() {
        return epics;
    }

    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    public HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
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
        epics.put(epic.getId(), epic);
        updateStatusOfEpic(epic);
    }

    public void addTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public void addSubtask(Subtask subtask, Epic epic) {
        subtasks.put(subtask.getId(), subtask);
        epic.setSubtasks(subtask);
        updateStatusOfEpic(epic);
    }
    //=================================================

    //Обновление задач
    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
        updateStatusOfEpic(epic);
    }

    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public void updateSubtask(Subtask subtask, Epic epic) {
        subtasks.put(subtask.getId(), subtask);
        epic.setSubtasks(subtask);
        updateStatusOfEpic(epic);
    }
    //=================================================

    //Удаление задачи по идентификатору
    public void removeEpicById(int id) {
        epics.remove(id);
    }

    public void removeTaskById(int id) {
        tasks.remove(id);
    }

    public void removeSubtaskById(int id, Epic epic) {
        subtasks.remove(id);
        updateStatusOfEpic(epic);
    }
    //=================================================

    //Получение списка всех подзадач эпика
    public ArrayList<Subtask> getAllSubtasks(int id, Epic epic) {
        ArrayList<Subtask> subtaskList = new ArrayList<>();
        subtaskList = epic.getSubtasks();
        return subtaskList;
    }
    //=================================================

    //Обновление статуса эпика
    private void updateStatusOfEpic(Epic epic) {
        int countNew = 0;
        int countDone = 0;

        if (subtasks.isEmpty()) {
            epic.setTaskStatus(TaskStatus.NEW);
        } else {
            for (Subtask subtask : subtasks.values()) {
                if (subtask.getTaskStatus().equals(TaskStatus.NEW)) {
                    countNew++;
                } else if (subtask.getTaskStatus().equals(TaskStatus.DONE)) {
                    countDone++;
                }
            }

            if (countNew == subtasks.size()) {
                epic.setTaskStatus(TaskStatus.NEW);
            } else if (countDone == subtasks.size()) {
                epic.setTaskStatus(TaskStatus.DONE);
            } else {
                epic.setTaskStatus(TaskStatus.IN_PROGRESS);
            }
        }
    }
    //=================================================
}
