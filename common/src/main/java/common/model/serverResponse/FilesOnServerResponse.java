package common.model.serverResponse;

import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@Getter
public class FilesOnServerResponse implements Serializable {

    private final List<String> files;

    public FilesOnServerResponse(List<String> files) {
        this.files = files;
    }
}
