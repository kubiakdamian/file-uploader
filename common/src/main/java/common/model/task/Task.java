package common.model.task;

import lombok.Getter;

import java.io.Serializable;
import java.util.UUID;

@Getter
public class Task implements Serializable {

    private final TaskTypeFromClient taskType;
    private final UUID taskId = UUID.randomUUID();

    public Task(TaskTypeFromClient taskType) {
        this.taskType = taskType;
    }
}
