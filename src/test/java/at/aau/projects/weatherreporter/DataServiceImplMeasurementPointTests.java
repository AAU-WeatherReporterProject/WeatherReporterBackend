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
class DataServiceImplMeasurementPointTests {

    @Mock
    private TemperatureMeasurementPointRepository temperatureMeasurementPointRepository;
    @Mock
    private MeasurementRepository measurementRepository;
    private DataService dataService;

    private final String LOCATION ="Klagenfurt";

    @BeforeEach
    void setup()
    {
        dataService = new DataServiceImpl(measurementRepository,temperatureMeasurementPointRepository);
    }

    @Test
    void test_get_all_measurement_points() {
        List<MeasurementPoint> measurementPoints_expected = new ArrayList<>();
        measurementPoints_expected.add(new MeasurementPoint(LOCATION));

        List<TemperatureMeasurementPoint> temperatureMeasurementPoints = new ArrayList<>();
        TemperatureMeasurementPoint temperatureMeasurementPoint = new TemperatureMeasurementPoint();
        temperatureMeasurementPoint.setLocation(LOCATION);
        temperatureMeasurementPoints.add(temperatureMeasurementPoint);

        when(temperatureMeasurementPointRepository.findAll()).thenReturn(temperatureMeasurementPoints);

        List<MeasurementPoint> measurementPointList = dataService.getAllMeasurementPoints();
        assertEquals("list of measurements points",measurementPoints_expected,measurementPointList);
    }

    @Test
    void test_add_measurement_point_data_null() {
        dataService.addMeasurementPoint(null);
        verify(temperatureMeasurementPointRepository,times(0)).saveAll(any());
    }

    @Test
    void test_add_measurement_point_data() {
        MeasurementPoint measurementPoint = new MeasurementPoint(LOCATION);
        when(temperatureMeasurementPointRepository.save(any(TemperatureMeasurementPoint.class))).thenAnswer(i -> i.getArguments()[0]);
        dataService.addMeasurementPoint(measurementPoint);
        final ArgumentCaptor<TemperatureMeasurementPoint> temperatureMeasurementPointArgumentCaptor
                = ArgumentCaptor.forClass(TemperatureMeasurementPoint.class);
        verify(temperatureMeasurementPointRepository,times(1)).save(temperatureMeasurementPointArgumentCaptor.capture());
        TemperatureMeasurementPoint temperatureMeasurementPoint = temperatureMeasurementPointArgumentCaptor.getValue();
        assertEquals("measurement point location",measurementPoint.getLocation(),temperatureMeasurementPoint.getLocation());
    }
}
