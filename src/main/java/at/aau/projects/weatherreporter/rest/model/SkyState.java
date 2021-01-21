package at.aau.projects.weatherreporter.rest.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum SkyState {
    SUNNY("0"),
    WINDY("1"),
    CLOUDY("2"),
    RAIN("3"),
    CLEAR("4");

    private String skyStateCode;
    SkyState(String skyStateCode)
    {
        this.skyStateCode = skyStateCode;
    }

    @JsonValue
    public String getSkyStateCode()
    {
        return this.skyStateCode;
    }
}
