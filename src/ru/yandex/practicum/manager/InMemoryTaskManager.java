package ru.yandex.practicum.manager;

import ru.yandex.practicum.history.HistoryManager;
import ru.yandex.practicum.tasks.Epic;
import ru.yandex.practicum.tasks.Subtask;
import ru.yandex.practicum.tasks.Task;
import ru.yandex.practicum.tasks.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

public class InMemoryTaskManager implements TaskManager {

    protected HashMap<Integer, Epic> epics = new HashMap<>();
    protected HashMap<Integer, Task> tasks = new HashMap<>();
    protected HashMap<Integer, Subtask> subtasks = new HashMap<>();
    protected static int idCounter = 0;

    protected HistoryManager historyManager = Managers.getDefaultHistory();

    public static int getIdCounter() {
        return idCounter;
    }

    public static void setIdCounter(int idCounter) {
        InMemoryTaskManager.idCounter = idCounter;
    }

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
        if (epics.containsKey(id)) {
            historyManager.addHistory(epics.get(id));
            return epics.get(id);
        } else {
            throw new NoSuchElementException();
        }
    }

    @Override
    public Task getTaskById(int id) {
        if (tasks.containsKey(id)) {
            historyManager.addHistory(tasks.get(id));
            return tasks.get(id);
        } else {
            throw new NoSuchElementException();
        }
    }

    @Override
    public Subtask getSubTaskById(int id) {
        if (subtasks.containsKey(id)) {
            historyManager.addHistory(subtasks.get(id));
            return subtasks.get(id);
        } else {
            throw new NoSuchElementException();
        }
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
    public void addSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
        epics.get(subtask.getEpicId()).setSubtasks(subtask);
        updateStatusOfEpic(epics.get(subtask.getEpicId()));
    }
    //=================================================

    //Обновление задач
    @Override
    public void updateEpic(Epic epic) {
        epics.remove(epic.getId());
        epics.put(epic.getId(), epic);
        updateStatusOfEpic(epic);
    }

    @Override
    public void updateTask(Task task) {
        tasks.remove(task.getId());
        tasks.put(task.getId(), task);
    }

    @Override
    public void updateSubtask(Subtask subtask, Epic epic) {
        subtasks.remove(subtask.getId());
        subtasks.put(subtask.getId(), subtask);
        epic.setSubtasks(subtask);
        updateStatusOfEpic(epic);
    }
    //=================================================

    //Удаление задачи по идентификатору
    @Override
    public void removeEpicById(int id) {
        epics.remove(id);
        historyManager.removeHistoryById(id);

        List<Subtask> tempList = new ArrayList<>();
        for (Subtask subtask : subtasks.values()) {
            if (subtask.getEpicId() == id) {
                tempList.add(subtask);
            }
        }
        for (Subtask subtask : tempList) {
            subtasks.remove(subtask.getId());
            historyManager.removeHistoryById(subtask.getId());
        }
    }

    @Override
    public void removeTaskById(int id) {
        tasks.remove(id);
        historyManager.removeHistoryById(id);
    }

    @Override
    public void removeSubtaskById(int id, Epic epic) {
        subtasks.remove(id);
        historyManager.removeHistoryById(id);
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

    //Получение истории просмотров
    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
    //=================================================
}
