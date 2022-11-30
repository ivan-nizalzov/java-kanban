import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import ru.yandex.practicum.kanban.manager.TaskManager;
import ru.yandex.practicum.kanban.tasks.*;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {
    protected T manager;

    public TaskManagerTest(T manager) {
        this.manager = manager;
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
    public void shouldAddTaskAndGetTaskById() {
        Task task = getTask(1);

        manager.addTask(task);
        assertFalse(manager.getTasks().isEmpty());
        assertEquals(1, manager.getTasks().size());
        assertEquals(task, manager.getTaskById(1));
    }
    //=================================================
    @Test
    public void shouldAddEpicAndGetEpicById() {
        Epic epic = getEpic(1);

        manager.addEpic(epic);
        assertFalse(manager.getEpics().isEmpty());
        assertEquals(1, manager.getEpics().size());
        assertEquals(epic, manager.getEpicById(1));
    }
    //=================================================
    @Test
    public void shouldAddSubtaskAndGetSubtaskId() {
        Epic epic = getEpic(2);
        Subtask subtask = getSubtask(3, 2);

        manager.addEpic(epic);
        manager.addSubtask(subtask);
        assertFalse(manager.getSubtasks().isEmpty());
        assertEquals(1, manager.getSubtasks().size());
        assertEquals(subtask, manager.getSubtaskById(3));
    }
    //=================================================
    @Test
    public void shouldGetAllSubtasksOfEpic() {
        Epic epic = getEpic(2);
        Subtask subtask = getSubtask(3, 2);

        manager.addEpic(epic);
        manager.addSubtask(subtask);

        assertEquals(1, manager.getEpics().size());
        assertEquals(1, manager.getSubtasks().size());
        assertEquals(epic, manager.getEpics().get(2));
        assertEquals(subtask, manager.getSubtasks().get(3));
    }
    //=================================================
    @Test
    public void shouldUpdateTask() {
        Task task = getTask(1);
        Task taskNew = getTask(1);
        taskNew.setTaskName("TaskNew");

        manager.addTask(task);
        manager.updateTask(taskNew);
        assertEquals("TaskNew", manager.getTaskById(1).getTaskName());
    }
    //=================================================
    @Test
    public void shouldUpdateEpic() {
        Epic epic = getEpic(2);
        Epic epicNew = getEpic(2);
        epicNew.setTaskName("EpicNew");

        manager.addEpic(epic);
        manager.updateEpic(epicNew);
        assertEquals("EpicNew", manager.getEpicById(2).getTaskName());
    }
    //=================================================
    @Test
    public void shouldUpdateSubtask() {
        Epic epic = getEpic(2);
        Subtask subtask = getSubtask(3, 2);
        Subtask subtaskNew = getSubtask(3, 2);
        subtaskNew.setTaskName("SubtaskNew");

        manager.addEpic(epic);
        manager.addSubtask(subtask);
        manager.updateSubtask(subtaskNew, epic);
        assertEquals("SubtaskNew", manager.getSubtaskById(3).getTaskName());
    }
    //=================================================
    @Test
    public void shouldRemoveAllTasks() {
        manager.addTask(getTask(1));
        manager.addTask(getTask(2));
        manager.removeAllTasks();
        assertTrue(manager.getTasks().isEmpty());
    }
    //=================================================
    @Test
    public void shouldRemoveAllEpics() {
        manager.addEpic(getEpic(2));
        manager.addSubtask(getSubtask(3, 2));
        manager.addSubtask(getSubtask(4, 2));

        manager.removeAllEpics();
        assertTrue(manager.getEpics().isEmpty());
    }
    //=================================================
    @Test
    public void shouldRemoveAllSubtasks() {
        manager.addEpic(getEpic(2));
        manager.addSubtask(getSubtask(3, 2));
        manager.addSubtask(getSubtask(4, 2));

        manager.removeAllSubtasks();
        assertTrue(manager.getSubtasks().isEmpty());
    }
    //=================================================
    @Test
    public void shouldRemoveTaskEpicSubtaskById() {
        manager.addTask(new Task(
                1,
                TaskType.TASK,
                "Task",
                TaskStatus.NEW,
                "Task description",
                30,
                LocalDateTime.of(2022, Month.NOVEMBER, 22, 18, 00)));
        Epic epic = new Epic(
                2,
                TaskType.EPIC,
                "Epic",
                TaskStatus.NEW,
                "Epic description");
        manager.addEpic(epic);
        manager.addSubtask(new Subtask(3, TaskType.SUBTASK,
                "Subtask",
                TaskStatus.NEW,
                "Subtask description",
                30,
                LocalDateTime.of(2022, Month.NOVEMBER, 21, 22, 00),
                2));

        manager.removeTaskById(1);
        assertTrue(manager.getTasks().isEmpty());
        manager.removeSubtaskById(3, epic);
        assertTrue(manager.getSubtasks().isEmpty());
        manager.removeEpicById(2);
        assertTrue(manager.getEpics().isEmpty());
    }
    //=================================================
    @Test
    public void shouldGetAllSubtasksByEpic() {
        Epic epic = getEpic(2);
        Subtask subtask1 = getSubtask(3, 2);
        Subtask subtask2 = getSubtask(4, 2);

        manager.addEpic(epic);
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);

        List<Subtask> testList = List.of(subtask1, subtask2);
        for (int i = 0; i < manager.getAllSubtasks(epic).size(); i++) {
            assertEquals(testList.get(i), manager.getAllSubtasks(epic).get(i));
        }
    }
    //=================================================
    @Test
    public void shouldGetHistoryWithoutRepeats() {
        Task task = getTask(1);
        Epic epic = getEpic(2);
        Subtask subtask = getSubtask(3,2 );

        manager.addTask(task);
        manager.addEpic(epic);
        manager.addSubtask(subtask);

        manager.getTaskById(1);
        manager.getSubtaskById(3);
        manager.getEpicById(2);
        manager.getSubtaskById(3);

        assertEquals(3, manager.getHistory().size());
        for (int i = 0; i < manager.getHistory().size(); i++) {
            assertEquals(i + 1, manager.getHistory().get(i).getId());
        }
    }
    //=================================================
    @Test
    public void shouldGetPrioritizedTasks() {
        Task task1 = getTask(1);
        task1.setStartTime(LocalDateTime.of(2022, Month.NOVEMBER, 21, 18, 00));
        Task task2 = getTask(2);
        task2.setStartTime(LocalDateTime.of(2022, Month.NOVEMBER, 21, 20, 00));
        Epic epic = getEpic(3);
        Subtask subtask1 = getSubtask(4, 3);
        subtask1.setStartTime(LocalDateTime.of(2022, Month.NOVEMBER, 22, 18, 00));
        Subtask subtask2 = getSubtask(5, 3);
        subtask2.setStartTime(LocalDateTime.of(2022, Month.NOVEMBER, 22, 23, 00));

        manager.addTask(task1);
        manager.addTask(task2);
        manager.addEpic(epic);
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);

        Set<Task> prioritizedTasks = manager.getPrioritizedTasks();

        assertEquals(4, manager.getPrioritizedTasks().size());
    }
    //=================================================
    @Test
    public void shouldCheckStartTimeCrossingTasksSubtasks() {
        Task task = getTask(1);
        task.setStartTime(LocalDateTime.of(2022, Month.NOVEMBER, 21, 18, 00));
        Task task2 = getTask(2);
        task2.setStartTime(LocalDateTime.of(2022, Month.NOVEMBER, 21, 20, 00));
        Epic epic = getEpic(3);
        Subtask subtask = getSubtask(4,3);
        subtask.setStartTime(LocalDateTime.of(2022, Month.NOVEMBER, 22, 18, 00));
        Subtask subtask2 = getSubtask(5, 3);
        subtask2.setStartTime(LocalDateTime.of(2022, Month.NOVEMBER, 22, 20, 00));

        manager.addTask(task);
        manager.addTask(task2);
        manager.addEpic(epic);
        manager.addSubtask(subtask);
        manager.addSubtask(subtask2);

        assertFalse(manager.checkStartTimeCrossing(task2));
        assertFalse(manager.checkStartTimeCrossing(task));
        assertFalse(manager.checkStartTimeCrossing(subtask2));
        assertFalse(manager.checkStartTimeCrossing(subtask));
    }
    //=================================================
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // ГРАНИЧНЫЕ КЕЙСЫ.
    @Test
    public void shouldThrowExceptionWhenGetTaskByWrongId() {
        Task task = getTask(1);

        //Empty Map
        NoSuchElementException ex = Assertions.assertThrows(
                NoSuchElementException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        manager.getTaskById(3);
                    }
                }
        );
        Assertions.assertEquals(ex.getMessage(), ex.getMessage());

        manager.addTask(task);

        //Wrong id
        NoSuchElementException ex2 = Assertions.assertThrows(
                NoSuchElementException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        manager.getTaskById(3);
                    }
                }
        );
        Assertions.assertEquals(ex2.getMessage(), ex2.getMessage());
    }
    //=================================================
    @Test
    public void shouldThrowExceptionWhenGetEpicByWrongId() {
        Epic epic = getEpic(2);

        //Empty Map
        NoSuchElementException ex = Assertions.assertThrows(
                NoSuchElementException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        manager.getEpicById(1);
                    }
                }
        );
        Assertions.assertEquals(ex.getMessage(), ex.getMessage());

        manager.addEpic(epic);

        //Wrong id
        NoSuchElementException ex2 = Assertions.assertThrows(
                NoSuchElementException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        manager.getEpicById(1);
                    }
                }
        );
        Assertions.assertEquals(ex2.getMessage(), ex2.getMessage());
    }
    //=================================================
    @Test
    public void shouldThrowExceptionWhenGetSubtaskByWrongId() {
        Epic epic = getEpic(2);
        Subtask subtask = getSubtask(3, 2);

        manager.addEpic(epic);

        //Empty Map
        NoSuchElementException ex = Assertions.assertThrows(
                NoSuchElementException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        manager.getSubtaskById(1);
                    }
                }
        );
        Assertions.assertEquals(ex.getMessage(), ex.getMessage());

        manager.addSubtask(subtask);

        //Wrong id
        NoSuchElementException ex2 = Assertions.assertThrows(
                NoSuchElementException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        manager.getSubtaskById(1);
                    }
                }
        );
        Assertions.assertEquals(ex2.getMessage(), ex2.getMessage());
    }
    //=================================================
    @Test
    public void shouldThrowExceptionWhenUpdateTaskIsNull() {
        Task task =getTask(1);
        Task taskNew = getTask(1);

        manager.addTask(task);

        NullPointerException ex = Assertions.assertThrows(
                NullPointerException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        manager.updateTask(null);
                        manager.getTaskById(1).getTaskName();
                    }
                }
        );
        Assertions.assertEquals(ex.getMessage(), ex.getMessage());
    }
    //=================================================
    @Test
    public void shouldThrowExceptionWhenUpdateEpicIsNull() {
        Epic epic = getEpic(2);
        Epic epicNew = getEpic(2);

        manager.addEpic(epic);

        NullPointerException ex = Assertions.assertThrows(
                NullPointerException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        manager.updateEpic(null);
                        manager.getEpicById(2).getTaskName();
                    }
                }
        );
        Assertions.assertEquals(ex.getMessage(), ex.getMessage());
    }
    //=================================================
    @Test
    public void shouldThrowExceptionWhenUpdateSubtaskIsNull() {
        Epic epic = getEpic(2);
        Subtask subtask = getSubtask(3,2 );
        Subtask subtaskNew = getSubtask(3, 2);
        subtaskNew.setTaskStatus(TaskStatus.IN_PROGRESS);

        manager.addEpic(epic);
        manager.addSubtask(subtask);

        NullPointerException ex = Assertions.assertThrows(
                NullPointerException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        manager.updateSubtask(null, epic);
                        manager.getSubtaskById(3).getTaskName();
                    }
                }
        );
        Assertions.assertEquals(ex.getMessage(), ex.getMessage());

        NullPointerException ex2 = Assertions.assertThrows(
                NullPointerException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        manager.updateSubtask(subtaskNew, null);
                        manager.getSubtaskById(3).getEpicId();
                    }
                }
        );
        Assertions.assertEquals(ex2.getMessage(), ex2.getMessage());
    }
    //=================================================
    //Boundary conditions
    @Test
    public void shouldUpdateEpicStatusBySubtasksInBoundaryConditions() {
        Epic epic = getEpic(2);

        //Empty subtasks
        manager.addEpic(epic);
        assertEquals(TaskStatus.NEW, manager.getEpics().get(2).getTaskStatus());

        //All subtasks are created with TaskStatus.NEW
        manager.addSubtask(getSubtask(3, 2));
        manager.addSubtask(getSubtask(4, 2));

        assertEquals(TaskStatus.NEW, manager.getEpics().get(2).getTaskStatus());

        //All subtasks are created with TaskStatus.DONE
        manager.updateSubtask(new Subtask(3, TaskType.SUBTASK,
                        "Subtask2",
                        TaskStatus.DONE,
                        "Subtask2 description",
                        30,
                        LocalDateTime.of(2022, Month.NOVEMBER, 22, 22, 00),
                        2),
                epic);
        manager.updateSubtask(new Subtask(4, TaskType.SUBTASK,
                        "Subtask2",
                        TaskStatus.DONE,
                        "Subtask2 description",
                        30,
                        LocalDateTime.of(2022, Month.NOVEMBER, 22, 22, 00),
                        2),
                epic);

        assertEquals(TaskStatus.DONE, manager.getEpics().get(2).getTaskStatus());

        //Subtasks are created with TaskStatus.NEW and TaskStatus.DONE
        manager.updateSubtask(new Subtask(3, TaskType.SUBTASK,
                        "Subtask1",
                        TaskStatus.NEW,
                        "Subtask1 description",
                        30,
                        LocalDateTime.of(2022, Month.NOVEMBER, 21, 22, 00),
                        2),
                epic);
        manager.updateSubtask(new Subtask(4, TaskType.SUBTASK,
                        "Subtask2",
                        TaskStatus.DONE,
                        "Subtask2 description",
                        30,
                        LocalDateTime.of(2022, Month.NOVEMBER, 22, 22, 00),
                        2),
                epic);

        assertEquals(TaskStatus.IN_PROGRESS, manager.getEpics().get(2).getTaskStatus());

        //Subtasks contain at least one subtask with TaskStatus.IN_PROGRESS
        manager.updateSubtask(new Subtask(4, TaskType.SUBTASK,
                        "Subtask2",
                        TaskStatus.IN_PROGRESS,
                        "Subtask2 description",
                        30,
                        LocalDateTime.of(2022, Month.NOVEMBER, 22, 22, 00),
                        2),
                epic);

        assertEquals(TaskStatus.IN_PROGRESS, manager.getEpics().get(2).getTaskStatus());
    }
    //=================================================
    @Test
    public void shouldAvoidDeletingWhenRemoveTaskEpicSubtaskByWrongId() {
        manager.addTask(getTask(1));
        Epic epic = getEpic(2);
        manager.addEpic(epic);
        manager.addSubtask(getSubtask(3, 2));

        assertEquals(1, manager.getTasks().size());
        manager.removeTaskById(55);
        assertEquals(1, manager.getTasks().size());

        assertEquals(1, manager.getEpics().size());
        manager.removeEpicById(55);
        assertEquals(1, manager.getEpics().size());

        assertEquals(1, manager.getSubtasks().size());
        manager.removeSubtaskById(55, epic);
        assertEquals(1, manager.getSubtasks().size());
    }
    //=================================================
    @Test
    public void shouldThrowExceptionWhenGetAllSubtasksByNullEpic() {
        Epic epic = getEpic(2);
        Subtask subtask1 = getSubtask(3, 2);
        Subtask subtask2 = getSubtask(4, 2);

        manager.addEpic(epic);
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);

        List<Subtask> testList = List.of(subtask1, subtask2);
        for (int i = 0; i < manager.getAllSubtasks(epic).size(); i++) {
            assertEquals(testList.get(i), manager.getAllSubtasks(epic).get(i));
        }

        NullPointerException ex3 = Assertions.assertThrows(
                NullPointerException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        for (int i = 0; i < manager.getAllSubtasks(epic).size(); i++) {
                            manager.getAllSubtasks(null).get(i);
                        }
                    }
                }
        );
        Assertions.assertEquals(ex3.getMessage(), ex3.getMessage());
    }
    //=================================================
    @Test
    public void shouldThrowExceptionWhenGetHistoryWithoutRepeatsByWrongId() {
        Task task = getTask(1);
        Epic epic = getEpic(2);
        Subtask subtask = getSubtask(3, 2);

        manager.addTask(task);
        manager.addEpic(epic);
        manager.addSubtask(subtask);

        manager.getTaskById(1);
        manager.getSubtaskById(3);
        manager.getEpicById(2);
        manager.getSubtaskById(3);

        //Wrong id
        NoSuchElementException ex3 = Assertions.assertThrows(
                NoSuchElementException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        for (int i = 0; i < manager.getAllSubtasks(epic).size(); i++) {
                            manager.getTaskById(12);
                            manager.getSubtaskById(12);
                            manager.getEpicById(12);
                        }
                    }
                }
        );
        Assertions.assertEquals(ex3.getMessage(), ex3.getMessage());
        //=========
        //For null
        manager.getEpics().clear();
        manager.getTasks().clear();
        manager.getSubtasks().clear();

        //Null
        NullPointerException ex4 = Assertions.assertThrows(
                NullPointerException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        for (int i = 0; i < manager.getAllSubtasks(epic).size(); i++) {
                            manager.addTask(null);
                            manager.addEpic(null);
                            manager.addSubtask(null);
                            manager.getTaskById(1);
                            manager.getSubtaskById(3);
                            manager.getEpicById(2);
                        }
                    }
                }
        );
        Assertions.assertEquals(ex4.getMessage(), ex4.getMessage());
    }
    //=================================================
    @Test
    public void shouldTrowExceptionWhenCheckStartTimeCrossingOfNullObject() {
        Task task = getTask(1);
        Epic epic = getEpic(2);
        Subtask subtask = getSubtask(3, 2);

        NullPointerException ex1 = Assertions.assertThrows(
                NullPointerException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        manager.addTask(null);
                        manager.checkStartTimeCrossing(task);
                    }
                }
        );
        Assertions.assertEquals(ex1.getMessage(), ex1.getMessage());

        NullPointerException ex2 = Assertions.assertThrows(
                NullPointerException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        manager.addSubtask(null);
                        manager.checkStartTimeCrossing(subtask);
                    }
                }
        );
        Assertions.assertEquals(ex2.getMessage(), ex2.getMessage());
    }
    //=================================================
    @Test
    public void shouldThrowExceptionWhenGetPrioritizedNullTasks() {
        Task task1 = getTask(1);
        Task task2 = getTask(2);
        Epic epic = getEpic(3);
        Subtask subtask1 = getSubtask(4, 3);
        Subtask subtask2 = getSubtask(5, 3);

        NullPointerException ex2 = Assertions.assertThrows(
                NullPointerException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        manager.addTask(null);
                        manager.addTask(null);
                        manager.addEpic(null);
                        manager.addSubtask(null);
                        manager.addSubtask(null);
                        manager.getPrioritizedTasks();
                    }
                }
        );
        Assertions.assertEquals(ex2.getMessage(), ex2.getMessage());
    }
    //=================================================
}
