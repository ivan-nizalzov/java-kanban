import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import ru.yandex.practicum.kanban.exception.ManagerSaveException;
import ru.yandex.practicum.kanban.manager.InMemoryTaskManager;
import ru.yandex.practicum.kanban.tasks.*;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EpicTest {

    protected InMemoryTaskManager manager;

    @BeforeEach
    void beforeEach() {
        manager = new InMemoryTaskManager();
    }

    // СТАНДАРТНЫЕ КЕЙСЫ.
    @Test
    public void shouldReturnEpicDuration() {
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

        manager.addEpic(epic);
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);

        assertEquals(60, manager.getEpics().get(1).getEpicDuration());
    }
    //=================================================
    @Test
    public void shouldReturnEpicStartTime() {
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

        manager.addEpic(epic);
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);

        assertEquals(LocalDateTime.of(2022, Month.NOVEMBER, 21, 20, 00),
                manager.getEpics().get(1).getEpicStartTime());
    }
    //=================================================
    @Test
    public void shouldReturnEpicEndTime() {
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

        manager.addEpic(epic);
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);

        assertEquals(LocalDateTime.of(2022, Month.NOVEMBER, 21, 22, 30),
                manager.getEpics().get(1).getEpicEndTime());
    }
    //=================================================
    @Test
    public void shouldGetSubtasks() {
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

        manager.addEpic(epic);
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);

        List<Subtask> testList = List.of(subtask1, subtask2);

        assertEquals(testList, manager.getEpics().get(1).getSubtasks());
    }
    //=================================================
    @Test
    public void shouldSetSubtask() {
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

        manager.addEpic(epic);
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);

        manager.getEpics().get(1).setSubtasks(subtask3);

        assertEquals(3, manager.getEpics().get(1).getSubtasks().size());
    }
    //=================================================
    @Test
    public void shouldRemoveSubtask() {
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

        manager.addEpic(epic);
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);

        manager.getEpics().get(1).removeSubtask(subtask1);

        assertEquals(1, manager.getEpics().get(1).getSubtasks().size());
    }
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // ГРАНИЧНЫЕ КЕЙСЫ.
    @Test
    public void shouldThrowExceptionWhenEpicDurationIs0orLess() {
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
                -1,
                LocalDateTime.of(2022, Month.NOVEMBER, 21, 20, 00),
                1);
        Subtask subtask2 = new Subtask(
                3,
                TaskType.SUBTASK,
                "Subtask2",
                TaskStatus.NEW,
                "Subtask2 description",
                -1,
                LocalDateTime.of(2022, Month.NOVEMBER, 21, 22, 00),
                1);

        manager.addEpic(epic);
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);

        ManagerSaveException ex = Assertions.assertThrows(
                ManagerSaveException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        manager.getEpics().get(1).getEpicDuration();
                    }
                }
        );
        Assertions.assertEquals("Продолжительность выполнения задачи не может быть меньше нуля.",
                ex.getMessage());
    }
    //=================================================
    @Test
    public void shouldThrowExceptionWhenEpicStartTimeIsNull() {
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
                null,
                1);
        Subtask subtask2 = new Subtask(
                3,
                TaskType.SUBTASK,
                "Subtask2",
                TaskStatus.NEW,
                "Subtask2 description",
                30,
                null,
                1);

        manager.addEpic(epic);

        NullPointerException ex = Assertions.assertThrows(
                NullPointerException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        manager.addSubtask(subtask1);
                        manager.addSubtask(subtask2);
                        manager.getEpics().get(1).getEpicStartTime();
                    }
                }
        );
        Assertions.assertEquals(ex.getMessage(), ex.getMessage());
    }
    //=================================================
    @Test
    public void shouldThrowExceptionWhenEpicEndTimeIsNull() {
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
                null,
                1);
        Subtask subtask2 = new Subtask(
                3,
                TaskType.SUBTASK,
                "Subtask2",
                TaskStatus.NEW,
                "Subtask2 description",
                30,
                null,
                1);

        manager.addEpic(epic);

        NullPointerException ex = Assertions.assertThrows(
                NullPointerException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        manager.addSubtask(subtask1);
                        manager.addSubtask(subtask2);
                    }
                }
        );
        Assertions.assertEquals(ex.getMessage(), ex.getMessage());
    }
    //=================================================
    @Test
    public void shouldReturn0WhenSubtasksMapIsEmpty() {
        Epic epic = new Epic(
                1,
                TaskType.EPIC,
                "Epic",
                TaskStatus.NEW,
                "Epic description");

        manager.addEpic(epic);

        assertEquals(0, manager.getEpics().get(1).getSubtasks().size());
    }
    //=================================================
    @Test
    public void shouldThrowExceptionWhenRemoveSubtaskIsNull() {
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

        manager.addEpic(epic);
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);

        NullPointerException ex = Assertions.assertThrows(
                NullPointerException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        manager.getEpics().get(1).removeSubtask(null);
                        manager.getEpics().get(1).getEpicStartTime();
                    }
                }
        );
        Assertions.assertEquals(ex.getMessage(), ex.getMessage());
    }
    //=================================================
}