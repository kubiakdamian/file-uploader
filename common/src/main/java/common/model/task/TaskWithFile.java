package common.model.task;

import common.model.file.File;
import common.model.file.FilePriority;
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

    public boolean isFilePriorityHigh() {
        return file.getPriority() == FilePriority.HIGH;
    }
}
