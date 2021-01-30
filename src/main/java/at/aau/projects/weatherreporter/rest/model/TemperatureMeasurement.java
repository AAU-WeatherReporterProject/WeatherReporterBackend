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
    private final Integer humidity;
    @Nullable
    private final Double pressure;
    @Nullable
    private final SkyState skyState;

    private final String timestamp;

    public TemperatureMeasurement() {
        this(null, null, null, null, null);
    }

    public TemperatureMeasurement(
            @Nullable Double degreeCelsius,
            @Nullable Integer humidity, @Nullable Double pressure, @Nullable SkyState skyState,
            String timestamp) {
        this.temperature = degreeCelsius;
        this.humidity = humidity;
        this.pressure = pressure;
        this.skyState = skyState;
        this.timestamp = timestamp;
    }
}
