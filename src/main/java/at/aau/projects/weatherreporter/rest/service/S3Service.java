package at.aau.projects.weatherreporter.rest.service;

import at.aau.projects.weatherreporter.rest.model.MeasurementPoint;
import at.aau.projects.weatherreporter.rest.model.TemperatureData;
import at.aau.projects.weatherreporter.rest.model.TemperatureMeasurement;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation to use S3
 */
@Service("DataService")
@Profile("S3Bucket")
public class S3Service implements DataService {

    @Override
    public void ingestData(TemperatureData data) {

    }

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
