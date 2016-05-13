package pl.edu.agh.tai.web.bing.map.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.mapping.Document;
import pl.edu.agh.tai.web.bing.map.enums.Severity;
import pl.edu.agh.tai.web.bing.map.enums.Type;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;


@Document(collection = "incidents")
@JsonIgnoreProperties({"__type"})
public class IncidentItem {
    @Id
    @JsonProperty("incidentId")
    private Long incidentId;

    @JsonProperty("severity")
    private Severity severity;

    @JsonProperty("verified")
    private Boolean verified;

    @JsonProperty("description")
    private String description;

    @JsonProperty("source")
    private Integer source;

    @JsonProperty("type")
    private Type type;

    @JsonProperty("roadClosed")
    private Boolean roadClosed;

    @JsonProperty("point")
    private GeoJsonPoint point;

    @JsonProperty("pointTo")
    private GeoJsonPoint pointTo;

    private Date start;

    private Date end;

    private Date lastModified;

    public IncidentItem() {
    }

    public IncidentItem(Date lastModified, Date end, Date start, GeoJsonPoint pointTo, GeoJsonPoint point, Boolean roadClosed, String type, Integer source, String description, Boolean verified, String severity, Long incidentId) {
        this.lastModified = lastModified;
        this.end = end;
        this.start = start;
        this.pointTo = pointTo;
        this.point = point;
        this.roadClosed = roadClosed;
        this.type = Type.valueOf(type);
        this.source = source;
        this.description = description;
        this.verified = verified;
        this.severity = Severity.valueOf(severity);
        this.incidentId = incidentId;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    @JsonProperty("lastModified")
    public void setLastModified(Map<String, Object> map) {
        setLastModified(parseDate(map));
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    @JsonProperty("end")
    public void setEnd(Map<String, Object> map) {
        setEnd(parseDate(map));
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    @JsonProperty("start")
    public void setStart(Map<String, Object> map) {
        setStart(parseDate(map));
    }

    public GeoJsonPoint getPointTo() {
        return pointTo;
    }

    public void setPointTo(GeoJsonPoint pointTo) {
        this.pointTo = pointTo;
    }

    public GeoJsonPoint getPoint() {
        return point;
    }

    public void setPoint(GeoJsonPoint point) {
        this.point = point;
    }

    public Boolean getRoadClosed() {
        return roadClosed;
    }

    public void setRoadClosed(Boolean roadClosed) {
        this.roadClosed = roadClosed;
    }

    public Type getType() {
        return type;
    }

    public void setType(String type) {
        this.type = Type.valueOf(type);
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public Severity getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = Severity.valueOf(severity);
    }

    public Long getIncidentId() {
        return incidentId;
    }

    public void setIncidentId(Long incidentId) {
        this.incidentId = incidentId;
    }

    private Date parseDate(Map<String, Object> map) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        Date date = null;
        try {
            date = format.parse((String) map.get("$date"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    @Override
    public String toString() {
        return "IncidentItem{" +
                "incidentId=" + incidentId +
                ", severity=" + severity +
                ", verified=" + verified +
                ", description='" + description + '\'' +
                ", source=" + source +
                ", type=" + type +
                ", roadClosed=" + roadClosed +
                ", point=" + point +
                ", pointTo=" + pointTo +
                ", start=" + start +
                ", end=" + end +
                ", lastModified=" + lastModified +
                '}';
    }
}
