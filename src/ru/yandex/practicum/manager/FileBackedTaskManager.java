package ru.yandex.practicum.manager;

import ru.yandex.practicum.exception.ManagerSaveException;
import ru.yandex.practicum.tasks.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.yandex.practicum.manager.CSVTaskFormat.*;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private File file;
    static Map<Integer, Task> allTasks = new HashMap<>();

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

    //РЕВЬЮЕРУ: данный метод единственный возвращает все такси, эпики и их сабтаски
    //В InMemoryTaskManager метод getAllSubtasks(int id, Epic epic) возвращает только сабтаски
    //Не знаю, зачем он нужен вообще, если нигде не используется (вызов всех сабтасков),
    //но реализацию данного метода требовали в ТЗ 3-го спринта
    private static Map<Integer, Task> getAllTasks() {
        for (Task task : tasks.values()) {
            allTasks.put(task.getId(), task);
        }
        for (Epic epic : epics.values()) {
            allTasks.put(epic.getId(), epic);
        }

        for (Subtask subtask : subtasks.values()) {
            allTasks.put(subtask.getId(), subtask);
        }

        return allTasks;
    }

    public static FileBackedTaskManager loadFromLife(File file) throws IOException {
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
                        restoreListOfTasks(fromStringToTask(lineArray, TaskType.TASK));
                        if (getIdCounter() <= id) {
                            setIdCounter(id);
                        }

                    } else if (lineArray[1].equals(TaskType.EPIC.toString())) {
                        int id = Integer.parseInt(lineArray[0]);
                        restoreListOfEpics(fromStringToEpic(lineArray, TaskType.EPIC));
                        if (getIdCounter() <= id) {
                            setIdCounter(id);
                        }
                    } else if (lineArray[1].equals(TaskType.SUBTASK.toString())) {
                        int id = Integer.parseInt(lineArray[0]);
                        restoreListOfSubtasks(fromStringToSubtask(lineArray, TaskType.SUBTASK));
                        if (getIdCounter() <= id) {
                            setIdCounter(id);
                        }
                    } else {
                        Map<Integer, Task> test = getAllTasks();

                        for (int k = 0; k < lineArray.length; k++) {
                            historyManager.addHistory(test.get(Integer.parseInt(lineArray[k])));
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Произошла ошибка при чтении данных из файла!" + "\n" + e.getMessage());
        }
        return new FileBackedTaskManager();
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

    //Методы для восстановления состояния менеджера после выполнения loadFromFile
    public static void restoreListOfTasks(Task task) {
        tasks.put(task.getId(), task);
    }

    public static void restoreListOfEpics(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    public static void restoreListOfSubtasks(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
    }

    public static void restoreHistory() {

    }
    //=================================================

    //В соответствие с ТЗ создаю второй метод main() для тестирования кода менеджера FileBackedTaskManager
    public static void main(String[] args) throws IOException {

        FileBackedTaskManager manager = Managers.getDefaultFileBackedManager();

        // ПРИМЕЧАНИЕ: часть кода закомментировано. Необходимо раскомментировать код ниже, чтобы
        // заработал тест из ТЗ (создание таска, эпика и двух сабтасков к нему, а затем сохранения истории в файл

        /*
        //Сценарий тестирования создания таксов и их просмотра для сохранения в истории
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

        manager.addSubtask(subtask1, epic1);
        manager.addSubtask(subtask2, epic1);
        manager.addSubtask(subtask3, epic1);
        //=================================================
        manager.getEpicById(2);
        manager.getTaskById(1);
        manager.getSubTaskById(3);
        manager.getSubTaskById(4);
        //=================================================
        System.out.println();
        System.out.println(manager.getHistory());
        //=================================================
        */

        //Тестирование сериализации
        FileBackedTaskManager newFileBackedManager = new FileBackedTaskManager(new File("back-up.csv"));

        newFileBackedManager.loadFromLife(new File("back-up.csv"));
        System.out.println("\n" + "Вывод тасков:");
        System.out.println(newFileBackedManager.tasks.toString());
        System.out.println(newFileBackedManager.epics.toString());
        System.out.println(newFileBackedManager.subtasks.toString());
        System.out.println("\n" + "Вывод истории: ");
        System.out.println(newFileBackedManager.historyManager.getHistory());
    }
}
