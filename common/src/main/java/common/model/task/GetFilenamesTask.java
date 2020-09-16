package common.model.task;

import static common.model.task.TaskTypeFromClient.GET_FILENAMES;

public class GetFilenamesTask extends Task {
    
    public GetFilenamesTask() {
        super(GET_FILENAMES);
    }
}
