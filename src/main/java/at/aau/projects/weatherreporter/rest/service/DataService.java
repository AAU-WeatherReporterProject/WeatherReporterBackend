package at.aau.projects.weatherreporter.rest.service;

import at.aau.projects.weatherreporter.rest.model.MeasurementPoint;
import at.aau.projects.weatherreporter.rest.model.TemperatureData;
import at.aau.projects.weatherreporter.rest.model.TemperatureMeasurement;

import java.util.List;

public interface DataService {

    /**
     * inserts measurements to the given location.
     *
     * @param data contains the measurements and the location
     */
    void ingestData(TemperatureData data);

    /**
     * reads the measurement for the given location and time period.
     *
     * @param from     optional parameter
     * @param to       optional parameter
     * @param location identifies the location
     * @return list of temperature measurements
     */
    List<TemperatureMeasurement> readMeasurements(String from, String to, String location);

    /**
     * adds the given measurement point to the database.
     *
     * @param measurementPoint to be added to database.
     */
    void addMeasurementPoint(MeasurementPoint measurementPoint);

    /**
     * returns a list of all measurement points.
     *
     * @return a list of measurement points
     */
    List<MeasurementPoint> getAllMeasurementPoints();
}
