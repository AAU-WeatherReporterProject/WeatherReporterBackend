package at.aau.projects.weatherreporter.rest.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

@Getter
@ToString
@AllArgsConstructor
public class Metadata {
    @Nullable
    @NotNull(message = "no location given")
    private final String key;

    public Metadata() {
        this(null);
    }
}
