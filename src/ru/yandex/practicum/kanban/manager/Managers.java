package ru.yandex.practicum.kanban.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.yandex.practicum.kanban.history.HistoryManager;
import ru.yandex.practicum.kanban.history.InMemoryHistoryManager;
import ru.yandex.practicum.kanban.adapter.LocalDateAdapter;
import ru.yandex.practicum.kanban.http.HttpTaskManager;

import java.io.File;
import java.time.LocalDateTime;

public class Managers {

    public static TaskManager getDefaultTaskManager() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static FileBackedTaskManager getDefaultFileBackedManager() {
        return new FileBackedTaskManager();
    }

    public static Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter());
        return gsonBuilder.create();
    }

    public static HttpTaskManager getDefault() {
        HttpTaskManager httpTaskManager = new HttpTaskManager("http://localhost:8078");
        FileBackedTaskManager.loadFromLife(new File("resources/back-up.csv"));
        return httpTaskManager;
    }
}
