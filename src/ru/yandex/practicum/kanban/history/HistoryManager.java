package ru.yandex.practicum.kanban.history;

import ru.yandex.practicum.kanban.tasks.Task;

import java.util.List;

public interface HistoryManager {

    void addHistory(Task task);

    void removeHistoryById(int id);

    List<Task> getHistory();

    String historyToString();
}
