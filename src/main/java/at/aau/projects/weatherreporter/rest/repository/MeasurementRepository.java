package at.aau.projects.weatherreporter.rest.repository;

import at.aau.projects.weatherreporter.rest.entity.Measurement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;

public interface MeasurementRepository extends JpaRepository<Measurement, Long> {

    List<Measurement> findAllByTemperatureMeasurementPoint_MeasurementKeyAndTimestampBetween(String measurementKey, Timestamp from, Timestamp to);
    List<Measurement> findAllByTemperatureMeasurementPoint_MeasurementKeyAndTimestampBefore(String measurementKey, Timestamp to);
    List<Measurement> findAllByTemperatureMeasurementPoint_MeasurementKeyAndTimestampAfter(String measurementKey, Timestamp from);
    List<Measurement> findAllByTemperatureMeasurementPoint_MeasurementKey(String measurementKey);
}


