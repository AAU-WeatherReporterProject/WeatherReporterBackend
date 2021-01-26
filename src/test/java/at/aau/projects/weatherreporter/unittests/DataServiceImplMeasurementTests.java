package at.aau.projects.weatherreporter.unittests;

import at.aau.projects.weatherreporter.rest.entity.Measurement;
import at.aau.projects.weatherreporter.rest.entity.TemperatureMeasurementPoint;
import at.aau.projects.weatherreporter.rest.model.*;
import at.aau.projects.weatherreporter.rest.repository.MeasurementRepository;
import at.aau.projects.weatherreporter.rest.repository.TemperatureMeasurementPointRepository;
import at.aau.projects.weatherreporter.rest.service.DataService;
import at.aau.projects.weatherreporter.rest.service.DataServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.AssertionErrors.assertEquals;

@ExtendWith(MockitoExtension.class)
class DataServiceImplMeasurementTests {

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
    void test_ingest_data_null() {
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () -> {
            dataService.ingestData(null);
        });
        assertEquals("http status",exception.getStatusCode(),HttpStatus.BAD_REQUEST);
        assertEquals("exception text", "400 Missing mandatory values", exception.getMessage());
    }

    @Test
    void test_ingest_metadata_null() {
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () -> {
            dataService.ingestData(new TemperatureData());
        });
        assertEquals("http status", exception.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals("exception text", "400 Missing mandatory values", exception.getMessage());
    }


    @Test
    void test_ingest_metadata_key_null() {
        TemperatureData data = new TemperatureData(new Metadata(null),createTemperatureData().getMeasurements());
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () -> {
            dataService.ingestData(data);
        });
        assertEquals("http status", exception.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals("exception text", "400 Missing mandatory values", exception.getMessage());
    }

    @Test
    void test_ingest_measurements_null() {
        Metadata metadata = new Metadata(LOCATION);
        TemperatureData data = new TemperatureData(metadata, null);
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () -> {
            dataService.ingestData(data);
        });
        assertEquals("http status", exception.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals("exception text", "400 No Measurements given to add", exception.getMessage());
    }

    @Test
    void test_ingest_measurement_empty() {
        TemperatureData data = createTemperatureData();
        data.getMeasurements().clear();
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () -> {
            dataService.ingestData(data);
        });
        assertEquals("http status", exception.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals("exception text", "400 No Measurements given to add", exception.getMessage());
    }

    @Test
    void test_ingest_measurement() {
        TemperatureData data = createTemperatureData();
        when(temperatureMeasurementPointRepository.existsById(LOCATION)).thenReturn(Boolean.TRUE);
        dataService.ingestData(data);
        final ArgumentCaptor<List<Measurement>> listMeasurements
                = ArgumentCaptor.forClass(List.class);
        verify(measurementRepository, times(1)).saveAll(listMeasurements.capture());
        List<Measurement> measurements = listMeasurements.getValue();
        assertNotNull(measurements);
        assertEquals("number of inserted measurements", 1, measurements.size());
        Assertions.assertEquals(data.getMeasurements().get(0).getSkyState(), measurements.get(0).getSky());
        Assertions.assertEquals(data.getMeasurements().get(0).getTemperature(), measurements.get(0).getTemperature());
        assertNotNull(measurements.get(0).getTimestamp());
        Assertions.assertEquals(measurements.get(0).getTemperatureMeasurementPoint().getLocation(), createTemperaturePoint().getLocation());
    }

    @Test
    void test_ingest_measurement_and_measurement_point_not_exists_yet() {
        TemperatureData data = createTemperatureData();
        dataService.ingestData(data);
        final ArgumentCaptor<List<Measurement>> listMeasurements
                = ArgumentCaptor.forClass(List.class);

        final ArgumentCaptor<TemperatureMeasurementPoint> temperatureMeasurementPointArgumentCaptor
                = ArgumentCaptor.forClass(TemperatureMeasurementPoint.class);
        verify(temperatureMeasurementPointRepository,times(1)).saveAndFlush(temperatureMeasurementPointArgumentCaptor.capture());
        verify(measurementRepository, times(1)).saveAll(listMeasurements.capture());

        TemperatureMeasurementPoint point = temperatureMeasurementPointArgumentCaptor.getValue();
        List<Measurement> measurements = listMeasurements.getValue();
        assertNotNull(measurements);
        assertEquals("number of inserted measurements", 1, measurements.size());
        Assertions.assertEquals(data.getMeasurements().get(0).getSkyState(), measurements.get(0).getSky());
        Assertions.assertEquals(data.getMeasurements().get(0).getTemperature(), measurements.get(0).getTemperature());
        assertNotNull(measurements.get(0).getTimestamp());
        Assertions.assertEquals(measurements.get(0).getTemperatureMeasurementPoint().getLocation(), LOCATION);

        assertNotNull(point);
        assertEquals("measurement point location",LOCATION, point.getLocation());
    }

    @Test
    void test_read_measurements_key_null() {
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () -> {
            dataService.readMeasurements(null, null, null);
        });
        assertEquals("http status", exception.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals("exception text", "400 No Location given", exception.getMessage());
    }

    @Test
    void test_read_measurements_to_null() {
        List<Measurement> measurementsKeyTo = new ArrayList<>();
        for (int i = 0; i < 4; i++)
            measurementsKeyTo.add(createMeasurement());
        when(measurementRepository.findAllByTemperatureMeasurementPoint_LocationAndTimestampBefore(anyString(), any(Timestamp.class)))
                .thenReturn(measurementsKeyTo);

        List<TemperatureMeasurement> temperatureMeasurements = dataService.readMeasurements(null, "2020-12-09 23:59:00", LOCATION);
        assertEquals("number of entries", 4, temperatureMeasurements.size());
    }

    @Test
    void test_read_measurements_from_null() {
        List<Measurement> measurementsKeyFrom = new ArrayList<>();
        for (int i = 0; i < 2; i++)
            measurementsKeyFrom.add(createMeasurement());
        when(measurementRepository.findAllByTemperatureMeasurementPoint_LocationAndTimestampAfter(anyString(), any(Timestamp.class)))
                .thenReturn(measurementsKeyFrom);

        List<TemperatureMeasurement> temperatureMeasurements = dataService.readMeasurements("2020-12-09 23:59:00", null, LOCATION);
        assertEquals("number of entries", 2, temperatureMeasurements.size());
    }

    @Test
    void test_read_measurements_with_to_from() {
        List<Measurement> measurementsKeyToFrom = new ArrayList<>();
        for (int i = 0; i < 1; i++)
            measurementsKeyToFrom.add(createMeasurement());
        when(measurementRepository.findAllByTemperatureMeasurementPoint_LocationAndTimestampBetween(anyString(), any(Timestamp.class), any(Timestamp.class)))
                .thenReturn(measurementsKeyToFrom);

        List<TemperatureMeasurement> temperatureMeasurements = dataService.readMeasurements("2020-12-09 23:59:00", "2020-12-09 23:59:00", LOCATION);
        assertEquals("number of entries", 1, temperatureMeasurements.size());
    }

    @Test
    void test_read_measurements_only_key() {
        List<Measurement> measurementsOnlyKey = new ArrayList<>();
        for (int i = 0; i < 5; i++)
            measurementsOnlyKey.add(createMeasurement());
        when(measurementRepository.findAllByTemperatureMeasurementPoint_Location(LOCATION)).thenReturn(measurementsOnlyKey);

        List<TemperatureMeasurement> temperatureMeasurements = dataService.readMeasurements(null, null, LOCATION);
        assertEquals("number of entries", 5, temperatureMeasurements.size());
    }

    private TemperatureData createTemperatureData() {
        Metadata metadata = new Metadata(LOCATION);
        List<TemperatureMeasurement> measurements = new ArrayList<>();
        TemperatureMeasurement measurement = new TemperatureMeasurement(12.0, SkyState.CLEAR, null);
        measurements.add(measurement);
        return new TemperatureData(metadata, measurements);
    }

    private TemperatureMeasurementPoint createTemperaturePoint() {
        return new TemperatureMeasurementPoint(LOCATION);
    }

    private Measurement createMeasurement() {
        Measurement measurement = new Measurement();
        measurement.setTemperature(12.0);
        measurement.setSky(SkyState.CLEAR);
        measurement.setTimestamp(new Timestamp(System.currentTimeMillis()));
        return measurement;
    }
}
