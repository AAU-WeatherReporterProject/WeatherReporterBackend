package at.aau.projects.weatherreporter.rest.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.annotation.Nullable;


@Getter
@Setter
@AllArgsConstructor
@ToString
public class TemperatureMeasurement {
    @Nullable
    private final Double temperature;
    @Nullable
    private final Integer humidity;
    @Nullable
    private final Double pressure;
    @Nullable
    private final SkyState skyState;
    @Nullable
    private final String timestamp;

    public TemperatureMeasurement() {
        this(null, null, null, null, null);
    }
}
