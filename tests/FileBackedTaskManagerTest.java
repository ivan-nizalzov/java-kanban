import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.kanban.manager.FileBackedTaskManager;
import ru.yandex.practicum.kanban.tasks.*;

import java.io.File;
import java.time.LocalDateTime;
import java.time.Month;

public class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {

    FileBackedTaskManager manager = new FileBackedTaskManager(new File("resources/back-up.csv"));
    public FileBackedTaskManagerTest() {
        super(new FileBackedTaskManager(new File("resources/back-up.csv")));
    }

    // СТАНДАРТНЫЕ КЕЙСЫ.
    @Test
    void shouldLoadFromFileTasksManager() {
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
        manager.addSubtask(subtask3);

        manager.getEpicById(1);
        manager.getSubtaskById(2);
        manager.getEpicById(1);
        manager.getSubtaskById(2);

        FileBackedTaskManager newManager =
                FileBackedTaskManager.loadFromLife(new File("resources/back-up.csv"));

        Assertions.assertEquals(manager.getSubtasks().get(2).getId(), newManager.getSubtasks().get(2).getId());
        Assertions.assertEquals(manager.getSubtasks().get(3).getId(), newManager.getSubtasks().get(3).getId());
        Assertions.assertEquals(manager.getSubtasks().get(4).getId(), newManager.getSubtasks().get(4).getId());
    }
    //=================================================
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // ГРАНИЧНЫЕ КЕЙСЫ.
    // Пустой список подзадач
    @Test
    void shouldLoadFromFileTasksManagerWithEmptyTasksEpicsSubtasks() {
        FileBackedTaskManager newManager =
                FileBackedTaskManager.loadFromLife(new File("resources/back-up.csv"));

        Assertions.assertTrue(manager.getSubtasks().isEmpty());
        Assertions.assertTrue(manager.getEpics().isEmpty());
        Assertions.assertTrue(manager.getTasks().isEmpty());
        Assertions.assertTrue(newManager.getSubtasks().isEmpty());
        Assertions.assertTrue(newManager.getEpics().isEmpty());
        Assertions.assertTrue(newManager.getTasks().isEmpty());
    }
    //=================================================
    // Эпик без подзадач
    @Test
    void shouldLoadFromFileTasksManagerWithEpicWithoutSubtasks() {
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
        manager.getEpicById(1);

        FileBackedTaskManager newManager =
                FileBackedTaskManager.loadFromLife(new File("resources/back-up.csv"));

        Assertions.assertEquals(manager.getEpics().get(1), newManager.getEpics().get(1));
        Assertions.assertTrue(manager.getSubtasks().isEmpty());
        Assertions.assertTrue(newManager.getSubtasks().isEmpty());
    }
    //=================================================
    // Пустой список истории
    @Test
    void shouldLoadFromFileTasksManagerWithEmptyHistory() {
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
        manager.addSubtask(subtask3);

        FileBackedTaskManager newManager =
                FileBackedTaskManager.loadFromLife(new File("resources/back-up.csv"));

        Assertions.assertEquals(manager.getSubtasks().get(2).getId(), newManager.getSubtasks().get(2).getId());
        Assertions.assertEquals(manager.getSubtasks().get(3).getId(), newManager.getSubtasks().get(3).getId());
        Assertions.assertEquals(manager.getSubtasks().get(4).getId(), newManager.getSubtasks().get(4).getId());

        Assertions.assertTrue(manager.getHistory().isEmpty());
        Assertions.assertTrue(newManager.getHistory().isEmpty());
    }
    //=================================================
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // Переопределение тестов
    // СТАНДАРТНЫЕ КЕЙСЫ.
    @Override
    @Test
    public void shouldAddTaskAndGetTaskById() {
        super.shouldAddTaskAndGetTaskById();
    }
    //=================================================
    @Override
    @Test
    public void shouldAddEpicAndGetEpicById() {
        super.shouldAddEpicAndGetEpicById();
    }
    //=================================================
    @Override
    @Test
    public void shouldAddSubtaskAndGetSubtaskId() {
        super.shouldAddSubtaskAndGetSubtaskId();
    }
    //=================================================
    @Override
    @Test
    public void shouldGetAllSubtasksOfEpic() {
        super.shouldGetAllSubtasksOfEpic();
    }
    //=================================================
    @Override
    @Test
    public void shouldUpdateTask() {
       super.shouldUpdateTask();
    }
    //=================================================
    @Override
    @Test
    public void shouldUpdateEpic() {
       super.shouldUpdateEpic();
    }
    //=================================================
    @Override
    @Test
    public void shouldUpdateSubtask() {
       super.shouldUpdateSubtask();
    }
    //=================================================
    @Override
    @Test
    public void shouldRemoveAllTasks() {
      super.shouldRemoveAllTasks();
    }
    //=================================================
    @Override
    @Test
    public void shouldRemoveAllEpics() {
      super.shouldRemoveAllEpics();
    }
    //=================================================
    @Override
    @Test
    public void shouldRemoveAllSubtasks() {
       super.shouldRemoveAllSubtasks();
    }
    //=================================================
    @Override
    @Test
    public void shouldRemoveTaskEpicSubtaskById() {
       super.shouldRemoveTaskEpicSubtaskById();
    }
    //=================================================
    @Override
    @Test
    public void shouldGetAllSubtasksByEpic() {
        super.shouldGetAllSubtasksByEpic();
    }
    //=================================================
    @Override
    @Test
    public void shouldGetHistoryWithoutRepeats() {
       super.shouldGetHistoryWithoutRepeats();
    }
    //=================================================
    @Override
    @Test
    public void shouldCheckStartTimeCrossing() {
     super.shouldCheckStartTimeCrossing();
    }
    //=================================================
    @Override
    @Test
    public void shouldGetPrioritizedTasks() {
        super.shouldGetPrioritizedTasks();
    }
    //=================================================
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // ГРАНИЧНЫЕ КЕЙСЫ.
    @Override
    @Test
    public void shouldThrowExceptionWhenGetTaskByWrongId() {
        super.shouldThrowExceptionWhenGetTaskByWrongId();
    }
    //=================================================
    @Override
    @Test
    public void shouldThrowExceptionWhenGetEpicByWrongId() {
       super.shouldThrowExceptionWhenGetEpicByWrongId();
    }
    //=================================================
    @Override
    @Test
    public void shouldThrowExceptionWhenGetSubtaskByWrongId() {
       super.shouldThrowExceptionWhenGetSubtaskByWrongId();
    }
    //=================================================
    @Override
    @Test
    public void shouldThrowExceptionWhenUpdateTaskIsNull() {
        super.shouldThrowExceptionWhenUpdateTaskIsNull();
    }
    //=================================================
    @Override
    @Test
    public void shouldThrowExceptionWhenUpdateEpicIsNull() {
        super.shouldThrowExceptionWhenUpdateEpicIsNull();
    }
    //=================================================
    @Override
    @Test
    public void shouldThrowExceptionWhenUpdateSubtaskIsNull() {
      super.shouldThrowExceptionWhenUpdateSubtaskIsNull();
    }
    //=================================================
    //Boundary conditions
    @Override
    @Test
    public void shouldUpdateEpicStatusBySubtasksInBoundaryConditions() {
       super.shouldUpdateEpicStatusBySubtasksInBoundaryConditions();
    }
    //=================================================
    @Override
    @Test
    public void shouldThrowExceptionWhenRemoveTaskEpicSubtaskByWrongId() {
        super.shouldThrowExceptionWhenRemoveTaskEpicSubtaskByWrongId();
    }
    //=================================================
    @Override
    @Test
    public void shouldThrowExceptionWhenGetAllSubtasksByNullEpic() {
        super.shouldThrowExceptionWhenGetAllSubtasksByNullEpic();
    }
    //=================================================
    @Override
    @Test
    public void shouldThrowExceptionWhenGetHistoryWithoutRepeatsByWrongId() {
       super.shouldThrowExceptionWhenGetHistoryWithoutRepeatsByWrongId();
    }
    //=================================================
    @Override
    @Test
    public void shouldTrowExceptionWhenCheckStartTimeCrossingOfNullObject() {
       super.shouldTrowExceptionWhenCheckStartTimeCrossingOfNullObject();
    }
    //=================================================
    @Override
    @Test
    public void shouldThrowExceptionWhenGetPrioritizedNullTasks() {
        super.shouldThrowExceptionWhenGetPrioritizedNullTasks();
    }
    //=================================================
}