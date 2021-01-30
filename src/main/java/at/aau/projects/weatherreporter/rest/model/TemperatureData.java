package at.aau.projects.weatherreporter.rest.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import javax.annotation.Nullable;
import java.util.List;

@Getter
@ToString
@AllArgsConstructor
public class TemperatureData {

    @Nullable
    private final Metadata metadata;

    @Nullable
    private final List<TemperatureMeasurement> measurements;

    public TemperatureData() {
        this(null, null);
    }
}
