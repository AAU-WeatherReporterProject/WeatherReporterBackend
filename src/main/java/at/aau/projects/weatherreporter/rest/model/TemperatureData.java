package at.aau.projects.weatherreporter.rest.model;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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
    @NotNull(message = "no metadata given")
    @Valid
    private final Metadata metadata;

    @Nullable
    @NotEmpty(message = "no measurements given")
    @Valid
    private final List<TemperatureMeasurement> measurements;

    public TemperatureData() {
        this(null, null);
    }
}
