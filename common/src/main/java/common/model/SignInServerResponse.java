package common.model;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class SignInServerResponse implements Serializable {

    private String directoryName;

    public SignInServerResponse(String directoryName) {
        this.directoryName = directoryName;
    }
}
