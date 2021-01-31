package at.aau.projects.weatherreporter.aws.service;

import at.aau.projects.weatherreporter.rest.model.MeasurementPoint;
import at.aau.projects.weatherreporter.rest.model.TemperatureData;
import at.aau.projects.weatherreporter.rest.model.TemperatureMeasurement;
import at.aau.projects.weatherreporter.rest.service.DataService;

import java.util.List;

public class S3Filemanager implements DataService {

    /**
     * Fetches file, updates temperature data record, reload to AWS
     * @param data
     */
    @Override
    public void ingestData(TemperatureData data) {

    }


    /**
     * Fetches measurements from
     * @param data
     */
    @Override
    public List<TemperatureMeasurement> readMeasurements(String from, String to, String location) {
        return null;
    }

    @Override
    public void addMeasurementPoint(MeasurementPoint measurementPoint) {

    }

    @Override
    public List<MeasurementPoint> getAllMeasurementPoints() {
        return null;
    }
}
