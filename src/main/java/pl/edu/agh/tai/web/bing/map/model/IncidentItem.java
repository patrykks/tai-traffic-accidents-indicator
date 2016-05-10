package pl.edu.agh.tai.web.bing.map.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.mapping.Document;
import pl.edu.agh.tai.web.bing.map.enums.Severity;
import pl.edu.agh.tai.web.bing.map.enums.Type;

import java.util.Date;

/**
 * Created by root on 4/05/16.
 */
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

    @JsonProperty("toPoint")
    private GeoJsonPoint pointTo;


    @JsonProperty("start")
    private Date start;


    @JsonProperty("end")
    private Date end;


    @JsonProperty("lastModified")
    private Date lastModified;

    public IncidentItem() {}

    public IncidentItem(Date lastModified, Date end, Date start, GeoJsonPoint pointTo, GeoJsonPoint point, Boolean roadClosed, Type type, Integer source, String description, Boolean verified, Severity severity, Long incidentId) {
        this.lastModified = lastModified;
        this.end = end;
        this.start = start;
        this.pointTo = pointTo;
        this.point = point;
        this.roadClosed = roadClosed;
        this.type = type;
        this.source = source;
        this.description = description;
        this.verified = verified;
        this.severity = severity;
        this.incidentId = incidentId;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
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

    public void setType(Type type) {
        this.type = type;
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

    public void setSeverity(Severity severity) {
        this.severity = severity;
    }

    public Long getIncidentId() {
        return incidentId;
    }

    public void setIncidentId(Long incidentId) {
        this.incidentId = incidentId;
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
