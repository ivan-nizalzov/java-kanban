import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.kanban.history.InMemoryHistoryManager;
import ru.yandex.practicum.kanban.tasks.*;

import java.time.LocalDateTime;
import java.time.Month;

class InMemoryHistoryManagerTest {

    protected InMemoryHistoryManager manager = new InMemoryHistoryManager();

    // Конструкторы
    public Epic getEpic(int id) {
        return new Epic(
                id,
                TaskType.EPIC,
                "Epic",
                TaskStatus.NEW,
                "Epic description");
    }

    public Task getTask(int id) {
        return new Task(
                id,
                TaskType.TASK,
                "Task",
                TaskStatus.NEW,
                "Task description",
                30,
                LocalDateTime.of(2022, Month.NOVEMBER, 21, 18, 00));
    }

    public Subtask getSubtask(int idSubtask, int idEpic) {
        return new Subtask(
                idSubtask,
                TaskType.SUBTASK,
                "Subtask",
                TaskStatus.NEW,
                "Subtask description",
                30,
                LocalDateTime.of(2022, Month.NOVEMBER, 22, 18, 00),
                idEpic);
    }

    // СТАНДАРТНЫЕ КЕЙСЫ.
    @Test
    public void shouldAddHistory() {
        Epic epic = getEpic(1);
        Subtask subtask1 = getSubtask(2, 1);
        Subtask subtask2 = getSubtask(3, 1);
        Subtask subtask3 = getSubtask(4, 1);

        manager.addHistory(epic);
        manager.addHistory(subtask1);
        manager.addHistory(subtask2);
        manager.addHistory(subtask3);

        Assertions.assertEquals(4, manager.getHistory().size());
    }

    @Test
    public void shouldRemoveHistoryById() {
        Epic epic = getEpic(1);
        Subtask subtask1 = getSubtask(2, 1);
        Subtask subtask2 = getSubtask(3, 1);
        Subtask subtask3 = getSubtask(4, 1);

        manager.addHistory(epic);
        manager.addHistory(subtask1);
        manager.addHistory(subtask2);
        manager.addHistory(subtask3);

        manager.removeHistoryById(4);

        Assertions.assertEquals(3, manager.getHistory().size());
    }

    @Test
    public void shouldGetHistory() {
        Epic epic = getEpic(1);
        Subtask subtask1 = getSubtask(2, 1);
        Subtask subtask2 = getSubtask(3, 1);
        Subtask subtask3 = getSubtask(4, 1);

        manager.addHistory(epic);
        manager.addHistory(subtask1);
        manager.addHistory(subtask2);
        manager.addHistory(subtask3);

        Assertions.assertEquals(4, manager.getHistory().size());
    }

    //=================================================
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // ГРАНИЧНЫЕ КЕЙСЫ.
    //Пустая история задач
    @Test
    public void shouldReturnEmptyLinkedListWhenRemoveEmptyHistoryById() {
        Epic epic = getEpic(1);
        Subtask subtask1 = getSubtask(2, 1);
        Subtask subtask2 = getSubtask(3, 1);
        Subtask subtask3 = getSubtask(4, 1);

        Assertions.assertTrue(manager.getHistory().isEmpty());
        manager.removeHistoryById(1);
        Assertions.assertTrue(manager.getHistory().isEmpty());
    }

    @Test
    public void shouldGetEmptyHistory() {
        Assertions.assertEquals(0, manager.getHistory().size());
    }

    //=================================================
    //Дублирование
    @Test
    public void shouldGetHistoryWithoutRepeat() {
        Epic epic = getEpic(1);
        Subtask subtask1 = getSubtask(2, 1);
        Subtask subtask2 = getSubtask(3, 1);
        Subtask subtask3 = getSubtask(4, 1);

        manager.addHistory(epic);
        manager.addHistory(epic);
        manager.addHistory(subtask1);
        manager.addHistory(subtask1);
        manager.addHistory(subtask2);
        manager.addHistory(subtask2);
        manager.addHistory(subtask3);
        manager.addHistory(subtask3);

        Assertions.assertEquals(4, manager.getHistory().size());
    }

    //=================================================
    //Удаление из истории: начало, середина, конец.
    @Test
    public void shouldRemoveHistoryByIdInTheBeginning() {
        Epic epic = getEpic(1);
        Subtask subtask1 = getSubtask(2, 1);
        Subtask subtask2 = getSubtask(3, 1);
        Subtask subtask3 = getSubtask(4, 1);

        manager.addHistory(epic);
        manager.addHistory(subtask1);
        manager.addHistory(subtask2);
        manager.addHistory(subtask3);

        manager.removeHistoryById(1);

        Assertions.assertEquals(3, manager.getHistory().size());
    }

    @Test
    public void shouldRemoveHistoryByIdInTheMiddle() {
        Epic epic = getEpic(1);
        Subtask subtask1 = getSubtask(2,1 );
        Subtask subtask2 = getSubtask(3, 1);
        Subtask subtask3 = getSubtask(4, 1);

        manager.addHistory(epic);
        manager.addHistory(subtask1);
        manager.addHistory(subtask2);
        manager.addHistory(subtask3);

        manager.removeHistoryById(2);

        Assertions.assertEquals(3, manager.getHistory().size());
    }

    @Test
    public void shouldRemoveHistoryByIdInTheEnd() {
        Epic epic = getEpic(1);
        Subtask subtask1 = getSubtask(2, 1);
        Subtask subtask2 = getSubtask(3, 1);
        Subtask subtask3 = getSubtask(4, 1);

        manager.addHistory(epic);
        manager.addHistory(subtask1);
        manager.addHistory(subtask2);
        manager.addHistory(subtask3);

        manager.removeHistoryById(4);

        Assertions.assertEquals(3, manager.getHistory().size());
    }
    //=================================================
}