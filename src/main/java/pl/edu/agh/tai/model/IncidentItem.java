package pl.edu.agh.tai.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.mapping.Document;
import pl.edu.agh.tai.model.enums.Severity;
import pl.edu.agh.tai.model.enums.Type;

import java.util.Date;

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

    @JsonProperty("congestion")
    private String congestion;

    @JsonProperty("detour")
    private String detour;

    @JsonProperty("lane")
    private String lane;

    @JsonProperty("type")
    private Type type;

    @JsonProperty("roadClosed")
    private Boolean roadClosed;

    @JsonProperty("point")
    private GeoJsonPoint point;

    @JsonProperty("toPoint")
    private GeoJsonPoint toPoint;

    @JsonProperty("start")
    private Date start;

    @JsonProperty("end")
    private Date end;

    @JsonProperty("lastModified")
    private Date lastModified;

    public IncidentItem() {
    }

    public IncidentItem(Date lastModified, Date end, Date start, GeoJsonPoint toPoint, GeoJsonPoint point, Boolean roadClosed, Type type, Integer source, String congestion, String lane, String detour, String description, Boolean verified, Severity severity, Long incidentId) {
        this.lastModified = lastModified;
        this.end = end;
        this.start = start;
        this.toPoint = toPoint;
        this.point = point;
        this.roadClosed = roadClosed;
        this.type = type;
        this.source = source;
        this.congestion = congestion;
        this.lane = lane;
        this.detour = detour;
        this.description = description;
        this.verified = verified;
        this.severity = severity;
        this.incidentId = incidentId;
    }

    public Long getIncidentId() {
        return incidentId;
    }

    public void setIncidentId(Long incidentId) {
        this.incidentId = incidentId;
    }

    public Severity getSeverity() {
        return severity;
    }

    public void setSeverity(Severity severity) {
        this.severity = severity;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCongestion() {
        return congestion;
    }

    public void setCongestion(String congestion) {
        this.congestion = congestion;
    }

    public String getDetour() {
        return detour;
    }

    public void setDetour(String detour) {
        this.detour = detour;
    }

    public String getLane() {
        return lane;
    }

    public void setLane(String lane) {
        this.lane = lane;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public Boolean getRoadClosed() {
        return roadClosed;
    }

    public void setRoadClosed(Boolean roadClosed) {
        this.roadClosed = roadClosed;
    }

    public GeoJsonPoint getPoint() {
        return point;
    }

    public void setPoint(GeoJsonPoint point) {
        this.point = point;
    }

    public GeoJsonPoint getToPoint() {
        return toPoint;
    }

    public void setToPoint(GeoJsonPoint toPoint) {
        this.toPoint = toPoint;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    @Override
    public String toString() {
        return "IncidentItem{" +
                "incidentId=" + incidentId +
                ", severity=" + severity +
                ", verified=" + verified +
                ", description='" + description + '\'' +
                ", source=" + source +
                ", congestion='" + congestion + '\'' +
                ", detour='" + detour + '\'' +
                ", lane='" + lane + '\'' +
                ", type=" + type +
                ", roadClosed=" + roadClosed +
                ", point=" + point +
                ", toPoint=" + toPoint +
                ", start=" + start +
                ", end=" + end +
                ", lastModified=" + lastModified +
                '}';
    }
}
