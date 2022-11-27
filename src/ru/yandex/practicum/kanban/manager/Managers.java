package ru.yandex.practicum.kanban.manager;

import ru.yandex.practicum.kanban.history.HistoryManager;
import ru.yandex.practicum.kanban.history.InMemoryHistoryManager;

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
}
