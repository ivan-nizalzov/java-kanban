package ru.yandex.practicum.manager;

import ru.yandex.practicum.history.HistoryManager;
import ru.yandex.practicum.history.InMemoryHistoryManager;

public class Managers {

    /*Данный метод прописан из-за требований в ТЗ, при этом мне непонятно, для чего он нужен,
    т.к. он не используется в текущей версии кода
     */
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
