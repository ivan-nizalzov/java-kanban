package ru.yandex.practicum.kanban.http;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ru.yandex.practicum.kanban.manager.FileBackedTaskManager;
import ru.yandex.practicum.kanban.manager.Managers;
import ru.yandex.practicum.kanban.tasks.Epic;
import ru.yandex.practicum.kanban.tasks.Subtask;
import ru.yandex.practicum.kanban.tasks.Task;
import ru.yandex.practicum.kanban.tasks.TaskType;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpTaskManager extends FileBackedTaskManager {
    private final Gson gson;
    private final KVTaskClient client;

    public HttpTaskManager(int port) {
        this(port, false);
    }

    public HttpTaskManager(int port, boolean load) {
        super(null);
        gson = Managers.getGson();
        client = new KVTaskClient(port);
        if (load) {
            load();
        }
    }

    protected void addTasks(List<? extends Task> tasks) {
        for (Task task : tasks) {
            final int id = task.getId();
            TaskType type = task.getTaskType();

            if (type.equals(TaskType.TASK)) {
                this.tasks.put(id, task);
                prioritizedTasks.add(task);
            } else if (type.equals(TaskType.SUBTASK)) {
                this.subtasks.put(id, (Subtask) task);
                prioritizedTasks.add(task);
            } else if (type.equals(TaskType.EPIC)) {
                this.epics.put(id, (Epic) task);
            }
        }
    }

    private void load() {
        String json = client.load("tasks");
        Type type = new TypeToken<Map<Integer, Task>>(){
        }.getType();
        Map<Integer, Task> tasksMap = gson.fromJson(json, type);
        List<Task> tasksList = new ArrayList<>(tasksMap.values());
        addTasks(tasksList);
        //allTasks.putAll(getTasks());

        json = client.load("epics");
        type = new TypeToken<Map<Integer, Epic>>(){
        }.getType();
        Map<Integer, Epic> epicsMap = gson.fromJson(json, type);
        List<Epic> epicsList = new ArrayList<>(epicsMap.values());
        addTasks(epicsList);
        //allTasks.putAll(getEpics());

        json = client.load("subtasks");
        type = new TypeToken<Map<Integer, Subtask>>(){
        }.getType();
        Map<Integer, Subtask> subtasksMap = gson.fromJson(json, type);
        List<Subtask> subtasksList = new ArrayList<>(subtasksMap.values());
        addTasks(subtasksList);
        //allTasks.putAll(getSubtasks());

        json = client.load("history");
        String historyLine = json.substring(1, json.length() - 1);
        if (!historyLine.equals("\"\"")) {
            String[] historyLineContents = historyLine.split(",");
            for (String s : historyLineContents) {
                historyManager.addHistory(allTasks.get(Integer.parseInt(s)));
            }
        }
        save();

       /* ArrayList<Task> tasks = gson.fromJson(client.load("/tasks"), new TypeToken<ArrayList<Task>>() {
        }.getType());
        addTasks(tasks);

        ArrayList<Epic> epics = gson.fromJson(client.load("/epics"), new TypeToken<ArrayList<Epic>>() {
        }.getType());
        addTasks(epics);

        ArrayList<Subtask> subtasks = gson.fromJson(client.load("/subtasks"), new TypeToken<ArrayList<Subtask>>() {
        }.getType());
        addTasks(subtasks);

        ArrayList<Task> history = gson.fromJson(client.load("/history"), new TypeToken<ArrayList<Task>>() {
        }.getType());
        for (Task task : history) {
            historyManager.addHistory(task);
        }*/
    }

    @Override
    public void save() {
        String jsonTasks = gson.toJson(new ArrayList<>(tasks.values()));
        client.put("tasks", jsonTasks);

        String jsonSubtasks = gson.toJson(new ArrayList<>(subtasks.values()));
        client.put("subtasks", jsonSubtasks);

        String jsonEpics = gson.toJson(new ArrayList<>(epics.values()));
        client.put("epics", jsonEpics);

        String jsonHistory = gson.toJson(historyManager.getHistory()
                .stream()
                .map(Task::getId)
                .collect(Collectors.toList()));
        client.put("history", jsonHistory);
    }
}
