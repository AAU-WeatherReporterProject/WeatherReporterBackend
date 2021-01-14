package at.aau.projects.weatherreporter.rest.model;

import lombok.Getter;
import lombok.ToString;

import javax.annotation.Nullable;


@Getter
@ToString
public class TemperatureMeasurement {
    @Nullable
    private final Double temperature;

    @Nullable
    private final SkyState skyState;

    private final String timestamp;

    public TemperatureMeasurement() {
        this(null, null, null);
    }

    public TemperatureMeasurement(
            @Nullable Double degreeCelsius,
            @Nullable SkyState skyState,
            String timestamp) {
        this.temperature = degreeCelsius;
        this.skyState = skyState;
        this.timestamp = timestamp;
    }
}
