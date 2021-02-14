package at.aau.projects.weatherreporter.rest.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.annotation.Nullable;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class MeasurementPoint {

    @Nullable
    @Size(max = 180, message = "location must be smaller or equal than {max} character")
    @NotEmpty(message = "no location given")
    private final String location;

    public MeasurementPoint() {
        this(null);
    }

}
