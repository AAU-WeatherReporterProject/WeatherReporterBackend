package at.aau.projects.weatherreporter.rest.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.annotation.Nullable;

@Getter
@ToString
@EqualsAndHashCode
public class MeasurementPoint {

    private final String location;

    public MeasurementPoint() {
        this(null);
    }

    public MeasurementPoint(String location) {
        this.location = location;
    }
}
