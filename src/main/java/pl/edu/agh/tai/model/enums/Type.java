package pl.edu.agh.tai.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Type {
    ACCIDENT(1),
    CONGESTION(2),
    DISABLED_VEHICLE(3),
    MASS_TRANSIT(4),
    MISCELLANEOUS(5),
    OTHER_NEWS(6),
    PLANNED_EVENT(7),
    ROAD_HAZARD(8),
    CONSTRUCTION(9),
    ALERT(10),
    WEATHER(11);

    private int code;

    Type(int code) {
        this.code = code;
    }

    @JsonValue
    public int value() {
        return code;
    }

    @JsonCreator
    public static Type fromValue(int typeCode) {
        for (Type c : Type.values()) {
            if (c.value() == typeCode) {
                return c;
            }
        }
        throw new IllegalArgumentException("Invalid Type type code: " + typeCode);
    }

}