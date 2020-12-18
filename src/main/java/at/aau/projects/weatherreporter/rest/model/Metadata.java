package at.aau.projects.weatherreporter.rest.model;

import lombok.Getter;
import lombok.ToString;

import javax.annotation.Nullable;

@Getter
@ToString
public class Metadata {
    @Nullable
    private final String key;

    public Metadata() {
        this(null);
    }

    public Metadata(@Nullable String key) {
        this.key = key;
    }
}
