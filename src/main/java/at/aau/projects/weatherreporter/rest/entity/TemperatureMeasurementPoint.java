package at.aau.projects.weatherreporter.rest.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "Temperature_Measurement_Point")
public class TemperatureMeasurementPoint {

    @Id
    private String measurementKey;
    @OneToMany(
            mappedBy = "temperatureMeasurementPoint",
            cascade = CascadeType.DETACH,
            fetch = FetchType.LAZY
    )
    private Set<Measurement> measurements;
    private String name;
    private String location;
}
