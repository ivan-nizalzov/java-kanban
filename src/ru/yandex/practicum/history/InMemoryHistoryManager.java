package ru.yandex.practicum.history;

import ru.yandex.practicum.tasks.Task;
import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private final LinkedList<Task> history = new LinkedList<>();

    //РЕВЬЮЕРУ: метод переписал с учетом замечаний (вместо списка ArrayList<>() использовал связанный LinkedList<>())
    @Override
    public void add(Task task) {
        history.add(task);
        if (history.size() > 10) {
            history.addFirst(task);
        } else {
            history.removeLast();
            history.addFirst(task);
        }
    }

    @Override
    public List<Task> getHistory() {
        return history;
    }
}
