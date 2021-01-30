package at.aau.projects.weatherreporter.rest.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.annotation.Nullable;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class MeasurementPoint {

    @Nullable
    private final String location;

    public MeasurementPoint() {
        this(null);
    }

}
