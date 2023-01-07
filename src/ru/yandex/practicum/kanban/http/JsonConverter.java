package ru.yandex.practicum.kanban.http;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import ru.yandex.practicum.kanban.manager.Managers;
import ru.yandex.practicum.kanban.tasks.Epic;
import ru.yandex.practicum.kanban.tasks.Subtask;
import ru.yandex.practicum.kanban.tasks.Task;
import ru.yandex.practicum.kanban.tasks.TaskType;

import java.util.LinkedHashMap;
import java.util.List;

public class JsonConverter {
    private static final Gson gson = Managers.getGson();

    public static List<Task> convertJsonToTaskList(String json) {
        return gson.fromJson(json, new TypeToken<List<Task>>() {
        }.getType());
    }

    public static List<Epic> convertJsonToEpicList(String json) {
        return gson.fromJson(json, new TypeToken<List<Epic>>() {
        }.getType());
    }

    public static List<Subtask> convertJsonToSubtaskList(String json) {
        return gson.fromJson(json, new TypeToken<List<Subtask>>() {
        }.getType());
    }
}
