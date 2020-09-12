package common.model.task;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class Task implements Serializable {

    TaskTypeFromClient taskType;

    public Task(TaskTypeFromClient taskType) {
        this.taskType = taskType;
    }
}
