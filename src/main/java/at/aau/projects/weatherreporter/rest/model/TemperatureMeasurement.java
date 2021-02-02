package at.aau.projects.weatherreporter.rest.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.annotation.Nullable;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


@Getter
@Setter
@AllArgsConstructor
@ToString
public class TemperatureMeasurement {
    @Nullable
    @NotNull(message = "no temperature for measurement given")
    @Min(value = -60, message = "temperature must be greater or equal {value}")
    @Max(value = 100, message = "temperature must be smaller or equal {value}")
    private final Double temperature;
    @Nullable
    @Min(value = 0, message = "humidity must be greater or equal {value}")
    @Max(value = 100, message = "humidity must be smaller or equal {value}")
    private final Integer humidity;
    @Nullable
    @Min(value = 800, message = "pressure must be greater or equal {value}")
    @Max(value = 1100, message = "pressure must be smaller or equal {value}")
    private final Double pressure;
    @Nullable
    private final SkyState skyState;
    @Nullable
    private final String timestamp;

    public TemperatureMeasurement() {
        this(null, null, null, null, null);
    }
}
