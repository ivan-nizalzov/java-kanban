import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import ru.yandex.practicum.kanban.exception.ManagerSaveException;
import ru.yandex.practicum.kanban.history.InMemoryHistoryManager;
import ru.yandex.practicum.kanban.tasks.*;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.NoSuchElementException;

class InMemoryHistoryManagerTest {

    protected InMemoryHistoryManager manager = new InMemoryHistoryManager();

    public Task getTask() {
        return new Task(
                1,
                TaskType.TASK,
                "Task",
                TaskStatus.NEW,
                "Task description",
                30,
                LocalDateTime.of(2022, Month.NOVEMBER, 21, 18, 00));
    }

    public Epic getEpic() {
        return new Epic(
                2,
                TaskType.EPIC,
                "Epic",
                TaskStatus.NEW,
                "Epic description");
    }

    public Subtask getSubtask() {
        return new Subtask(
                3,
                TaskType.SUBTASK,
                "Subtask",
                TaskStatus.NEW,
                "Subtask description",
                30,
                LocalDateTime.of(2022, Month.NOVEMBER, 22, 18, 00),
                3);
    }

    // СТАНДАРТНЫЕ КЕЙСЫ.
    @Test
    public void shouldAddHistory() {
        Epic epic = new Epic(
                1,
                TaskType.EPIC,
                "Epic",
                TaskStatus.NEW,
                "Epic description");
        Subtask subtask1 = new Subtask(
                2,
                TaskType.SUBTASK,
                "Subtask1",
                TaskStatus.NEW,
                "Subtask1 description",
                30,
                LocalDateTime.of(2022, Month.NOVEMBER, 21, 20, 00),
                1);
        Subtask subtask2 = new Subtask(
                3,
                TaskType.SUBTASK,
                "Subtask2",
                TaskStatus.NEW,
                "Subtask2 description",
                30,
                LocalDateTime.of(2022, Month.NOVEMBER, 21, 22, 00),
                1);
        Subtask subtask3 = new Subtask(
                4,
                TaskType.SUBTASK,
                "Subtask2",
                TaskStatus.NEW,
                "Subtask2 description",
                30,
                LocalDateTime.of(2022, Month.NOVEMBER, 21, 18, 00),
                1);

        manager.addHistory(epic);
        manager.addHistory(subtask1);
        manager.addHistory(subtask2);
        manager.addHistory(subtask3);

        Assertions.assertEquals(4, manager.getHistory().size());
    }

    @Test
    public void shouldRemoveHistoryById() {
        Epic epic = new Epic(
                1,
                TaskType.EPIC,
                "Epic",
                TaskStatus.NEW,
                "Epic description");
        Subtask subtask1 = new Subtask(
                2,
                TaskType.SUBTASK,
                "Subtask1",
                TaskStatus.NEW,
                "Subtask1 description",
                30,
                LocalDateTime.of(2022, Month.NOVEMBER, 21, 20, 00),
                1);
        Subtask subtask2 = new Subtask(
                3,
                TaskType.SUBTASK,
                "Subtask2",
                TaskStatus.NEW,
                "Subtask2 description",
                30,
                LocalDateTime.of(2022, Month.NOVEMBER, 21, 22, 00),
                1);
        Subtask subtask3 = new Subtask(
                4,
                TaskType.SUBTASK,
                "Subtask2",
                TaskStatus.NEW,
                "Subtask2 description",
                30,
                LocalDateTime.of(2022, Month.NOVEMBER, 21, 18, 00),
                1);

        manager.addHistory(epic);
        manager.addHistory(subtask1);
        manager.addHistory(subtask2);
        manager.addHistory(subtask3);

        manager.removeHistoryById(4);

        Assertions.assertEquals(3, manager.getHistory().size());
    }

    @Test
    public void shouldGetHistory() {
        Epic epic = new Epic(
                1,
                TaskType.EPIC,
                "Epic",
                TaskStatus.NEW,
                "Epic description");
        Subtask subtask1 = new Subtask(
                2,
                TaskType.SUBTASK,
                "Subtask1",
                TaskStatus.NEW,
                "Subtask1 description",
                30,
                LocalDateTime.of(2022, Month.NOVEMBER, 21, 20, 00),
                1);
        Subtask subtask2 = new Subtask(
                3,
                TaskType.SUBTASK,
                "Subtask2",
                TaskStatus.NEW,
                "Subtask2 description",
                30,
                LocalDateTime.of(2022, Month.NOVEMBER, 21, 22, 00),
                1);
        Subtask subtask3 = new Subtask(
                4,
                TaskType.SUBTASK,
                "Subtask2",
                TaskStatus.NEW,
                "Subtask2 description",
                30,
                LocalDateTime.of(2022, Month.NOVEMBER, 21, 18, 00),
                1);

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
        Epic epic = new Epic(
                1,
                TaskType.EPIC,
                "Epic",
                TaskStatus.NEW,
                "Epic description");
        Subtask subtask1 = new Subtask(
                2,
                TaskType.SUBTASK,
                "Subtask1",
                TaskStatus.NEW,
                "Subtask1 description",
                30,
                LocalDateTime.of(2022, Month.NOVEMBER, 21, 20, 00),
                1);
        Subtask subtask2 = new Subtask(
                3,
                TaskType.SUBTASK,
                "Subtask2",
                TaskStatus.NEW,
                "Subtask2 description",
                30,
                LocalDateTime.of(2022, Month.NOVEMBER, 21, 22, 00),
                1);
        Subtask subtask3 = new Subtask(
                4,
                TaskType.SUBTASK,
                "Subtask2",
                TaskStatus.NEW,
                "Subtask2 description",
                30,
                LocalDateTime.of(2022, Month.NOVEMBER, 21, 18, 00),
                1);

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
        Epic epic = new Epic(
                1,
                TaskType.EPIC,
                "Epic",
                TaskStatus.NEW,
                "Epic description");
        Subtask subtask1 = new Subtask(
                2,
                TaskType.SUBTASK,
                "Subtask1",
                TaskStatus.NEW,
                "Subtask1 description",
                30,
                LocalDateTime.of(2022, Month.NOVEMBER, 21, 20, 00),
                1);
        Subtask subtask2 = new Subtask(
                3,
                TaskType.SUBTASK,
                "Subtask2",
                TaskStatus.NEW,
                "Subtask2 description",
                30,
                LocalDateTime.of(2022, Month.NOVEMBER, 21, 22, 00),
                1);
        Subtask subtask3 = new Subtask(
                4,
                TaskType.SUBTASK,
                "Subtask2",
                TaskStatus.NEW,
                "Subtask2 description",
                30,
                LocalDateTime.of(2022, Month.NOVEMBER, 21, 18, 00),
                1);

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
        Epic epic = new Epic(
                1,
                TaskType.EPIC,
                "Epic",
                TaskStatus.NEW,
                "Epic description");
        Subtask subtask1 = new Subtask(
                2,
                TaskType.SUBTASK,
                "Subtask1",
                TaskStatus.NEW,
                "Subtask1 description",
                30,
                LocalDateTime.of(2022, Month.NOVEMBER, 21, 20, 00),
                1);
        Subtask subtask2 = new Subtask(
                3,
                TaskType.SUBTASK,
                "Subtask2",
                TaskStatus.NEW,
                "Subtask2 description",
                30,
                LocalDateTime.of(2022, Month.NOVEMBER, 21, 22, 00),
                1);
        Subtask subtask3 = new Subtask(
                4,
                TaskType.SUBTASK,
                "Subtask2",
                TaskStatus.NEW,
                "Subtask2 description",
                30,
                LocalDateTime.of(2022, Month.NOVEMBER, 21, 18, 00),
                1);

        manager.addHistory(epic);
        manager.addHistory(subtask1);
        manager.addHistory(subtask2);
        manager.addHistory(subtask3);

        manager.removeHistoryById(1);

        Assertions.assertEquals(3, manager.getHistory().size());
    }

    public void shouldRemoveHistoryByIdInTheMiddle() {
        Epic epic = new Epic(
                1,
                TaskType.EPIC,
                "Epic",
                TaskStatus.NEW,
                "Epic description");
        Subtask subtask1 = new Subtask(
                2,
                TaskType.SUBTASK,
                "Subtask1",
                TaskStatus.NEW,
                "Subtask1 description",
                30,
                LocalDateTime.of(2022, Month.NOVEMBER, 21, 20, 00),
                1);
        Subtask subtask2 = new Subtask(
                3,
                TaskType.SUBTASK,
                "Subtask2",
                TaskStatus.NEW,
                "Subtask2 description",
                30,
                LocalDateTime.of(2022, Month.NOVEMBER, 21, 22, 00),
                1);
        Subtask subtask3 = new Subtask(
                4,
                TaskType.SUBTASK,
                "Subtask2",
                TaskStatus.NEW,
                "Subtask2 description",
                30,
                LocalDateTime.of(2022, Month.NOVEMBER, 21, 18, 00),
                1);

        manager.addHistory(epic);
        manager.addHistory(subtask1);
        manager.addHistory(subtask2);
        manager.addHistory(subtask3);

        manager.removeHistoryById(2);

        Assertions.assertEquals(3, manager.getHistory().size());
    }

    public void shouldRemoveHistoryByIdInTheEnd() {
        Epic epic = new Epic(
                1,
                TaskType.EPIC,
                "Epic",
                TaskStatus.NEW,
                "Epic description");
        Subtask subtask1 = new Subtask(
                2,
                TaskType.SUBTASK,
                "Subtask1",
                TaskStatus.NEW,
                "Subtask1 description",
                30,
                LocalDateTime.of(2022, Month.NOVEMBER, 21, 20, 00),
                1);
        Subtask subtask2 = new Subtask(
                3,
                TaskType.SUBTASK,
                "Subtask2",
                TaskStatus.NEW,
                "Subtask2 description",
                30,
                LocalDateTime.of(2022, Month.NOVEMBER, 21, 22, 00),
                1);
        Subtask subtask3 = new Subtask(
                4,
                TaskType.SUBTASK,
                "Subtask2",
                TaskStatus.NEW,
                "Subtask2 description",
                30,
                LocalDateTime.of(2022, Month.NOVEMBER, 21, 18, 00),
                1);

        manager.addHistory(epic);
        manager.addHistory(subtask1);
        manager.addHistory(subtask2);
        manager.addHistory(subtask3);

        manager.removeHistoryById(4);

        Assertions.assertEquals(3, manager.getHistory().size());
    }
    //=================================================
}