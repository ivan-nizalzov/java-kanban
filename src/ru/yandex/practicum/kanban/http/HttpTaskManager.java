package ru.yandex.practicum.kanban.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import ru.yandex.practicum.kanban.adapter.LocalDateAdapter;
import ru.yandex.practicum.kanban.manager.FileBackedTaskManager;
import ru.yandex.practicum.kanban.manager.Managers;
import ru.yandex.practicum.kanban.tasks.Epic;
import ru.yandex.practicum.kanban.tasks.Subtask;
import ru.yandex.practicum.kanban.tasks.Task;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class HttpTaskManager extends FileBackedTaskManager {
    private final Gson gson = Managers.getGson();
    protected KVTaskClient kvTaskClient;
    protected String path;

    public HttpTaskManager(String path) {
        this.path = path;
    }

    public void getToken() {
        kvTaskClient = new KVTaskClient(path);
        kvTaskClient.register();
    }

    public void saveTasks() throws IOException {
        if (kvTaskClient == null) {
            System.out.println("Требуется регистрация");
            return;
        }

        FileBackedTaskManager.loadFromLife(new File("resources/back-up.csv"));

        kvTaskClient.put("/tasks", gson.toJson(getTasks().values()));
        kvTaskClient.put("/epics", gson.toJson(getEpics().values()));
        kvTaskClient.put("/subtasks", gson.toJson(getSubtasks().values()));
        kvTaskClient.put("/history", gson.toJson(getHistory()));
    }

    public void loadTasks() {
        String json = kvTaskClient.load("/tasks");
        Type type = new TypeToken<ArrayList<Task>>(){}.getType();
        ArrayList<Task> tasksList = gson.fromJson(json, type);
        for (Task task : tasksList) {
            addTaskFromKVServer(task);
        }
        allTasks.putAll(getTasks());

        json = kvTaskClient.load("/epics");
        type = new TypeToken<ArrayList<Epic>>(){}.getType();
        ArrayList<Epic> epicsList = gson.fromJson(json, type);
        for (Epic epic : epicsList) {
            addEpicFromKVServer(epic);
        }
        allTasks.putAll(getEpics());

        json = kvTaskClient.load("/subtasks");
        type = new TypeToken<ArrayList<Subtask>>(){}.getType();
        ArrayList<Subtask> subtasksList = gson.fromJson(json, type);
        for (Subtask subtask : subtasksList) {
            addSubtaskFromKVServer(subtask);
        }
        allTasks.putAll(getSubtasks());

        json = kvTaskClient.load("/history");
        String historyLine = json.substring(1, json.length() - 1);
        if (!historyLine.equals("\"\"")) {
            String[] historyLineContents = historyLine.split(",");
            for (String s : historyLineContents) {
                historyManager.addHistory(allTasks.get(Integer.parseInt(s)));
            }
        }
        save();
    }

    public int addTaskFromKVServer(Task task) {
        task.setId(task.getId());
        prioritizedTasks.add(task);
        tasks.put(task.getId(), task);
        save();
        return task.getId();
    }

    public int addEpicFromKVServer(Epic epic) {
        epic.setId(epic.getId());
        prioritizedTasks.add(epic);
        epics.put(epic.getId(), epic);
        save();
        return epic.getId();
    }

    public int addSubtaskFromKVServer(Subtask subtask) {
        subtask.setId(subtask.getId());
        prioritizedTasks.add(subtask);
        subtasks.put(subtask.getId(), subtask);
        save();
        return subtask.getId();
    }

    @Override
    public void addTask(Task task) {
        task.setId(task.getId());
        task.getStartTime();
        prioritizedTasks.add(task);
        tasks.put(task.getId(), task);
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        epic.setId(epic.getId());
        prioritizedTasks.add(epic);
        epics.put(epic.getId(), epic);
        save();
    }

    @Override
    public void addSubtask(Subtask subtask) {
        subtask.setId(subtask.getId());
        subtask.getStartTime();
        prioritizedTasks.add(subtask);
        subtasks.put(subtask.getId(), subtask);
        save();
    }


}
