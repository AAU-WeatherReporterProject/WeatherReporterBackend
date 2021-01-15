package at.aau.projects.weatherreporter.rest.service;

import at.aau.projects.weatherreporter.rest.entity.Measurement;
import at.aau.projects.weatherreporter.rest.entity.TemperatureMeasurementPoint;
import at.aau.projects.weatherreporter.rest.model.MeasurementPoint;
import at.aau.projects.weatherreporter.rest.model.TemperatureData;
import at.aau.projects.weatherreporter.rest.model.TemperatureMeasurement;
import at.aau.projects.weatherreporter.rest.repository.MeasurementRepository;
import at.aau.projects.weatherreporter.rest.repository.TemperatureMeasurementPointRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DataServiceImpl implements DataService {

    private final MeasurementRepository measurementRepository;
    private final TemperatureMeasurementPointRepository temperatureMeasurementPointRepository;

    public DataServiceImpl(MeasurementRepository measurementRepository, TemperatureMeasurementPointRepository temperatureMeasurementPointRepository) {
        this.measurementRepository = measurementRepository;
        this.temperatureMeasurementPointRepository = temperatureMeasurementPointRepository;
    }

    @Override
    public void ingestData(TemperatureData data) {
        List<Measurement> measurementList = new ArrayList<>();
        if (data != null && data.getMetadata() != null && data.getMeasurements() != null && !data.getMeasurements().isEmpty()) {
            TemperatureMeasurementPoint point = readTemperatureMeasurementPoint(data.getMetadata().getKey());
            if(point == null)
                addMeasurementPoint(new MeasurementPoint(data.getMetadata().getKey()));

            for (TemperatureMeasurement inputMeasurement : data.getMeasurements()) {
                Measurement measurement = new Measurement();
                measurement.setTemperatureMeasurementPoint(point);
                measurement.setTemperature(inputMeasurement.getTemperature());
                measurement.setSky(inputMeasurement.getSkyState());
                measurement.setTimestamp(new Timestamp(System.currentTimeMillis()));
                measurementList.add(measurement);
            }
            measurementRepository.saveAll(measurementList);
        }
    }

    @Override
    public List<TemperatureMeasurement> readDataPoints(String from, String to, String location) {
        List<TemperatureMeasurement> temperatureMeasurements = new ArrayList<>();
        List<Measurement> measurements;
        if (location != null) {

            Timestamp timestampFrom = from != null ? Timestamp.valueOf(from) : null;
            Timestamp timestampTo = to != null ? Timestamp.valueOf(to) : null;
            if (timestampFrom != null && timestampTo != null) {
                measurements = measurementRepository.findAllByTemperatureMeasurementPoint_LocationAndTimestampBetween(location, timestampFrom, timestampTo);
            } else if (timestampFrom != null) {
                measurements = measurementRepository.findAllByTemperatureMeasurementPoint_LocationAndTimestampAfter(location, timestampFrom);
            } else if (timestampTo != null) {
                measurements = measurementRepository.findAllByTemperatureMeasurementPoint_LocationAndTimestampBefore(location, timestampTo);
            } else {
                measurements = measurementRepository.findAllByTemperatureMeasurementPoint_Location(location);
            }

            measurements.sort(Comparator.comparing(Measurement::getTimestamp).reversed());
            for (Measurement measurement : measurements) {
                temperatureMeasurements.add(new TemperatureMeasurement(measurement.getTemperature(), measurement.getSky(), measurement.getTimestamp().toString()));
            }
        }
        return temperatureMeasurements;
    }

    @Override
    public void addMeasurementPoint(MeasurementPoint measurementPoint) {
        TemperatureMeasurementPoint measurementPoint1 = readTemperatureMeasurementPoint(measurementPoint.getLocation());
        if(measurementPoint1!=null)
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST,"Measurement Point already exists");

        TemperatureMeasurementPoint point = new TemperatureMeasurementPoint();
        point.setLocation(measurementPoint.getLocation());
        temperatureMeasurementPointRepository.saveAndFlush(point);
    }

    @Override
    public List<MeasurementPoint> getAllMeasurementPoints() {
        List<TemperatureMeasurementPoint> temperatureMeasurementPoints = temperatureMeasurementPointRepository.findAll();
        return temperatureMeasurementPoints.stream()
                .map(temp -> new MeasurementPoint(temp.getLocation()))
                .collect(Collectors.toList());
    }

    //TODO if never needed again delete
    /*private String generateMeasurementKey() {
        return UUID.randomUUID().toString();
    }*/

    private TemperatureMeasurementPoint readTemperatureMeasurementPoint(String location) {
        Optional<TemperatureMeasurementPoint> optPoint = temperatureMeasurementPointRepository.findById(location);
        if (optPoint.isEmpty()) {
            return null;
        }
        return optPoint.get();
    }
}
