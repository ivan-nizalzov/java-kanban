package ru.yandex.practicum.kanban.http;

import com.google.gson.Gson;
import ru.yandex.practicum.kanban.manager.FileBackedTaskManager;
import ru.yandex.practicum.kanban.manager.Managers;
import ru.yandex.practicum.kanban.tasks.Epic;
import ru.yandex.practicum.kanban.tasks.Subtask;
import ru.yandex.practicum.kanban.tasks.Task;
import ru.yandex.practicum.kanban.tasks.TaskType;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
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
        List<Task> tasksList = JsonConverter.convertJsonToTaskList(json);
        addTasks(tasksList);

        json = client.load("epics");
        List<Epic> epicsList = JsonConverter.convertJsonToEpicList(json);
        addTasks(epicsList);


        json = client.load("subtasks");
        List<Subtask> subtasksList = JsonConverter.convertJsonToSubtaskList(json);
        addTasks(subtasksList);

        json = client.load("history");
        String historyLine = json.substring(1, json.length() - 1);
        if (!historyLine.equals("\"\"")) {
            String[] historyLineContents = historyLine.split(",");
            for (String s : historyLineContents) {
                historyManager.addHistory(allTasks.get(Integer.parseInt(s)));
            }
        }
    }

    public void saveTasksToKVServer() {
        FileBackedTaskManager.loadFromLife(new File("resources/back-up.csv"));

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
