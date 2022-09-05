package ru.yandex.practicum.main;

import ru.yandex.practicum.manager.TaskManager;
import ru.yandex.practicum.tasks.Epic;
import ru.yandex.practicum.tasks.Subtask;
import ru.yandex.practicum.tasks.Task;

public class Main {

    public static void main(String[] args) {

        TaskManager taskManager = new TaskManager();

        Task task1 = new Task("Задача #1", "Забрать заказ в аптеке", "NEW");
        Task task2 = new Task("Задача #2", "Записаться к барберу", "IN_PROGRESS");
        taskManager.addTask(task1);
        taskManager.addTask(task2);

        Epic epic1 = new Epic("Эпик #1", "Выполнить финалку 3-го спринта", "NEW");
        Epic epic2 = new Epic("Эпик #2", "Отремонтировать машину", "NEW");
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);

        Subtask subtask1 = new Subtask("Подзадача #1", "Набросать список классов и методов",
                "IN_PROGRESS", epic1.getEpicId());
        Subtask subtask2 = new Subtask("Подзадача #2", "Проверить работоспособность кода",
                "NEW", epic1.getEpicId());
        Subtask subtask3 = new Subtask("Подзадача #3", "Записаться в ЕвроАвто",
                "IN_PROGRESS", epic2.getEpicId());
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        taskManager.addSubtask(subtask3);

        Subtask subtask4 = new Subtask("Подзадача #3", "Перенести запись в ЕвроАвто",
                "NEW", epic2.getEpicId());
        taskManager.updateSubtask(subtask4);



    }
}
