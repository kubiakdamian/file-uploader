package common.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ClientData {

    private String name;
    private String directoryName;

    public ClientData(String name, String directoryName) {
        this.name = name;
        this.directoryName = directoryName;
    }
}
