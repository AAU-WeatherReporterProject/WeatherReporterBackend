package at.aau.projects.weatherreporter.rest.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
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

    public TemperatureMeasurementPoint(){
    }

    public TemperatureMeasurementPoint(String measurementKey, String name, String location) {
        this.measurementKey = measurementKey;
        this.name = name;
        this.location = location;
    }
}
