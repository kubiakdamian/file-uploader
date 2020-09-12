package common.model.task;

import common.model.file.File;
import lombok.Getter;

import static common.model.task.TaskTypeFromClient.ADD_FILE;

@Getter
public class SendFileTask extends Task {

    private final File file;

    public SendFileTask(File file) {
        super(ADD_FILE);
        this.file = file;
    }
}
