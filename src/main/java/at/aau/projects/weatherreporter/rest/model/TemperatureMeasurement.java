package at.aau.projects.weatherreporter.rest.model;

import lombok.Getter;
import lombok.ToString;

import javax.annotation.Nullable;


@Getter
@ToString
public class TemperatureMeasurement {
    @Nullable
    private final Double degreeCelcius;

    @Nullable
    private final SkyState sky;

    public TemperatureMeasurement() {
        this(null, null);
    }

    public TemperatureMeasurement(
            @Nullable Double degreeCelsius,
            @Nullable SkyState sky) {
        this.degreeCelcius = degreeCelsius;
        this.sky = sky;
    }
}
