package at.aau.projects.weatherreporter.unittests;

import at.aau.projects.weatherreporter.rest.entity.Measurement;
import at.aau.projects.weatherreporter.rest.model.SkyState;
import at.aau.projects.weatherreporter.rest.model.TemperatureMeasurement;
import at.aau.projects.weatherreporter.rest.repository.MeasurementRepository;
import at.aau.projects.weatherreporter.rest.repository.TemperatureMeasurementPointRepository;
import at.aau.projects.weatherreporter.rest.service.DataService;
import at.aau.projects.weatherreporter.rest.service.DataServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertEquals;

@ExtendWith(MockitoExtension.class)
class DataServiceImplReadMeasurementTests {

    @Mock
    private TemperatureMeasurementPointRepository temperatureMeasurementPointRepository;
    @Mock
    private MeasurementRepository measurementRepository;
    private DataService dataService;

    private final String LOCATION = "Klagenfurt";

    @BeforeEach
    void setup() {
        dataService = new DataServiceImpl(measurementRepository, temperatureMeasurementPointRepository);
    }

    @Test
    void test_read_measurements_key_null() {
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () -> dataService.readMeasurements(null, null, null));
        assertEquals("http status", exception.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals("exception text", "400 No Location given", exception.getMessage());
    }

    @Test
    void test_read_measurements_to_null() {
        List<Measurement> measurementsKeyTo = new ArrayList<>();
        for (int i = 0; i < 4; i++)
            measurementsKeyTo.add(createMeasurement());
        when(measurementRepository.findAllByTemperatureMeasurementPointLocationAndTimestampBefore(anyString(), any(Timestamp.class)))
                .thenReturn(measurementsKeyTo);

        List<TemperatureMeasurement> temperatureMeasurements = dataService.readMeasurements(null, "2020-12-09 23:59:00", LOCATION);
        assertEquals("number of entries", 4, temperatureMeasurements.size());
    }

    @Test
    void test_read_measurements_from_null() {
        List<Measurement> measurementsKeyFrom = new ArrayList<>();
        for (int i = 0; i < 2; i++)
            measurementsKeyFrom.add(createMeasurement());
        when(measurementRepository.findAllByTemperatureMeasurementPointLocationAndTimestampAfter(anyString(), any(Timestamp.class)))
                .thenReturn(measurementsKeyFrom);

        List<TemperatureMeasurement> temperatureMeasurements = dataService.readMeasurements("2020-12-09 23:59:00", null, LOCATION);
        assertEquals("number of entries", 2, temperatureMeasurements.size());
    }

    @Test
    void test_read_measurements_with_to_from() {
        List<Measurement> measurementsKeyToFrom = new ArrayList<>();
        for (int i = 0; i < 1; i++)
            measurementsKeyToFrom.add(createMeasurement());
        when(measurementRepository.findAllByTemperatureMeasurementPointLocationAndTimestampBetween(anyString(), any(Timestamp.class), any(Timestamp.class)))
                .thenReturn(measurementsKeyToFrom);

        List<TemperatureMeasurement> temperatureMeasurements = dataService.readMeasurements("2020-12-09 23:59:00", "2020-12-09 23:59:00", LOCATION);
        assertEquals("number of entries", 1, temperatureMeasurements.size());
    }

    @Test
    void test_read_measurements_only_key() {
        List<Measurement> measurementsOnlyKey = new ArrayList<>();
        for (int i = 0; i < 5; i++)
            measurementsOnlyKey.add(createMeasurement());
        when(measurementRepository.findAllByTemperatureMeasurementPointLocation(LOCATION)).thenReturn(measurementsOnlyKey);

        List<TemperatureMeasurement> temperatureMeasurements = dataService.readMeasurements(null, null, LOCATION);
        assertEquals("number of entries", 5, temperatureMeasurements.size());
    }

    private Measurement createMeasurement() {
        Measurement measurement = new Measurement();
        measurement.setTemperature(12.0);
        measurement.setSky(SkyState.CLEAR);
        measurement.setHumidity(80);
        measurement.setPressure(980.20);
        measurement.setTimestamp(new Timestamp(System.currentTimeMillis()));
        return measurement;
    }
}
