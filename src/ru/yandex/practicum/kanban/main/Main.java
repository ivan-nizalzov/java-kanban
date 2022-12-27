package ru.yandex.practicum.kanban.main;

import ru.yandex.practicum.kanban.http.HttpTaskManager;
import ru.yandex.practicum.kanban.http.KVServer;
import ru.yandex.practicum.kanban.manager.Managers;
import ru.yandex.practicum.kanban.tasks.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.Month;

public class Main {
    public static void main(String[] args) throws IOException {
        // Запуск сервера:
        new KVServer().start();

        HttpTaskManager manager = Managers.getDefault();
        int id = 0;

        Task task1 = new Task(
                ++id,
                TaskType.TASK,
                "Задача #1",
                TaskStatus.NEW,
                "Оплатить к/у",
                10,
                LocalDateTime.of(2022, Month.NOVEMBER, 24, 20, 00));
        manager.addTask(task1);

        Epic epic1 = new Epic(
                ++id,
                TaskType.EPIC,
                "Эпик #1",
                TaskStatus.NEW,
                "Выполнить финалку 8-го спринта");
        manager.addEpic(epic1);

        int idOfEpicInSubtask = epic1.getId();
        Subtask subtask1 = new Subtask(
                ++id,
                TaskType.SUBTASK,
                "Подзадача #1",
                TaskStatus.NEW,
                "Набросать список классов и методов",
                120,
                LocalDateTime.of(2022, Month.NOVEMBER, 20, 10, 00),
                idOfEpicInSubtask);
        Subtask subtask2 = new Subtask(
                ++id,
                TaskType.SUBTASK,
                "Подзадача #2",
                TaskStatus.NEW,
                "Проверить работоспособность кода",
                60,
                LocalDateTime.of(2022, Month.NOVEMBER, 20, 20, 00),
                idOfEpicInSubtask);
        Subtask subtask3 = new Subtask(
                ++id,
                TaskType.SUBTASK,
                "Подзадача #3",
                TaskStatus.NEW,
                "Закоммитить код и затем запушить его",
                15,
                LocalDateTime.of(2022, Month.NOVEMBER, 20, 21, 30),
                idOfEpicInSubtask);

        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);
        manager.addSubtask(subtask3);

        // Просмотр задач для добавления в историю
        manager.getEpicById(2);
        manager.getTaskById(1);
        manager.getEpicById(2);
        manager.getSubtaskById(4);
        manager.getSubtaskById(3);
        manager.getSubtaskById(4);
    }
}
