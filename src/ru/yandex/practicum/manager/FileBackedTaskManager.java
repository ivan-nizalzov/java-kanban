package ru.yandex.practicum.manager;

import ru.yandex.practicum.exception.ManagerSaveException;
import ru.yandex.practicum.tasks.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private File file;

    public FileBackedTaskManager(File file) {
        super();
        this.file = file;
    }

    public FileBackedTaskManager() {

    }

    private void save() {
        try {
            FileWriter fileWriter = new FileWriter("back-up.csv");
            final String head = "id,type,name,status,description,epic" + System.lineSeparator();

            String tasksStr = CSVTaskFormat.tasksToString(tasks, epics, subtasks);
            String historyStr = historyManager.historyToString();

            String data = head + tasksStr + System.lineSeparator() + historyStr;

            fileWriter.write(data);
            fileWriter.close();

        } catch (IOException e) {
            throw new ManagerSaveException("Произошла ошибка при записи файла!" + "\n" + e.getMessage());
        }
    }

    public static FileBackedTaskManager loadFromLife(File file) {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager();

        try (BufferedReader fileReader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {

            List<String> listLine = new ArrayList<>();
            List<String[]> listArrays = new ArrayList<>();

            while (fileReader.ready()) {
                String line = fileReader.readLine();
                listLine.add(line);
            }
            if (listLine.size() < 2) {
                System.out.println("В файле back-up.csv нет данных для восстановления.");
            } else {
                for (int i = 1; i < listLine.size(); i++) {
                    if (!listLine.get(i).isEmpty() || !listLine.get(i).isBlank()) {
                        listArrays.add(listLine.get(i).split(","));
                    }
                }
                for (String[] lineArray : listArrays) {

                    if (lineArray[1].equals(TaskType.TASK.toString())) {
                        int id = Integer.parseInt(lineArray[0]);
                        if (getIdCounter() <= id) {
                            setIdCounter(id);
                        }
                        fileBackedTaskManager.addTask(CSVTaskFormat.fromStringToTask(lineArray, TaskType.TASK));

                    } else if (lineArray[1].equals(TaskType.EPIC.toString())) {
                        int id = Integer.parseInt(lineArray[0]);
                        if (getIdCounter() <= id) {
                            setIdCounter(id);
                        }
                        fileBackedTaskManager.addEpic(CSVTaskFormat.fromStringToEpic(lineArray, TaskType.EPIC));
                    } else if (lineArray[1].equals(TaskType.SUBTASK.toString())) {
                        int id = Integer.parseInt(lineArray[0]);
                        if (getIdCounter() <= id) {
                            setIdCounter(id);
                        }
                        fileBackedTaskManager.addSubtask(CSVTaskFormat.fromStringToSubtask(lineArray, TaskType.SUBTASK));
                    } else {
                        Map<Integer, Task> test = new HashMap<>();
                        test.putAll(fileBackedTaskManager.getTasks());
                        test.putAll(fileBackedTaskManager.getEpics());
                        test.putAll(fileBackedTaskManager.getSubtasks());

                        for (String s : lineArray) {
                            fileBackedTaskManager.historyManager.addHistory(
                                    test.get(Integer.parseInt(s)));
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Произошла ошибка при чтении данных из файла!" + "\n" + e.getMessage());
        }
        return fileBackedTaskManager;
    }

    //Удаление всех задач для каждой HashMap
    @Override
    public void removeAllEpics() {
        super.removeAllEpics();
        save();
    }

    @Override
    public void removeAllTasks() {
        super.removeAllTasks();
        save();
    }

    @Override
    public void removeAllSubtasks() {
        super.removeAllSubtasks();
        save();
    }
    //=================================================

    //Получение задачи по id для каждой HashMap
    @Override
    public Epic getEpicById(int id) {
        Epic epic = super.getEpicById(id);
        save();
        return epic;
    }

    @Override
    public Task getTaskById(int id) {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public Subtask getSubTaskById(int id) {
        Subtask subtask = super.getSubTaskById(id);
        save();
        return subtask;
    }
    //=================================================

    //Создание задачи с присвоением id
    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void addSubtask(Subtask subtask) {
        super.addSubtask(subtask);
        save();
    }
    //=================================================

    //Обновление задач
    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask, Epic epic) {
        super.updateSubtask(subtask, epic);
        save();
    }
    //=================================================

    //Удаление задачи по идентификатору
    @Override
    public void removeEpicById(int id) {
        super.removeEpicById(id);
        save();
    }

    @Override
    public void removeTaskById(int id) {
        super.removeTaskById(id);
        save();
    }

    @Override
    public void removeSubtaskById(int id, Epic epic) {
        super.removeSubtaskById(id, epic);
        save();
    }
    //=================================================


    //В соответствие с ТЗ создаю второй метод main() для тестирования кода менеджера FileBackedTaskManager
    public static void main(String[] args) {

        //Сценарий тестирования создания таксов и их просмотра для сохранения в истории
        FileBackedTaskManager manager = new FileBackedTaskManager(new File("back-up.csv"));
        int id = InMemoryTaskManager.getIdCounter();
        //=================================================
        Task task1 = new Task(++id, TaskType.TASK, "Задача #1", TaskStatus.NEW,
                "Оплатить к/у");
        manager.addTask(task1);
        //=================================================
        Epic epic1 = new Epic(++id, TaskType.EPIC, "Эпик #1", TaskStatus.NEW,
                "Выполнить финалку 6-го спринта");
        manager.addEpic(epic1);
        //=================================================
        int id1 = epic1.getId();
        Subtask subtask1 = new Subtask(++id, TaskType.SUBTASK, "Подзадача #1", TaskStatus.NEW,
                "Набросать список классов и методов", id1);
        Subtask subtask2 = new Subtask(++id, TaskType.SUBTASK, "Подзадача #2", TaskStatus.NEW,
                "Проверить работоспособность кода", id1);
        Subtask subtask3 = new Subtask(++id, TaskType.SUBTASK, "Подзадача #3", TaskStatus.NEW,
                "Закоммитить код и затем запушить его", id1);

        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);
        manager.addSubtask(subtask3);
        //=================================================
        manager.getEpicById(2);
        manager.getTaskById(1);
        manager.getSubTaskById(3);
        manager.getSubTaskById(4);
        //=================================================
        System.out.println();
        System.out.println(">>Первый менеджер: ");
        System.out.println("\n" + "Вывод тасков:");
        System.out.println(manager.tasks.toString());
        System.out.println(manager.epics.toString());
        System.out.println(manager.subtasks.toString());
        System.out.println("\n" + "Вывод истории: ");
        System.out.println(manager.getHistory());
        //=================================================

        //Тестирование сериализации
        FileBackedTaskManager newManager =
                FileBackedTaskManager.loadFromLife(new File("back-up.csv"));

        System.out.println("\n" + ">>Второй менеджер: ");
        System.out.println("\n" + "Вывод тасков:");
        System.out.println(newManager.tasks.toString());
        System.out.println(newManager.epics.toString());
        System.out.println(newManager.subtasks.toString());
        System.out.println("\n" + "Вывод истории: ");
        System.out.println(newManager.historyManager.getHistory());
    }
}
