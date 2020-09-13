package common.model.task;

import common.model.file.File;
import lombok.Getter;

@Getter
public class TaskWithFile extends Task {
    private final File file;

    public TaskWithFile(TaskTypeFromClient taskType, File file) {
        super(taskType);
        this.file = file;
    }

    public String getFilename() {
        return file.getName() + "#" + file.getSize();
    }
}
