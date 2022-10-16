package ru.yandex.practicum.manager;

import ru.yandex.practicum.history.HistoryManager;
import ru.yandex.practicum.tasks.Epic;
import ru.yandex.practicum.tasks.Subtask;
import ru.yandex.practicum.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {

    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();

    HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public HashMap<Integer, Epic> getEpics() {
        return epics;
    }

    @Override
    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    @Override
    public HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    //Получение списка всех задач для каждой HashMap
    @Override
    public ArrayList<Epic> getEpicValues() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public ArrayList<Task> getTaskValues() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<Subtask> getSubtaskValues() {
        return new ArrayList<>(subtasks.values());
    }
    //=================================================

    //Удаление всех задач для каждой HashMap
    @Override
    public void removeAllEpics() {
        epics.clear();
    }

    @Override
    public void removeAllTasks() {
        tasks.clear();
    }

    @Override
    public void removeAllSubtasks() {
        subtasks.clear();
    }
    //=================================================

    //Получение задачи по id для каждой HashMap
    @Override
    public Epic getEpicById(int id) {
        historyManager.add(epics.get(id));
        return epics.get(id);
    }

    @Override
    public Task getTaskById(int id) {
        historyManager.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public Subtask getSubTaskById(int id) {
        historyManager.add(subtasks.get(id));
        return subtasks.get(id);
    }
    //=================================================

    //Создание задачи с присвоением id
    @Override
    public void addEpic(Epic epic) {
        epics.put(epic.getId(), epic);
        updateStatusOfEpic(epic);
    }

    @Override
    public void addTask(Task task) {
        tasks.put(task.getId(), task);
    }

    @Override
    public void addSubtask(Subtask subtask, Epic epic) {
        subtasks.put(subtask.getId(), subtask);
        epic.setSubtasks(subtask);
        updateStatusOfEpic(epic);
    }
    //=================================================

    //Обновление задач
    @Override
    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
        updateStatusOfEpic(epic);
    }

    @Override
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    @Override
    public void updateSubtask(Subtask subtask, Epic epic) {
        subtasks.put(subtask.getId(), subtask);
        epic.setSubtasks(subtask);
        updateStatusOfEpic(epic);
    }
    //=================================================

    //Удаление задачи по идентификатору
    @Override
    public void removeEpicById(int id) {
        epics.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void removeTaskById(int id) {
        tasks.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void removeSubtaskById(int id, Epic epic) {
        subtasks.remove(id);
        historyManager.remove(id);
        updateStatusOfEpic(epic);
    }
    //=================================================

    //Получение списка всех подзадач эпика
    @Override
    public ArrayList<Subtask> getAllSubtasks(int id, Epic epic) {
        ArrayList<Subtask> subtaskList = new ArrayList<>();
        subtaskList = epic.getSubtasks();
        return subtaskList;
    }
    //=================================================

    //Обновление статуса эпика
    @Override
    public void updateStatusOfEpic(Epic epic) {
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

    //Получение истории просмотров
    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
    //=================================================
}
