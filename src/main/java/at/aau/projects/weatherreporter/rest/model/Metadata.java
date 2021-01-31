package at.aau.projects.weatherreporter.rest.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import javax.annotation.Nullable;

@Getter
@ToString
@AllArgsConstructor
public class Metadata {
    @Nullable
    private final String key;

    public Metadata() {
        this(null);
    }
}
