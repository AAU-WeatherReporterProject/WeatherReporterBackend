package at.aau.projects.weatherreporter.rest.repository;

import at.aau.projects.weatherreporter.rest.entity.Measurement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;

public interface MeasurementRepository extends JpaRepository<Measurement, Long> {

    List<Measurement> findAllByTemperatureMeasurementPoint_LocationAndTimestampBetween(String Location, Timestamp from, Timestamp to);

    List<Measurement> findAllByTemperatureMeasurementPoint_LocationAndTimestampBefore(String Location, Timestamp to);

    List<Measurement> findAllByTemperatureMeasurementPoint_LocationAndTimestampAfter(String Location, Timestamp from);

    List<Measurement> findAllByTemperatureMeasurementPoint_Location(String Location);
}


