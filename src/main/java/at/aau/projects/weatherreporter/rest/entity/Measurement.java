package at.aau.projects.weatherreporter.rest.entity;

import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Set;

@Getter
@ToString
@Entity
@Table(name = "Measurement")
public class Measurement {

    @Id
    private String measurement_key;
    @OneToMany(
            mappedBy = "measurement",
            cascade = CascadeType.DETACH,
            fetch = FetchType.LAZY
    )
    private Set<TemperatureMeasurementPoint> temperatureMeasurementPoints;
}
