package ru.yandex.practicum.manager;

import ru.yandex.practicum.history.HistoryManager;
import ru.yandex.practicum.history.InMemoryHistoryManager;

public class Managers {

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
