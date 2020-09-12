package common.model.file;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class File {

    private String name;
    private int size;
    private Type type;
    private FilePriority priority;

    public File(String name, int size) {
        this.name = name;
        this.size = size;
        this.priority = getPriorityBySize(size);
    }

    public enum Type {
        CREATE, DELETE
    }

    public boolean isCreateType() {
        return type == Type.CREATE;
    }

    public boolean isDeleteType() {
        return type == Type.DELETE;
    }

    private FilePriority getPriorityBySize(int size) {
        if (size < 5) {
            return FilePriority.LOW;
        }

        return FilePriority.HIGH;
    }
}
