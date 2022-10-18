package ru.yandex.practicum.main;

import ru.yandex.practicum.manager.InMemoryTaskManager;
import ru.yandex.practicum.tasks.TaskStatus;
import ru.yandex.practicum.tasks.Epic;
import ru.yandex.practicum.tasks.Subtask;
import ru.yandex.practicum.tasks.Task;

public class Main {

    public static void main(String[] args) {

        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        int id = 0;

        //Добавление задач
        Task task1 = new Task("Задача #1", "Забрать заказ в аптеке", TaskStatus.NEW,
                ++id);
        Task task2 = new Task("Задача #2", "Записаться к барберу", TaskStatus.IN_PROGRESS, ++id);

        inMemoryTaskManager.addTask(task1);
        inMemoryTaskManager.addTask(task2);
        //=================================================

        //Добавление эпиков
        Epic epic1 = new Epic("Эпик #1", "Выполнить финалку 5-го спринта", TaskStatus.NEW,
                ++id);
        Epic epic2 = new Epic("Эпик #2", "Отремонтировать машину", TaskStatus.NEW, ++id);

        inMemoryTaskManager.addEpic(epic1);
        inMemoryTaskManager.addEpic(epic2);
        //=================================================

        //Добавление подзадач
        Subtask subtask1 = new Subtask("Подзадача #1", "Набросать список классов и методов",
                TaskStatus.IN_PROGRESS, ++id, epic1.getId());
        Subtask subtask2 = new Subtask("Подзадача #2", "Проверить работоспособность кода",
                TaskStatus.NEW, ++id, epic1.getId());
        Subtask subtask3 = new Subtask("Подзадача #3", "Закоммитить код и затем запушить его",
                TaskStatus.NEW, ++id, epic1.getId());

        Subtask subtask4 = new Subtask("Подзадача #4", "Записаться в ЕвроАвто",
                TaskStatus.IN_PROGRESS, ++id, epic2.getId());

        inMemoryTaskManager.addSubtask(subtask1, epic1);
        inMemoryTaskManager.addSubtask(subtask2, epic1);
        inMemoryTaskManager.addSubtask(subtask3, epic1);
        inMemoryTaskManager.addSubtask(subtask4, epic2);
        //=================================================

        //Обновление подзадач в эпике
        Subtask subtask5 = new Subtask("Подзадача #4", "Перенести запись в ЕвроАвто",
                TaskStatus.NEW, ++id, epic2.getId());
        inMemoryTaskManager.updateSubtask(subtask5, epic2);
        //=================================================

        //Тестирование кода
        inMemoryTaskManager.getEpicById(3);
        inMemoryTaskManager.getEpicById(4);
        inMemoryTaskManager.getEpicById(3);

        inMemoryTaskManager.getTaskById(1);
        inMemoryTaskManager.getTaskById(2);
        inMemoryTaskManager.getTaskById(2);

        inMemoryTaskManager.getSubTaskById(5);
        inMemoryTaskManager.getSubTaskById(6);
        inMemoryTaskManager.getSubTaskById(6);
        inMemoryTaskManager.getSubTaskById(7);

        System.out.println(inMemoryTaskManager.getHistory());

        inMemoryTaskManager.removeTaskById(2);
        inMemoryTaskManager.removeEpicById(3);

        System.out.println(inMemoryTaskManager.getHistory());
        //=================================================

    }
}
