package common.model;

import java.io.Serializable;

public enum Priority implements Serializable {
    LOW(0), MEDIUM(5), HIGH(10);

    Priority(int value) {
        this.value = value;
    }

    private final int value;
}
