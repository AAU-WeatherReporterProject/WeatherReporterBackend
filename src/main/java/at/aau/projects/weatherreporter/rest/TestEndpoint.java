package at.aau.projects.weatherreporter.rest;

import at.aau.projects.weatherreporter.rest.model.TemperatureMeasurement;
import at.aau.projects.weatherreporter.rest.model.TemperatureData;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nonnull;
import java.util.List;

@RestController
@RequestMapping("v1")
public class TestEndpoint {
    @PostMapping
    public void ingest(@RequestBody @Nonnull TemperatureData data) {
        System.out.println(data);
    }

    @GetMapping
    public List<TemperatureMeasurement> getDataPoints(
            @Param("from") String from, @Param("to") String to, @Param("key") String locationKey) {
        return List.of(
                new TemperatureMeasurement(1d, null),
                new TemperatureMeasurement(1d, null),
                new TemperatureMeasurement(1d, null),
                new TemperatureMeasurement(1d, null)
        );
    }
}
