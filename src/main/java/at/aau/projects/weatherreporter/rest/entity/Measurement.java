package at.aau.projects.weatherreporter.rest.entity;

import at.aau.projects.weatherreporter.rest.model.SkyState;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "Measurement")
public class Measurement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location", referencedColumnName = "location")
    private TemperatureMeasurementPoint temperatureMeasurementPoint;
    private Timestamp timestamp;
    private Double temperature;
    private Integer humidity;
    private Double pressure;
    private SkyState sky;

    public Measurement(TemperatureMeasurementPoint point, Timestamp timestamp, Double temperature, Integer humidity, Double pressure, SkyState skyState) {
        this.temperatureMeasurementPoint = point;
        this.timestamp = timestamp;
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
        this.sky = skyState;
    }
}
