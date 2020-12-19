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
    private final SkyState sky;

    private final String timestamp;

    public TemperatureMeasurement() {
        this(null, null,null);
    }

    public TemperatureMeasurement(
            @Nullable Double degreeCelsius,
            @Nullable SkyState sky,
            String timestamp) {
        this.temperature = degreeCelsius;
        this.sky = sky;
        this.timestamp = timestamp;
    }
}
