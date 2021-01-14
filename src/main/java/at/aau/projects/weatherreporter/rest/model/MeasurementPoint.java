package at.aau.projects.weatherreporter.rest.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.annotation.Nullable;

@Getter
@ToString
@EqualsAndHashCode
public class MeasurementPoint {

    private final String name;
    private final String location;
    @Nullable
    private final String measurementKey;

    public MeasurementPoint() {
        this(null, null, null);
    }

    public MeasurementPoint(String name, String location, String measurementKey) {
        this.name = name;
        this.location = location;
        this.measurementKey = measurementKey;
    }
}
