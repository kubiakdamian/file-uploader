package common.model.task;

import static common.model.task.TaskTypeFromClient.CREATE_USER;

public class CreateUserTask extends Task {

    public CreateUserTask() {
        super(CREATE_USER);
    }
}
