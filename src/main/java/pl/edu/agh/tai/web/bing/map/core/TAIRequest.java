package pl.edu.agh.tai.web.bing.map.core;


import pl.edu.agh.tai.web.bing.map.enums.Severity;
import pl.edu.agh.tai.web.bing.map.enums.Type;

import java.util.HashSet;
import java.util.Set;

public class TAIRequest {

    private static final String DEMO_KEY = "%20Al32s_5BniM3B6eU4rVaVzTb1CeZpPwt7OZ_40mJSHTSksNCQifMtyp2XQkUphbw";
    private static final String URL_CORE = "http://dev.virtualearth.net/REST/v1/Traffic/Incidents/";

    private String key;

    private double south;
    private double west;
    private double north;
    private double east;

    private Set<Severity> severitySet;
    private Set<Type> typeSet;

    private boolean locationCodes = false;

    public TAIRequest() {
        severitySet = new HashSet<Severity>();
        typeSet = new HashSet<Type>();
        key = DEMO_KEY;
    }

    public TAIRequest addKey(String key) {
        this.key = key;
        return this;
    }

    public TAIRequest setBounds(double south, double west, double north, double east) {
        this.south = south;
        this.west = west;
        this.north = north;
        this.east = east;
        return this;
    }

    public TAIRequest addSeverity(Severity sev) {
        severitySet.add(sev);
        return this;
    }

    public TAIRequest addType(Type type) {
        typeSet.add(type);
        return this;
    }

    public TAIRequest includeLocationCodes(boolean include) {
        locationCodes = include;
        return this;
    }

    public String build() {
        StringBuilder builder = new StringBuilder(URL_CORE);

        builder.append(south).append(",");
        builder.append(west).append(",");
        builder.append(north).append(",");
        builder.append(east).append("/");

        builder.append(locationCodes ? "true" : "false").append("?");

        if (!severitySet.isEmpty()) {
            builder.append("severity=");
            for (Severity sev : severitySet) {
                builder.append(sev.value()).append(",");
            }
            builder.setCharAt(builder.length() - 1, '&');
        }

        if (!typeSet.isEmpty()) {
            builder.append("type=");
            for (Type type : typeSet) {
                builder.append(type.value()).append(",");
            }
            builder.setCharAt(builder.length() - 1, '?');
        }

        builder.append("key=").append(key);

        System.out.println(builder);
        return builder.toString();
    }

}
