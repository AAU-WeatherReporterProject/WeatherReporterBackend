package at.aau.projects.weatherreporter.rest.model;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class MeasurementPoint {

    private final String name;
    private final String location;

    public MeasurementPoint() {
        this(null, null);
    }

    public MeasurementPoint(String name, String location) {
        this.name= name;
        this.location = location;
    }
}
