package common.model.task;

import static common.model.task.TaskTypeFromClient.SIGN_OUT_USER;

public class SignOutUserTask extends Task {

    public SignOutUserTask() {
        super(SIGN_OUT_USER);
    }
}
