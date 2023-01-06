package ru.yandex.practicum.kanban.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.yandex.practicum.kanban.adapter.LocalDateTimeSerializer;
import ru.yandex.practicum.kanban.history.HistoryManager;
import ru.yandex.practicum.kanban.history.InMemoryHistoryManager;
import ru.yandex.practicum.kanban.adapter.LocalDateAdapter;
import ru.yandex.practicum.kanban.http.HttpTaskManager;
import ru.yandex.practicum.kanban.http.KVServer;

import java.io.IOException;
import java.time.LocalDate;
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
        return gsonBuilder.create();
    }

    public static KVServer getDefaultKVServer() throws IOException {
        final KVServer kvServer = new KVServer();
        kvServer.start();
        return kvServer;
    }

    public static HttpTaskManager getDefault() {
        return new HttpTaskManager(KVServer.PORT);
    }
}
