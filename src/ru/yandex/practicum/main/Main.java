package ru.yandex.practicum.main;

import ru.yandex.practicum.manager.TaskManager;
import ru.yandex.practicum.manager.TaskStatus;
import ru.yandex.practicum.tasks.Epic;
import ru.yandex.practicum.tasks.Subtask;
import ru.yandex.practicum.tasks.Task;

public class Main {

    public static void main(String[] args) {

        TaskManager taskManager = new TaskManager();
        int id = 0;

        Task task1 = new Task("Задача #1", "Забрать заказ в аптеке", TaskStatus.NEW,
                ++id);
        Task task2 = new Task("Задача #2", "Записаться к барберу", TaskStatus.IN_PROGRESS, ++id);
        taskManager.addTask(task1);
        taskManager.addTask(task2);

        Epic epic1 = new Epic("Эпик #1", "Выполнить финалку 3-го спринта", TaskStatus.NEW,
                ++id);
        Epic epic2 = new Epic("Эпик #2", "Отремонтировать машину", TaskStatus.NEW, ++id);
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);

        Subtask subtask1 = new Subtask("Подзадача #1", "Набросать список классов и методов",
                TaskStatus.IN_PROGRESS, ++id, epic1.getId());
        Subtask subtask2 = new Subtask("Подзадача #2", "Проверить работоспособность кода",
                TaskStatus.NEW, ++id, epic1.getId());
        Subtask subtask3 = new Subtask("Подзадача #3", "Записаться в ЕвроАвто",
                TaskStatus.IN_PROGRESS, ++id, epic2.getId());
        taskManager.addSubtask(subtask1, epic1);
        taskManager.addSubtask(subtask2, epic1);
        taskManager.addSubtask(subtask3, epic2);

        Subtask subtask4 = new Subtask("Подзадача #3", "Перенести запись в ЕвроАвто",
                TaskStatus.NEW, ++id, epic2.getId());
        taskManager.updateSubtask(subtask4, epic2);



    }
}
