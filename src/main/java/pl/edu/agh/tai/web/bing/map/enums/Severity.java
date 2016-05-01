package pl.edu.agh.tai.web.bing.map.enums;


public enum Severity {
    LOWIMPACT(1),
    MINOR(2),
    MODERATE(3),
    SERIOUS(4);

    private int code;

    Severity(int code){
        this.code = code;
    }

    @Override
    public String toString() {
        return Integer.toString(code);
    }
}
