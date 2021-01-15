package at.aau.projects.weatherreporter.rest.service;

import at.aau.projects.weatherreporter.rest.model.MeasurementPoint;
import at.aau.projects.weatherreporter.rest.model.TemperatureData;
import at.aau.projects.weatherreporter.rest.model.TemperatureMeasurement;

import java.util.List;

public interface DataService {
    void ingestData(TemperatureData data);

    List<TemperatureMeasurement> readDataPoints(String from, String to, String measurementKey);

    void addMeasurementPoint(MeasurementPoint measurementPoint);

    List<MeasurementPoint> getAllMeasurementPoints();
}
