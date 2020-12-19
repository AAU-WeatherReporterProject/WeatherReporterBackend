package at.aau.projects.weatherreporter.rest.entity;

import at.aau.projects.weatherreporter.rest.model.SkyState;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "Measurement")
public class Measurement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "measurementKey", referencedColumnName = "measurementKey")
    private TemperatureMeasurementPoint temperatureMeasurementPoint;
    private Timestamp timestamp;
    private Double temperature;
    private SkyState sky;


}
