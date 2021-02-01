package at.aau.projects.weatherreporter.rest;

import at.aau.projects.weatherreporter.rest.model.MeasurementPoint;
import at.aau.projects.weatherreporter.rest.model.TemperatureData;
import at.aau.projects.weatherreporter.rest.model.TemperatureMeasurement;
import at.aau.projects.weatherreporter.rest.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;


import javax.annotation.Nonnull;
import java.util.List;

@RestController
@RequestMapping("v1")
public class RestEndpointController {

    private DataService dataService;

    public RestEndpointController(@Autowired DataService dataService) {
        this.dataService = dataService;
    }

    @PostMapping(value = "/ingest")
    public void ingest(@RequestBody @Nonnull TemperatureData data) {
        dataService.ingestData(data);
    }

    @PostMapping(value = "/measurementPoint")
    public ResponseEntity<String> addMeasurementPoint(@RequestBody @Nonnull MeasurementPoint measurementPoint) {
        dataService.addMeasurementPoint(measurementPoint);
        return ResponseEntity.ok("Measurement Point was successfully created!");
    }

    @GetMapping(value = "/measurementPoints")
    public List<MeasurementPoint> getMeasurementPoints() {
        return dataService.getAllMeasurementPoints();
    }

    @GetMapping(value = "/dataPoints")
    public List<TemperatureMeasurement> getDataPoints(
            @Param("from") String from, @Param("to") String to, @Param("key") String key) {
        return dataService.readMeasurements(from, to, key);
    }
}