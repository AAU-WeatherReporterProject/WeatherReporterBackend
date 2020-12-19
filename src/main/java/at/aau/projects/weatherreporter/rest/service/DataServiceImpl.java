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

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DataServiceImpl implements DataService {

    @Autowired
    private MeasurementRepository measurementRepository;

    @Autowired
    private TemperatureMeasurementPointRepository temperatureMeasurementPointRepository;

    @Override
    public void ingestData(TemperatureData data) {
        List<Measurement> measurementList = new ArrayList<>();
        Optional<TemperatureMeasurementPoint> optPoint = temperatureMeasurementPointRepository.findById(data.getMetadata().getKey());
        if(optPoint.isPresent())
        {
            for(TemperatureMeasurement inputMeasurement :data.getMeasurements())
            {
                Measurement measurement = new Measurement();
                measurement.setTemperatureMeasurementPoint(optPoint.get());
                measurement.setTemperature(inputMeasurement.getTemperature());
                measurement.setSky(inputMeasurement.getSky());
                measurement.setTimestamp(Timestamp.valueOf(inputMeasurement.getTimestamp()));
                measurementList.add(measurement);
            }
        }
        measurementRepository.saveAll(measurementList);
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

        point = temperatureMeasurementPointRepository.save(point);
        return point.getMeasurementKey();
    }

    private String generateMeasurementKey()
    {
      return  UUID.randomUUID().toString();
    }



}
