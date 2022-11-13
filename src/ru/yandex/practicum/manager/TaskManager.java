package ru.yandex.practicum.manager;

import ru.yandex.practicum.tasks.Epic;
import ru.yandex.practicum.tasks.Subtask;
import ru.yandex.practicum.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface TaskManager {

    HashMap<Integer, Epic> getEpics();

    HashMap<Integer, Task> getTasks();

    HashMap<Integer, Subtask> getSubtasks();

    //Получение списка всех задач для каждой HashMap
    ArrayList<Epic> getEpicValues();

    ArrayList<Task> getTaskValues();

    ArrayList<Subtask> getSubtaskValues();
    //=================================================

    //Удаление всех задач для каждой HashMap
    void removeAllEpics();

    void removeAllTasks();

    void removeAllSubtasks();
    //=================================================

    //Получение задачи по id для каждой HashMap
    Epic getEpicById(int id);

    Task getTaskById(int id);

    Subtask getSubTaskById(int id);
    //=================================================

    //Создание задачи с присвоением id
    void addEpic(Epic epic);

    void addTask(Task task);

    void addSubtask(Subtask subtask);
    //=================================================

    //Обновление задач
    void updateEpic(Epic epic);

    void updateTask(Task task);

    void updateSubtask(Subtask subtask, Epic epic);
    //=================================================

    //Удаление задачи по идентификатору
    void removeEpicById(int id);

    void removeTaskById(int id);

    void removeSubtaskById(int id, Epic epic);
    //=================================================

    //Получение списка всех подзадач эпика
    ArrayList<Subtask> getAllSubtasks(int id, Epic epic);
    //=================================================

    //Метод возвращает историю просмотренных задач
    List<Task> getHistory();
    //=================================================
}
