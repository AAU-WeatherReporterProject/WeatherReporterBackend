package at.aau.projects.weatherreporter.integrationtests;

import at.aau.projects.weatherreporter.rest.entity.Measurement;
import at.aau.projects.weatherreporter.rest.entity.TemperatureMeasurementPoint;
import at.aau.projects.weatherreporter.rest.model.MeasurementPoint;
import at.aau.projects.weatherreporter.rest.model.SkyState;
import at.aau.projects.weatherreporter.rest.repository.MeasurementRepository;
import at.aau.projects.weatherreporter.rest.repository.TemperatureMeasurementPointRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.util.AssertionErrors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class MeasurementPointIntegrationTests {

    @Autowired
    protected MockMvc mvc;
    @Autowired
    private TemperatureMeasurementPointRepository temperatureMeasurementPointRepository;
    @Autowired
    private MeasurementRepository measurementRepository;

    private final String MEASUREMENT_POINT_1 = "TestLocation";
    private final String MEASUREMENT_POINT_2 = "12345678";
    private final String MEASUREMENT_POINT_TOO_LONG = "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789" +
            "012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789";
    private final String MEASUREMENT_POINT_LENGTH_180 = "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789" +
            "01234567890123456789012345678901234567890123456789012345678901234567890123456789";

    @BeforeEach
    public void setUp() {
        addTestValues();
    }

    @AfterEach
    public void tearDown() {
        measurementRepository.deleteAll();
        measurementRepository.flush();
        temperatureMeasurementPointRepository.deleteAll();
        temperatureMeasurementPointRepository.flush();
    }

    @Test
    void getMeasurementPoints() throws Exception {
        this.mvc.perform(get("/v1/measurementPoints"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(2));
    }

    @Test
    void addMeasurementPoints() throws Exception {
        this.mvc.perform(post("/v1/measurementPoint").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapToJson(new MeasurementPoint("test2323"))))
                .andExpect(status().isOk());

        List<TemperatureMeasurementPoint> points = temperatureMeasurementPointRepository.findAll();
        assertNotNull("temperature points", points);
        assertEquals("number of temperature points", 3, points.size());
        assertTrue("contains temperature point 'test2323' ", points.stream().anyMatch(point -> "test2323".equals(point.getLocation())));
    }


    @Test
    void addMeasurementPoints_locationLength_180() throws Exception {
        this.mvc.perform(post("/v1/measurementPoint").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapToJson(new MeasurementPoint(MEASUREMENT_POINT_LENGTH_180))))
                .andExpect(status().isOk());

        List<TemperatureMeasurementPoint> points = temperatureMeasurementPointRepository.findAll();
        assertNotNull("temperature points", points);
        assertEquals("number of temperature points", 3, points.size());
        assertTrue("contains temperature point ", points.stream().anyMatch(point -> MEASUREMENT_POINT_LENGTH_180.equals(point.getLocation())));
    }

    @Test
    void addMeasurementPoints_no_content() throws Exception {
        this.mvc.perform(post("/v1/measurementPoint").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        List<TemperatureMeasurementPoint> points = temperatureMeasurementPointRepository.findAll();
        assertNotNull("temperature points", points);
        assertEquals("number of temperature points", 2, points.size());
    }

    @Test
    void addMeasurementPoints_empty_json_content() throws Exception {
        this.mvc.perform(post("/v1/measurementPoint").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasItem("no location given")));

        List<TemperatureMeasurementPoint> points = temperatureMeasurementPointRepository.findAll();
        assertNotNull("temperature points", points);
        assertEquals("number of temperature points", 2, points.size());
    }

    @Test
    void addMeasurementPoints_location_null() throws Exception {
        this.mvc.perform(post("/v1/measurementPoint").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapToJson(new MeasurementPoint(null))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasItem("no location given")));
    }


    @Test
    void addMeasurementPoints_location_empty() throws Exception {
        this.mvc.perform(post("/v1/measurementPoint").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapToJson(new MeasurementPoint(""))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasItem("no location given")));
    }

    @Test
    void addMeasurementPoints_location_too_long() throws Exception {
        this.mvc.perform(post("/v1/measurementPoint").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapToJson(new MeasurementPoint(MEASUREMENT_POINT_TOO_LONG))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasItem("location must be smaller or equal than 180 character")));
    }

    @Test
    void addMeasurementPoints_point_already_exists() throws Exception {
        this.mvc.perform(post("/v1/measurementPoint").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapToJson(new MeasurementPoint(MEASUREMENT_POINT_1))))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Measurement Point already exists"));

        List<TemperatureMeasurementPoint> points = temperatureMeasurementPointRepository.findAll();
        assertNotNull("temperature points", points);
        assertEquals("number of temperature points", 2, points.size());
    }

    private void addTestValues() {
        List<Measurement> measurementList = new ArrayList<>();
        TemperatureMeasurementPoint point = new TemperatureMeasurementPoint(MEASUREMENT_POINT_1);
        TemperatureMeasurementPoint point2 = new TemperatureMeasurementPoint(MEASUREMENT_POINT_2);

        temperatureMeasurementPointRepository.saveAndFlush(point);
        temperatureMeasurementPointRepository.saveAndFlush(point2);

        measurementList.add(new Measurement(point, Timestamp.valueOf("2020-12-09 23:59:00.0"), 12.0, 30, 960.20, SkyState.WINDY));
        measurementList.add(new Measurement(point, Timestamp.valueOf("2020-11-09 23:59:00.0"), 12.0, 30, 960.20, SkyState.WINDY));
        measurementList.add(new Measurement(point, Timestamp.valueOf("2020-10-09 23:59:00.0"), 12.0, 30, 960.20, SkyState.WINDY));
        measurementList.add(new Measurement(point, Timestamp.valueOf("2020-09-09 23:59:00.0"), 12.0, 30, 960.20, SkyState.WINDY));
        measurementList.add(new Measurement(point, Timestamp.valueOf("2020-08-09 23:59:00.0"), 12.0, 30, 960.20, SkyState.WINDY));

        measurementRepository.saveAll(measurementList);
        measurementRepository.flush();
    }

    protected String mapToJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }
}
