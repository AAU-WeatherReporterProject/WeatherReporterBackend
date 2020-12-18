package at.aau.projects.weatherreporter.rest.entity;

import at.aau.projects.weatherreporter.rest.model.SkyState;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@ToString
@Entity
@Table(name = "Temperature_Measurement_Point")
public class TemperatureMeasurementPoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "measurement_key", referencedColumnName = "measurement_key")
    private Measurement measurement;
    private String measurement_key;
    @Column(name = "timestamp", columnDefinition = "DATETIME")
    private LocalDateTime timestamp;
    private Long temperatur;
    private SkyState state;
}
