package at.aau.projects.weatherreporter.rest.model;

import lombok.Getter;
import lombok.ToString;

import javax.annotation.Nullable;
import java.util.List;

@Getter
@ToString
public class TemperatureData {

    @Nullable
    private final Metadata metadata;

    @Nullable
    private final List<TemperatureMeasurement> measurements;

    public TemperatureData() {
        this(null, null);
    }

    public TemperatureData(@Nullable Metadata metadata, @Nullable List<TemperatureMeasurement> measurements) {
        this.metadata = metadata;
        this.measurements = measurements;
    }
}
