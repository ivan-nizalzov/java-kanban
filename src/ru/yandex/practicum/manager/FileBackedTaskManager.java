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

    private final File file;
    static Map<Integer, Task> allTasks = new HashMap<>();

    public FileBackedTaskManager(File file) {
        super();
        this.file = file;
    }

    private void save() {
        try {
            FileWriter fileWriter = new FileWriter("back-up.csv");
            final String head = "id,type,name,status,description,epic" + System.lineSeparator();

            String tasksStr = tasksToString();
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
        allTasks.putAll(tasks);
        allTasks.putAll(epics);
        allTasks.putAll(subtasks);

        return allTasks;
    }

    public void loadFromLife(File file) throws IOException {
        try (BufferedReader fileReader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {

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

    //Перегрузка метода toString() для экземпляров класса Subtask
    public String toString(Subtask subtask) {
        return String.join(",",
                String.valueOf(subtask.getId()),
                String.valueOf(subtask.getTaskType()),
                subtask.getTaskName(),
                String.valueOf(subtask.getTaskStatus()),
                subtask.getTaskDescription(),
                String.valueOf(subtask.getEpicId()));
    }
    //=================================================

    //Методы для восстановления состояния менеджера после выполнения loadFromFile
    public void restoreListOfTasks(Task task) {
        tasks.put(task.getId(), task);
    }

    public void restoreListOfEpics(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    public void restoreListOfSubtasks(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
    }

    public void restoreHistory() {

    }
    //=================================================

    //Представление всех задач в String
    private String tasksToString() {
        List<Task> tempList = new ArrayList<>();
        List<Subtask> tempListSubTask = new ArrayList<>();
        StringBuilder sb = new StringBuilder();

        for (Task task : tasks.values()) {
            tempList.add(task);
        }
        for (Epic epic : epics.values()) {
            tempList.add(epic);
        }

        for (int i = 0; i < tempList.size(); i++) {
            sb.append(toString(tempList.get(i))).append(System.lineSeparator());
        }

        tempList.clear();

        for (Subtask subtask : subtasks.values()) {
            tempListSubTask.add(subtask);
        }

        for (Subtask subtask : tempListSubTask) {
            sb.append(toString(subtask)).append(System.lineSeparator());
        }

        tempListSubTask.clear();

        return sb.toString();
    }
    //=================================================

    //В соответствие с ТЗ создаю второй метод main() для тестирования кода менеджера FileBackedTaskManager
    public static void main(String[] args) throws IOException {

        FileBackedTaskManager manager = new FileBackedTaskManager(new File("back-up.csv"));

        //Для переключения между методами необходимо закомментировать "лишний"

        //Сценарий тестирования создания таксов и их просмотра для сохранения в истории
        manager.testCode(manager);

        //Тестирование сериализации
        /*manager.loadFromLife(new File("back-up.csv"));
        System.out.println(tasks.toString());
        System.out.println(epics.toString());
        System.out.println(subtasks.toString());*/
    }

    public void testCode(FileBackedTaskManager manager) {
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
        int id1 = epic1.getId();
        int id2 = epic2.getId();
        Subtask subtask1 = new Subtask(++id, TaskType.SUBTASK, "Подзадача #1", TaskStatus.NEW,
                "Набросать список классов и методов", id1);
        Subtask subtask2 = new Subtask(++id, TaskType.SUBTASK, "Подзадача #2", TaskStatus.NEW,
                "Проверить работоспособность кода", id1);
        Subtask subtask3 = new Subtask(++id, TaskType.SUBTASK, "Подзадача #3", TaskStatus.NEW,
                "Закоммитить код и затем запушить его", id1);

        Subtask subtask4 = new Subtask(++id, TaskType.SUBTASK, "Подзадача #4", TaskStatus.IN_PROGRESS,
                "Записаться в ЕвроАвто", id2);

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
