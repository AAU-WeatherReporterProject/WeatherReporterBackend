package at.aau.projects.weatherreporter.rest.service;

import at.aau.projects.weatherreporter.rest.entity.Measurement;
import at.aau.projects.weatherreporter.rest.entity.TemperatureMeasurementPoint;
import at.aau.projects.weatherreporter.rest.model.MeasurementPoint;
import at.aau.projects.weatherreporter.rest.model.TemperatureData;
import at.aau.projects.weatherreporter.rest.model.TemperatureMeasurement;

import java.util.List;

public interface DataService {

    void ingestData(TemperatureData data);

    List<TemperatureMeasurement> readDataPoints(String from, String to, String measurementKey);

    String addMeasurementPoint(MeasurementPoint measurementPoint) ;

}
