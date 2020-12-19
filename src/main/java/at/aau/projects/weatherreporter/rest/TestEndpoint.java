package at.aau.projects.weatherreporter.rest;

import at.aau.projects.weatherreporter.rest.model.MeasurementPoint;
import at.aau.projects.weatherreporter.rest.model.TemperatureData;
import at.aau.projects.weatherreporter.rest.model.TemperatureMeasurement;

import at.aau.projects.weatherreporter.rest.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private DataService dataService;

    @PostMapping(value = "/ingest")
    public void ingest(@RequestBody @Nonnull TemperatureData data) {
        System.out.println(data);
    }

    @PostMapping(value = "/measurementPoint")
    public String addMeasurementPoint(@RequestBody @Nonnull MeasurementPoint measurementPoint) {
        return dataService.addMeasurementPoint(measurementPoint);
    }

    @GetMapping(value = "/dataPoints")
    public List<TemperatureMeasurement> getDataPoints(
            @Param("from") String from, @Param("to") String to, @Param("key") String measurementKey) {
       return  dataService.readDataPoints(from,to,measurementKey);
    }
}
