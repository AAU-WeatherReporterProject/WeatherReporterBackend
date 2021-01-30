package at.aau.projects.weatherreporter.unittests;

import at.aau.projects.weatherreporter.rest.entity.Measurement;
import at.aau.projects.weatherreporter.rest.entity.TemperatureMeasurementPoint;
import at.aau.projects.weatherreporter.rest.model.Metadata;
import at.aau.projects.weatherreporter.rest.model.SkyState;
import at.aau.projects.weatherreporter.rest.model.TemperatureData;
import at.aau.projects.weatherreporter.rest.model.TemperatureMeasurement;
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
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.AssertionErrors.assertEquals;

@ExtendWith(MockitoExtension.class)
class DataServiceImplIngestMeasurementTests {

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
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () -> dataService.ingestData(null));
        assertEquals("http status",exception.getStatusCode(),HttpStatus.BAD_REQUEST);
        assertEquals("exception text", "400 Missing mandatory values", exception.getMessage());
    }

    @Test
    void test_ingest_metadata_null() {
        TemperatureData data = new TemperatureData();
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () -> dataService.ingestData(data));
        assertEquals("http status", exception.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals("exception text", "400 Missing mandatory values", exception.getMessage());
    }

    @Test
    void test_ingest_metadata_key_null() {
        TemperatureData data = new TemperatureData(new Metadata(null),createTemperatureData().getMeasurements());
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () -> dataService.ingestData(data));
        assertEquals("http status", exception.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals("exception text", "400 Missing mandatory values", exception.getMessage());
    }

    @Test
    void test_ingest_measurements_null() {
        Metadata metadata = new Metadata(LOCATION);
        TemperatureData data = new TemperatureData(metadata, null);
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () -> dataService.ingestData(data));
        assertEquals("http status", exception.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals("exception text", "400 No Measurements given to add", exception.getMessage());
    }

    @Test
    void test_ingest_measurement_empty() {
        TemperatureData data = createTemperatureData();
        data.getMeasurements().clear();
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () -> dataService.ingestData(data));
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


    private TemperatureData createTemperatureData() {
        Metadata metadata = new Metadata(LOCATION);
        List<TemperatureMeasurement> measurements = new ArrayList<>();
        TemperatureMeasurement measurement = new TemperatureMeasurement(12.0, 12, 960.0, SkyState.CLEAR, null);
        measurements.add(measurement);
        return new TemperatureData(metadata, measurements);
    }

    private TemperatureMeasurementPoint createTemperaturePoint() {
        return new TemperatureMeasurementPoint(LOCATION);
    }

}
