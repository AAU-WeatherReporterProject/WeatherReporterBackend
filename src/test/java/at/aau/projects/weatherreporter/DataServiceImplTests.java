package at.aau.projects.weatherreporter;

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
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
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

@SpringBootTest
class DataServiceImplTests {

    @Mock
    private TemperatureMeasurementPointRepository temperatureMeasurementPointRepository;
    @Mock
    private MeasurementRepository measurementRepository;
    private DataService dataService;

    private final String MEASUREMENT_KEY ="123456789";

    @BeforeEach
    void setup()
    {
        dataService = new DataServiceImpl(measurementRepository,temperatureMeasurementPointRepository);
    }

    @Test
    void test_get_all_measurement_points() {
        List<MeasurementPoint> measurementPoints_expected = new ArrayList<>();
        measurementPoints_expected.add(new MeasurementPoint("name","locaiton","123456789"));

        List<TemperatureMeasurementPoint> temperatureMeasurementPoints = new ArrayList<>();
        TemperatureMeasurementPoint temperatureMeasurementPoint = new TemperatureMeasurementPoint();
        temperatureMeasurementPoint.setMeasurementKey("123456789");
        temperatureMeasurementPoint.setLocation("locaiton");
        temperatureMeasurementPoint.setName("name");
        temperatureMeasurementPoints.add(temperatureMeasurementPoint);

        when(temperatureMeasurementPointRepository.findAll()).thenReturn(temperatureMeasurementPoints);

        List<MeasurementPoint> measurementPointList = dataService.getAllMeasurementPoints();
        assertEquals("list of measurements points",measurementPoints_expected,measurementPointList);
    }
    @Test
    void test_ingest_data_null() {
        dataService.ingestData(null);
        verify(measurementRepository,times(0)).saveAll(any());
    }

    @Test
    void test_ingest_metadata_null() {
        dataService.ingestData(new TemperatureData());
        verify(measurementRepository,times(0)).saveAll(any());
    }

    @Test
    void test_ingest_measurements_null() {
        Metadata metadata = new Metadata(MEASUREMENT_KEY);
        TemperatureData data = new TemperatureData(metadata,null);
        dataService.ingestData(data);
        verify(measurementRepository,times(0)).saveAll(any());
    }

    @Test
    void test_ingest_measurement_empty() {
        TemperatureData data = createTemperatureData();
        data.getMeasurements().clear();
        when(temperatureMeasurementPointRepository.findById(MEASUREMENT_KEY)).thenReturn(Optional.of(createTemperaturPoint()));
        dataService.ingestData(data);
        verify(measurementRepository,times(0)).saveAll(any());
    }

    @Test
    void test_ingest_measurement_not_found() {
        TemperatureData data = createTemperatureData();
        Exception exception = assertThrows(HttpClientErrorException.class, () -> {
            dataService.ingestData(data);
        });
        assertEquals("expect HttpClientErrorException exception",HttpClientErrorException.class, exception.getClass());
        assertEquals("expect http not found status", HttpStatus.NOT_FOUND, ((HttpClientErrorException)exception).getStatusCode());
    }


    @Test
    void test_ingest_measurement() {
        TemperatureData data = createTemperatureData();
        when(temperatureMeasurementPointRepository.findById(MEASUREMENT_KEY)).thenReturn(Optional.of(createTemperaturPoint()));
        dataService.ingestData(data);
        final ArgumentCaptor<List<Measurement>> listMeasurements
                = ArgumentCaptor.forClass(List.class);
        verify(measurementRepository,times(1)).saveAll(listMeasurements.capture());
        List<Measurement> measurements = listMeasurements.getValue();
        assertEquals("number of inserted measurements",1,measurements.size());
        Assertions.assertEquals(data.getMeasurements().get(0).getSkyState(),measurements.get(0).getSky());
        Assertions.assertEquals(data.getMeasurements().get(0).getTemperature(),measurements.get(0).getTemperature());
        assertNotNull(measurements.get(0).getTimestamp());
        Assertions.assertEquals(measurements.get(0).getTemperatureMeasurementPoint().getMeasurementKey(),createTemperaturPoint().getMeasurementKey());
    }

    @Test
    void test_add_measurement_point_data_null() {
        String measurementKey = dataService.addMeasurementPoint(null);
        assertNull(measurementKey);
    }

    @Test
    void test_add_measurement_point_data() {
        MeasurementPoint measurementPoint = new MeasurementPoint("name","location",null);
        when(temperatureMeasurementPointRepository.save(any(TemperatureMeasurementPoint.class))).thenAnswer(i -> i.getArguments()[0]);
        String measurementKey = dataService.addMeasurementPoint(measurementPoint);
        assertNotNull(measurementKey);
        final ArgumentCaptor<TemperatureMeasurementPoint> temperatureMeasurementPointArgumentCaptor
                = ArgumentCaptor.forClass(TemperatureMeasurementPoint.class);
        verify(temperatureMeasurementPointRepository,times(1)).save(temperatureMeasurementPointArgumentCaptor.capture());
        TemperatureMeasurementPoint temperatureMeasurementPoint = temperatureMeasurementPointArgumentCaptor.getValue();
        assertEquals("measurement point location",measurementPoint.getLocation(),temperatureMeasurementPoint.getLocation());
        assertEquals("measurement point name",measurementPoint.getName(),temperatureMeasurementPoint.getName());
    }


    @Test
    void test_read_point_data_key_null() {
        List<TemperatureMeasurement> temperatureMeasurements = dataService.readDataPoints(null,null,null);
        assertEquals("expected empty list",new ArrayList<>(),temperatureMeasurements);
    }

    @Test
    void test_read_point_data_to_null() {
        prepareDataForReadDataPoints();
        List<TemperatureMeasurement> temperatureMeasurements = dataService.readDataPoints(null,"2020-12-09 23:59:00",MEASUREMENT_KEY);
        assertEquals("number of entries",4,temperatureMeasurements.size());
    }

    @Test
    void test_read_point_data_from_null() {
        prepareDataForReadDataPoints();
        List<TemperatureMeasurement> temperatureMeasurements = dataService.readDataPoints("2020-12-09 23:59:00",null,MEASUREMENT_KEY);
        assertEquals("number of entries",2,temperatureMeasurements.size());
    }

    @Test
    void test_read_point_data_with_to_from() {
        prepareDataForReadDataPoints();
        List<TemperatureMeasurement> temperatureMeasurements = dataService.readDataPoints("2020-12-09 23:59:00","2020-12-09 23:59:00",MEASUREMENT_KEY);
        assertEquals("number of entries",1,temperatureMeasurements.size());
    }

    @Test
    void test_read_point_data_only_key() {
        prepareDataForReadDataPoints();
        List<TemperatureMeasurement> temperatureMeasurements = dataService.readDataPoints(null,null,MEASUREMENT_KEY);
        assertEquals("number of entries",5,temperatureMeasurements.size());
    }

    private void prepareDataForReadDataPoints()
    {
        List<Measurement> measurementsOnlyKey = new ArrayList<>();
        List<Measurement> measurementsKeyTo = new ArrayList<>();
        List<Measurement> measurementsKeyFrom = new ArrayList<>();
        List<Measurement> measurementsKeyToFrom = new ArrayList<>();

        for(int i=0; i<5; i++)
            measurementsOnlyKey.add(createMeasurement());
        for(int i=0; i<4; i++)
            measurementsKeyTo.add(createMeasurement());
        for(int i=0; i<2; i++)
            measurementsKeyFrom.add(createMeasurement());
        for(int i=0; i<1; i++)
            measurementsKeyToFrom.add(createMeasurement());

        when(measurementRepository.findAllByTemperatureMeasurementPoint_MeasurementKey(MEASUREMENT_KEY)).thenReturn(measurementsOnlyKey);
        when(measurementRepository.findAllByTemperatureMeasurementPoint_MeasurementKeyAndTimestampBefore(anyString(),any(Timestamp.class)))
                .thenReturn(measurementsKeyTo);
        when(measurementRepository.findAllByTemperatureMeasurementPoint_MeasurementKeyAndTimestampAfter(anyString(),any(Timestamp.class)))
                .thenReturn(measurementsKeyFrom);
        when(measurementRepository.findAllByTemperatureMeasurementPoint_MeasurementKeyAndTimestampBetween(anyString(),any(Timestamp.class),any(Timestamp.class)))
                .thenReturn(measurementsKeyToFrom);
    }

    private TemperatureData createTemperatureData()
    {
        Metadata metadata = new Metadata(MEASUREMENT_KEY);
        List<TemperatureMeasurement> measurements = new ArrayList<>();
        TemperatureMeasurement measurement = new TemperatureMeasurement(12.0,SkyState.CLEAR,null);
        measurements.add(measurement);
        return new TemperatureData(metadata,measurements);
    }

    private TemperatureMeasurementPoint createTemperaturPoint()
    {
        return new TemperatureMeasurementPoint(MEASUREMENT_KEY,"name","location");
    }

    private Measurement createMeasurement ()
    {
        Measurement measurement = new Measurement();
        measurement.setTemperature(12.0);
        measurement.setSky(SkyState.CLEAR);
        measurement.setTimestamp(new Timestamp(System.currentTimeMillis()));

        return measurement;
    }

}
