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
    public void shouldReturnEpicDuration() {
        Epic epic = getEpic(1);
        Subtask subtask1 = getSubtask(2, 1);
        Subtask subtask2 = getSubtask(3, 1);

        manager.addEpic(epic);
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);

        assertEquals(60, manager.getEpics().get(1).getEpicDuration());
    }
    //=================================================
    @Test
    public void shouldReturnEpicStartTime() {
        Epic epic = getEpic(1);
        Subtask subtask1 = getSubtask(2, 1);
        Subtask subtask2 = getSubtask(3, 1);

        manager.addEpic(epic);
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);

        assertEquals(LocalDateTime.of(2022, Month.NOVEMBER, 22, 18, 00),
                manager.getEpics().get(1).getEpicStartTime());
    }
    //=================================================
    @Test
    public void shouldReturnEpicEndTime() {
        Epic epic = getEpic(1);
        Subtask subtask1 = getSubtask(2, 1);
        subtask1.setStartTime(LocalDateTime.of(2022, Month.NOVEMBER, 21, 20, 00));
        Subtask subtask2 = getSubtask(3, 1);
        subtask2.setStartTime(LocalDateTime.of(2022, Month.NOVEMBER, 21, 22, 00));

        manager.addEpic(epic);
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);

        assertEquals(LocalDateTime.of(2022, Month.NOVEMBER, 21, 22, 30),
                manager.getEpics().get(1).getEpicEndTime());
    }
    //=================================================
    @Test
    public void shouldGetSubtasks() {
        Epic epic = getEpic(1);
        Subtask subtask1 = getSubtask(2, 1);
        Subtask subtask2 = getSubtask(3, 1);

        manager.addEpic(epic);
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);

        List<Subtask> testList = List.of(subtask1, subtask2);

        assertEquals(testList, manager.getEpics().get(1).getSubtasks());
    }
    //=================================================
    @Test
    public void shouldSetSubtask() {
        Epic epic = getEpic(1);
        Subtask subtask1 = getSubtask(2, 1);
        Subtask subtask2 = getSubtask(3, 1);
        subtask2.setStartTime(LocalDateTime.of(2022, Month.NOVEMBER, 21, 22, 00));
        Subtask subtask3 = getSubtask(4,1);
        subtask3.setStartTime(LocalDateTime.of(2022, Month.NOVEMBER, 21, 18, 00));

        manager.addEpic(epic);
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);

        manager.getEpics().get(1).setSubtasks(subtask3);

        assertEquals(3, manager.getEpics().get(1).getSubtasks().size());
    }
    //=================================================
    @Test
    public void shouldRemoveSubtask() {
        Epic epic = getEpic(1);
        Subtask subtask1 = getSubtask(2, 1);
        Subtask subtask2 = getSubtask(3, 1);

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
        Epic epic = getEpic(1);
        Subtask subtask1 = getSubtask(2, 1);
        subtask1.setDuration(-1);
        Subtask subtask2 = getSubtask(3, 1);
        subtask2.setDuration(-1);

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
        Epic epic = getEpic(1);
        Subtask subtask1 = getSubtask(2, 1);
        subtask1.setStartTime(null);
        Subtask subtask2 = getSubtask(3, 1);
        subtask2.setStartTime(null);

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
        Epic epic = getEpic(1);
        Subtask subtask1 = getSubtask(2, 1);
        subtask1.setStartTime(null);
        Subtask subtask2 = getSubtask(3, 1);
        subtask2.setStartTime(null);

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
        Epic epic = getEpic(1);

        manager.addEpic(epic);

        assertEquals(0, manager.getEpics().get(1).getSubtasks().size());
    }
    //=================================================
    @Test
    public void shouldThrowExceptionWhenRemoveSubtaskIsNull() {
        Epic epic = getEpic(1);
        Subtask subtask1 = getSubtask(2, 1);
        Subtask subtask2 = getSubtask(3, 1);

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