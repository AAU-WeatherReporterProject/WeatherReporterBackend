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
class ReadMeasurementIntegrationTests {

    @Autowired
    protected MockMvc mvc;
    @Autowired
    private TemperatureMeasurementPointRepository temperatureMeasurementPointRepository;
    @Autowired
    private MeasurementRepository measurementRepository;

    private final String MEASUREMENT_POINT_1 = "TestLocation";
    private final String MEASUREMENT_POINT_2 = "12345678";

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
    void getMeasurements_no_key() throws Exception {
        this.mvc.perform(get("/v1/dataPoints"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Required String parameter 'key' is not present"));
    }

    @Test
    void getMeasurements() throws Exception {
        this.mvc.perform(get("/v1/dataPoints")
                .param("key", MEASUREMENT_POINT_1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(5));
    }

    @Test
    void getMeasurements_with_from() throws Exception {
        this.mvc.perform(get("/v1/dataPoints")
                .param("key", MEASUREMENT_POINT_1)
                .param("from", "2020-10-08 23:59:00"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(3));
    }

    @Test
    void getMeasurements_with_to() throws Exception {
        this.mvc.perform(get("/v1/dataPoints")
                .param("key", MEASUREMENT_POINT_1)
                .param("to", "2020-10-08 23:59:00"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2));
    }

    @Test
    void getMeasurements_with_from_and_to() throws Exception {
        this.mvc.perform(get("/v1/dataPoints")
                .param("key", MEASUREMENT_POINT_1)
                .param("from", "2020-09-08 23:59:00")
                .param("to", "2020-12-08 23:59:00"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(3));
    }

    @Test
    void getMeasurements_from_wrong_format() throws Exception {
        this.mvc.perform(get("/v1/dataPoints")
                .param("key", MEASUREMENT_POINT_1)
                .param("from", "2020-09-08 :00"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Timestamp format must be yyyy-mm-dd hh:mm:ss[.fffffffff]"));
    }
    @Test
    void getMeasurements_to_wrong_format() throws Exception {
        this.mvc.perform(get("/v1/dataPoints")
                .param("key", MEASUREMENT_POINT_1)
                .param("to", "2020-09-08 :00"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Timestamp format must be yyyy-mm-dd hh:mm:ss[.fffffffff]"));
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
