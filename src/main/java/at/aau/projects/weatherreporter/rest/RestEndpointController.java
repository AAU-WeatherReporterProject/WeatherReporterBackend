package at.aau.projects.weatherreporter.rest;

import at.aau.projects.weatherreporter.rest.model.MeasurementPoint;
import at.aau.projects.weatherreporter.rest.model.TemperatureData;
import at.aau.projects.weatherreporter.rest.model.TemperatureMeasurement;
import at.aau.projects.weatherreporter.rest.service.DataService;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Nonnull;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("v1")
public class RestEndpointController {

    private DataService dataService;

    public RestEndpointController(@Autowired DataService dataService) {
        this.dataService = dataService;
    }

    @PostMapping(value = "/ingest")
    public void ingestData(@RequestBody @Valid TemperatureData data) {
        dataService.ingestData(data);
    }

    @PostMapping(value = "/measurementPoint")
    public ResponseEntity<String> addMeasurementPoint(@RequestBody @Nonnull MeasurementPoint measurementPoint) {
        dataService.addMeasurementPoint(measurementPoint);
        return ResponseEntity.ok("Measurement Point was successfully created!");
    }

    @GetMapping(value = "/measurementPoints")
    public List<MeasurementPoint> getAllMeasurementPoints() {
        return dataService.getAllMeasurementPoints();
    }

    @GetMapping(value = "/dataPoints")
    @Validated
    public List<TemperatureMeasurement> readMeasurements(
            @RequestParam(value = "from", required = false) @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm") String from,
            @RequestParam(value = "to", required = false) @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm") String to,
            @RequestParam(value = "key") String key) {
        return dataService.readMeasurements(from, to, key);
    }
}
