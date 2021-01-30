package at.aau.projects.weatherreporter.rest.repository;

import at.aau.projects.weatherreporter.rest.entity.Measurement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;

public interface MeasurementRepository extends JpaRepository<Measurement, Long> {

    List<Measurement> findAllByTemperatureMeasurementPointLocationAndTimestampBetween(String location, Timestamp from, Timestamp to);

    List<Measurement> findAllByTemperatureMeasurementPointLocationAndTimestampBefore(String location, Timestamp to);

    List<Measurement> findAllByTemperatureMeasurementPointLocationAndTimestampAfter(String location, Timestamp from);

    List<Measurement> findAllByTemperatureMeasurementPointLocation(String location);
}


