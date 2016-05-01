package pl.edu.agh.tai.web.bing.map.enums;

/**
 * Created by mike on 4/22/16.
 */
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

    @Override
    public String toString() {
        return Integer.toString(code);
    }
}
