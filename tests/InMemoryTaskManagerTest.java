import org.junit.jupiter.api.Test;
import ru.yandex.practicum.kanban.manager.InMemoryTaskManager;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    InMemoryTaskManager manager;
    public InMemoryTaskManagerTest() {
        super(new InMemoryTaskManager());
    }

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