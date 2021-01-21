package at.aau.projects.weatherreporter.rest.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;

import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "Temperature_Measurement_Point")
public class TemperatureMeasurementPoint {

    @Id
    private String location;
    @OneToMany(
            mappedBy = "temperatureMeasurementPoint",
            cascade = CascadeType.DETACH,
            fetch = FetchType.LAZY
    )
    private Set<Measurement> measurements;

    public TemperatureMeasurementPoint() {
    }

    public TemperatureMeasurementPoint(String location) {
        this.location = location;
    }
}
