package ru.yandex.practicum.main;

import ru.yandex.practicum.manager.FileBackedTaskManager;
import ru.yandex.practicum.manager.InMemoryTaskManager;
import ru.yandex.practicum.manager.Managers;
import ru.yandex.practicum.manager.TaskManager;
import ru.yandex.practicum.tasks.*;

public class Main {
    public static void main(String[] args) {

        TaskManager manager = Managers.getDefaultTaskManager();
        int id = InMemoryTaskManager.getIdCounter();

        //Добавление задач
        Task task1 = new Task(++id, TaskType.TASK, "Задача #1", TaskStatus.NEW,
                "Забрать заказ в аптеке");
        Task task2 = new Task(++id, TaskType.TASK, "Задача #2", TaskStatus.IN_PROGRESS,
                "Записаться к барберу");

        manager.addTask(task1);
        manager.addTask(task2);
        //=================================================

        //Добавление эпиков
        Epic epic1 = new Epic(++id, TaskType.EPIC, "Эпик #1", TaskStatus.NEW,
                "Выполнить финалку 6-го спринта");
        Epic epic2 = new Epic(++id, TaskType.EPIC, "Эпик #2", TaskStatus.NEW,
                "Помыть машину");

        manager.addEpic(epic1);
        manager.addEpic(epic2);
        //=================================================

        //Добавление подзадач
        Subtask subtask1 = new Subtask(++id, TaskType.SUBTASK, "Подзадача #1", TaskStatus.NEW,
                "Набросать список классов и методов", epic1.getId());
        Subtask subtask2 = new Subtask(++id, TaskType.SUBTASK, "Подзадача #2", TaskStatus.NEW,
                "Проверить работоспособность кода", epic1.getId());
        Subtask subtask3 = new Subtask(++id, TaskType.SUBTASK, "Подзадача #3", TaskStatus.NEW,
                "Закоммитить код и затем запушить его", epic1.getId());

        Subtask subtask4 = new Subtask(++id, TaskType.SUBTASK, "Подзадача #4", TaskStatus.IN_PROGRESS,
                "Записаться в ЕвроАвто", epic2.getId());

        manager.addSubtask(subtask1, epic1);
        manager.addSubtask(subtask2, epic1);
        manager.addSubtask(subtask3, epic1);
        manager.addSubtask(subtask4, epic2);
        //=================================================

        //Обновление подзадач в эпике
        Subtask subtask5 = new Subtask(++id, TaskType.SUBTASK, "Подзадача #4", TaskStatus.NEW,
                "Перенести запись в ЕвроАвто", epic2.getId());

        manager.updateSubtask(subtask5, epic2);
        //=================================================

        //Тестирование кода
        manager.getEpicById(3);
        manager.getEpicById(4);
        manager.getEpicById(3);

        manager.getTaskById(1);
        manager.getTaskById(2);
        manager.getTaskById(2);

        manager.getSubTaskById(5);
        manager.getSubTaskById(6);
        manager.getSubTaskById(6);
        manager.getSubTaskById(7);

        System.out.println();
        System.out.println(manager.getHistory());

        manager.removeTaskById(2);
        manager.removeEpicById(3);

        System.out.println();
        System.out.println(manager.getHistory());
        //=================================================

    }
}
