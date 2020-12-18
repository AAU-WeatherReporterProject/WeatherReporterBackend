package at.aau.projects.weatherreporter.rest;

import at.aau.projects.weatherreporter.rest.model.TemperatureData;
import at.aau.projects.weatherreporter.rest.model.TemperatureMeasurement;

import at.aau.projects.weatherreporter.rest.service.DataIngestService;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Nonnull;
import java.util.List;

@RestController
@RequestMapping("v1")
public class TestEndpoint {

    private DataIngestService ingestService;

    public TestEndpoint(DataIngestService ingestService )
    {
        this.ingestService = ingestService;
    }

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
