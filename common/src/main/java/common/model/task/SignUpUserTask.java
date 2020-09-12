package common.model.task;

import lombok.Getter;

@Getter
public class SignUpUserTask extends Task {

    private final String userName;
    private final String directoryName;

    public SignUpUserTask(String userName, String directoryName) {
        super(TaskTypeFromClient.CREATE_USER);
        this.userName = userName;
        this.directoryName = directoryName;
    }
}
