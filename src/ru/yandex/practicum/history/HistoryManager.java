package ru.yandex.practicum.history;

import ru.yandex.practicum.tasks.Task;

import java.util.List;

public interface HistoryManager {

    void addHistory(Task task);

    void removeHistoryById(int id);

    List<Task> getHistory();

    String historyToString();
}
