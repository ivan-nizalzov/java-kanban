package ru.yandex.practicum.manager;

import ru.yandex.practicum.exception.ManagerSaveException;
import ru.yandex.practicum.history.HistoryManager;
import ru.yandex.practicum.tasks.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {

    public final static String PATH = "";
    HistoryManager historyManager = getHistoryManager();
    Map<Integer, Task> allTasks = new HashMap<>();

    public static void createBackUpFile () {
        try {
            if (!Files.exists(Paths.get(PATH))) {
                Path directory = Files.createDirectory(Paths.get(PATH));
            }
            Path file = Files.createFile(Paths.get(PATH, "back-up.csv"));
        } catch (IOException e) {
            throw new ManagerSaveException("Произошла ошибка при создании файла!" + "\n" + e.getMessage());
        }
    }

    public void save() {
        try {
            FileWriter fileWriter = new FileWriter("back-up.csv");
            final String head = "id,type,name,status,description,epic" + System.lineSeparator();

            String test = tasksToString();
            String test2 = historyManager.historyToString();

            String data = head + test + System.lineSeparator() + test2;

            fileWriter.write(data);
            fileWriter.close();

        } catch (IOException e) {
            throw new ManagerSaveException("Произошла ошибка при записи файла!" + "\n" + e.getMessage());
        }
    }

    private Map<Integer, Task> getAllTasks() {
        allTasks.putAll(tasks);
        allTasks.putAll(epics);
        allTasks.putAll(subtasks);

        return allTasks;
    }

    public void loadFromLife(String fileName) throws IOException {
        try (BufferedReader fileReader = new BufferedReader(new FileReader(fileName, StandardCharsets.UTF_8))) {

            List<String> listLine = new ArrayList<>();
            List<String[]> listArrays = new ArrayList<>();

            while (fileReader.ready()) {
                String line = fileReader.readLine();
                listLine.add(line);
            }
            if (listLine.size() < 2) {
                System.out.println("В файле back-up.csv нет данных для восстановления.");
                return;
            } else {
                for (String line : listLine) {
                    if (!line.isEmpty() || !line.isBlank()) {
                        listArrays.add(line.split(","));
                    }
                }
                for (String[] lineArray : listArrays) {

                    if (lineArray[1].equals(TaskType.TASK)) {
                        int id = Integer.parseInt(lineArray[0]);
                        addTask(fromStringToTask(lineArray, TaskType.TASK));
                        if (getIdCounter() <= id) {
                            setIdCounter(id);
                        }

                    } else if (lineArray[1].equals(TaskType.EPIC)) {
                        int id = Integer.parseInt(lineArray[0]);
                        addEpic(fromStringToEpic(lineArray, TaskType.EPIC));
                        if (getIdCounter() <= id) {
                            setIdCounter(id);
                        }
                    } else if (lineArray[1].equals(TaskType.SUBTASK)) {
                        int id = Integer.parseInt(lineArray[0]);
                        int epicId = Integer.parseInt(lineArray[5]);
                        addSubtask(fromStringToSubtask(lineArray, TaskType.SUBTASK), getEpicById(epicId));
                        if (getIdCounter() <= id) {
                            setIdCounter(id);
                        }
                    } else {
                        for (int k = 0; k < lineArray.length; k++) {
                            historyManager.addHistory(getAllTasks().get(lineArray[k]));
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Произошла ошибка при чтении данных из файла!" + "\n" + e.getMessage());
        }
    }

    public Task fromStringToTask(String[] value, TaskType taskType) {
        int id = Integer.parseInt(value[0]);
        String taskName = value[2];
        TaskStatus taskStatus = TaskStatus.valueOf(value[3]);
        String taskDescription = value[4];
        int epicId = Integer.parseInt(value[5]);

        return new Task(id, taskType, taskName, taskStatus, taskDescription);
    }

    public Epic fromStringToEpic(String[] value, TaskType taskType) {
        int id = Integer.parseInt(value[0]);
        String taskName = value[2];
        TaskStatus taskStatus = TaskStatus.valueOf(value[3]);
        String taskDescription = value[4];
        int epicId = Integer.parseInt(value[5]);

        return new Epic(id, taskType, taskName, taskStatus, taskDescription);
    }

    public Subtask fromStringToSubtask(String[] value, TaskType taskType) {
        int id = Integer.parseInt(value[0]);
        String taskName = value[2];
        TaskStatus taskStatus = TaskStatus.valueOf(value[3]);
        String taskDescription = value[4];
        int epicId = Integer.parseInt(value[5]);

        return new Subtask(id, taskType, taskName, taskStatus, taskDescription, epicId);
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
    public void addSubtask(Subtask subtask, Epic epic) {
        super.addSubtask(subtask, epic);
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

    //Реализация собственного метода toString()
    public String toString(Task task) {
        return String.join(",",
                String.valueOf(task.getId()),
                String.valueOf(task.getTaskType()),
                task.getTaskName(),
                String.valueOf(task.getTaskStatus()),
                task.getTaskDescription());
    }
    //=================================================

    //Представление всех задач в String
    private String tasksToString() {
        List<Task> tempList = new ArrayList<>();
        StringBuilder sb = new StringBuilder();

        for (Task task : tasks.values()) {
            tempList.add(task);
        }
        for (Epic epic : epics.values()) {
            tempList.add(epic);
        }
        for (Subtask subtask : subtasks.values()) {
            tempList.add(subtask);
        }

        for (int i = 0; i < tempList.size(); i++) {
            sb.append(toString(tempList.get(i))).append(System.lineSeparator());
        }

        return sb.toString();
    }
    //=================================================
}
