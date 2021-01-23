package at.aau.projects.weatherreporter.unittests;

import at.aau.projects.weatherreporter.rest.entity.TemperatureMeasurementPoint;
import at.aau.projects.weatherreporter.rest.model.MeasurementPoint;
import at.aau.projects.weatherreporter.rest.repository.MeasurementRepository;
import at.aau.projects.weatherreporter.rest.repository.TemperatureMeasurementPointRepository;
import at.aau.projects.weatherreporter.rest.service.DataService;
import at.aau.projects.weatherreporter.rest.service.DataServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.AssertionErrors.assertEquals;

@ExtendWith(MockitoExtension.class)
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
    void test_add_measurement_point_null() {
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () -> {
            dataService.addMeasurementPoint(null);
        });
        assertEquals("expect http bad request status", HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void test_add_measurement_point_location_null() {
        MeasurementPoint measurementPoint = new MeasurementPoint(null);
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () -> {
            dataService.addMeasurementPoint(measurementPoint);
        });
        assertEquals("expect http bad request status", HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void test_add_measurement_point_data() {
        MeasurementPoint measurementPoint = new MeasurementPoint(LOCATION);
        dataService.addMeasurementPoint(measurementPoint);
        final ArgumentCaptor<TemperatureMeasurementPoint> temperatureMeasurementPointArgumentCaptor
                = ArgumentCaptor.forClass(TemperatureMeasurementPoint.class);
        verify(temperatureMeasurementPointRepository,times(1)).saveAndFlush(temperatureMeasurementPointArgumentCaptor.capture());
        TemperatureMeasurementPoint temperatureMeasurementPoint = temperatureMeasurementPointArgumentCaptor.getValue();
        assertEquals("measurement point location",measurementPoint.getLocation(),temperatureMeasurementPoint.getLocation());
    }
}
