package at.aau.projects.weatherreporter.rest.repository;

import at.aau.projects.weatherreporter.rest.entity.TemperatureMeasurementPoint;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TemperatureMeasurementPointRepository extends JpaRepository<TemperatureMeasurementPoint, String> {

}


