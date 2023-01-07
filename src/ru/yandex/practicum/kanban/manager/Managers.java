package ru.yandex.practicum.kanban.manager;

import com.google.gson.*;
import ru.yandex.practicum.kanban.adapter.LocalDateTimeSerializer;
import ru.yandex.practicum.kanban.history.HistoryManager;
import ru.yandex.practicum.kanban.history.InMemoryHistoryManager;
import ru.yandex.practicum.kanban.adapter.LocalDateAdapter;
import ru.yandex.practicum.kanban.http.HttpTaskManager;
import ru.yandex.practicum.kanban.http.KVServer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
        /*return new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer())
                .serializeNulls()
                .create();*/
        return new GsonBuilder()
        .registerTypeAdapter(LocalDateTime.class, (JsonSerializer<LocalDateTime>) (value, type, context) ->
                new JsonPrimitive(value.format(DateTimeFormatter.ISO_DATE_TIME)))
                .registerTypeAdapter(LocalDateTime.class, (JsonDeserializer<LocalDateTime>) (jsonElement, type, context) ->
                        LocalDateTime.parse(jsonElement.getAsJsonPrimitive()
                                .getAsString(), DateTimeFormatter.ISO_DATE_TIME))
                .serializeNulls()
                .create();
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
