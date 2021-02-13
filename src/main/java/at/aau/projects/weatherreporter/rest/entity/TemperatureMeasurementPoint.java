package at.aau.projects.weatherreporter.rest.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "Temperature_Measurement_Point")
public class TemperatureMeasurementPoint {

    @Id
    @Column(length = 180)
    private String location;
    @OneToMany(
            mappedBy = "temperatureMeasurementPoint",
            cascade = CascadeType.DETACH,
            fetch = FetchType.LAZY
    )
    private Set<Measurement> measurements;

    public TemperatureMeasurementPoint(String location) {
        this.location = location;
    }
}
