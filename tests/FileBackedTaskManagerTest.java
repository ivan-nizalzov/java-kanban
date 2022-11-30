import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.kanban.manager.FileBackedTaskManager;
import ru.yandex.practicum.kanban.tasks.*;

import java.io.File;
import java.time.LocalDateTime;
import java.time.Month;

public class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {

    protected FileBackedTaskManager manager = new FileBackedTaskManager(new File("resources/back-up.csv"));
    protected FileBackedTaskManagerTest() {
        super(new FileBackedTaskManager(new File("resources/back-up.csv")));
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
    void shouldLoadFromFileTasksManager() {
        Epic epic = getEpic(1);
        Subtask subtask1 = getSubtask(2, 1);
        Subtask subtask2 = getSubtask(3, 1);
        Subtask subtask3 = getSubtask(4, 1);

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
    }
    //=================================================
    // Эпик без подзадач
    @Test
    void shouldLoadFromFileTasksManagerWithEpicWithoutSubtasks() {
        Epic epic = getEpic(1);
        Subtask subtask1 = getSubtask(2, 1);
        Subtask subtask2 = getSubtask(3, 1);
        Subtask subtask3 = getSubtask(4, 1);
        Task task = getTask(5);

        manager.addEpic(epic);
        manager.addTask(task);
        manager.getEpicById(1);
        manager.getTaskById(5);

        FileBackedTaskManager newManager = FileBackedTaskManager
                .loadFromLife(new File("resources/back-up.csv"));

        Assertions.assertEquals(manager.getEpics().get(1), newManager.getEpics().get(1));
        Assertions.assertTrue(manager.getSubtasks().isEmpty());
        Assertions.assertTrue(newManager.getSubtasks().isEmpty());
    }
    //=================================================
    // Пустой список истории
    @Test
    void shouldLoadFromFileTasksManagerWithEmptyHistory() {
        Epic epic = getEpic(1);
        Subtask subtask1 = getSubtask(2, 1);
        Subtask subtask2 = getSubtask(3, 1);
        Subtask subtask3 = getSubtask(4, 1);

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
    public void shouldGetPrioritizedTasks() {
        super.shouldGetPrioritizedTasks();
    }
    //=================================================
    @Override
    @Test
    public void shouldCheckStartTimeCrossingTasksSubtasks() {
        super.shouldCheckStartTimeCrossingTasksSubtasks();
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
    public void shouldAvoidDeletingWhenRemoveTaskEpicSubtaskByWrongId() {
        super.shouldAvoidDeletingWhenRemoveTaskEpicSubtaskByWrongId();
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