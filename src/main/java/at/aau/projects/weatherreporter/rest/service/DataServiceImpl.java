package at.aau.projects.weatherreporter.rest.service;

import at.aau.projects.weatherreporter.rest.entity.Measurement;
import at.aau.projects.weatherreporter.rest.entity.TemperatureMeasurementPoint;
import at.aau.projects.weatherreporter.rest.model.MeasurementPoint;
import at.aau.projects.weatherreporter.rest.model.TemperatureData;
import at.aau.projects.weatherreporter.rest.model.TemperatureMeasurement;
import at.aau.projects.weatherreporter.rest.repository.MeasurementRepository;
import at.aau.projects.weatherreporter.rest.repository.TemperatureMeasurementPointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class DataServiceImpl implements DataService {

    @Autowired
    private MeasurementRepository measurementRepository;

    @Autowired
    private TemperatureMeasurementPointRepository temperatureMeasurementPoint;

    @Override
    public void ingestData(TemperatureData data) {
    }

    @Override
    public List<TemperatureMeasurement> readDataPoints(String from, String to, String measurementKey) {
        List<TemperatureMeasurement> temperatureMeasurements = new ArrayList<>();
        List<Measurement> measurements = measurementRepository.findAll();
        for(Measurement measurement: measurements)
        {
            temperatureMeasurements.add(new TemperatureMeasurement(measurement.getTemperature(),measurement.getSky(),null ));
        }

        return temperatureMeasurements;
    }

    @Override
    public String addMeasurementPoint(MeasurementPoint measurementPoint) {
        TemperatureMeasurementPoint point = new TemperatureMeasurementPoint();
        point.setLocation(measurementPoint.getLocation());
        point.setName(measurementPoint.getName());
        point.setMeasurementKey(generateMeasurementKey());

        point = temperatureMeasurementPoint.save(point);
        return point.getMeasurementKey();
    }

    private String generateMeasurementKey()
    {
      return  UUID.randomUUID().toString();
    }



}
