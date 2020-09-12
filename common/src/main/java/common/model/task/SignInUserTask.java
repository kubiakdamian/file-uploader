package common.model.task;

import lombok.Getter;

import static common.model.task.TaskTypeFromClient.SIGN_IN_USER;

@Getter
public class SignInUserTask extends Task {

    private final String clientName;

    public SignInUserTask(String clientName) {
        super(SIGN_IN_USER);
        this.clientName = clientName;
    }
}
