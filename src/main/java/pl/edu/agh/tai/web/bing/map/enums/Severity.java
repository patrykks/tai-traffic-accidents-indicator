package pl.edu.agh.tai.web.bing.map.enums;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Severity {
    LOWIMPACT(1),
    MINOR(2),
    MODERATE(3),
    SERIOUS(4);

    private int code;

    Severity(int code) {
        this.code = code;
    }

    @JsonValue
    public int value() {
        return code;
    }

    @JsonCreator
    public static Severity fromValue(int typeCode) {
        for (Severity c : Severity.values()) {
            if (c.value() == typeCode) {
                return c;
            }
        }
        throw new IllegalArgumentException("Invalid Severity type code: " + typeCode);
    }

}
