package common.model.serverResponse;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class SignInServerResponse implements Serializable {

    private final String directoryName;

    public SignInServerResponse(String directoryName) {
        this.directoryName = directoryName;
    }
}
