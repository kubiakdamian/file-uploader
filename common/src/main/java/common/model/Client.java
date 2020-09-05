package common.model;

import lombok.Getter;

@Getter
public class Client {

    private final String name;
    private final String directoryName;

    public Client(String name, String directoryName) {
        this.name = name;
        this.directoryName = directoryName;
    }
}
