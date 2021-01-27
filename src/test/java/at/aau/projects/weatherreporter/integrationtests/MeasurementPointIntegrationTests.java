package at.aau.projects.weatherreporter.integrationtests;

import at.aau.projects.weatherreporter.rest.entity.Measurement;
import at.aau.projects.weatherreporter.rest.entity.TemperatureMeasurementPoint;
import at.aau.projects.weatherreporter.rest.model.MeasurementPoint;
import at.aau.projects.weatherreporter.rest.model.SkyState;
import at.aau.projects.weatherreporter.rest.model.TemperatureMeasurement;
import at.aau.projects.weatherreporter.rest.repository.MeasurementRepository;
import at.aau.projects.weatherreporter.rest.repository.TemperatureMeasurementPointRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.util.AssertionErrors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class MeasurementPointIntegrationTests {

    @Autowired
    protected MockMvc mvc;
    @Autowired
    private TemperatureMeasurementPointRepository temperatureMeasurementPointRepository;
    @Autowired
    private MeasurementRepository measurementRepository;

    @BeforeEach
    public void setUp()
    {
        addTestValues();
    }

    @AfterEach
    public void tearDown()
    {
        measurementRepository.deleteAll();
        measurementRepository.flush();
        temperatureMeasurementPointRepository.deleteAll();
        temperatureMeasurementPointRepository.flush();
    }

    @Test
    public void getMeasurementPoints() throws Exception {
        this.mvc.perform(get("/v1/measurementPoints"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(2));
    }

    @Test
    public void addMeasurementPoints() throws Exception {
        this.mvc.perform(post("/v1/measurementPoint").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapToJson(new MeasurementPoint("test2323"))))
                .andExpect(status().isOk());

        List<TemperatureMeasurementPoint> points = temperatureMeasurementPointRepository.findAll();
        assertNotNull("temperature points", points);
        assertEquals("number of temperature points", 3, points.size());
        assertTrue("contains temperature point 'test2323' ", points.stream().anyMatch(point -> "test2323".equals(point.getLocation())));
    }

    private void addTestValues()
    {
        List<Measurement> measurementList = new ArrayList<>();
        TemperatureMeasurementPoint point = new TemperatureMeasurementPoint("TestLocation");
        TemperatureMeasurementPoint point2 = new TemperatureMeasurementPoint("123456789");

        temperatureMeasurementPointRepository.saveAndFlush(point);
        temperatureMeasurementPointRepository.saveAndFlush(point2);

        measurementList.add(new Measurement(point,Timestamp.valueOf("2020-12-09 23:59:00.0"),12.0, SkyState.WINDY));
        measurementList.add(new Measurement(point,Timestamp.valueOf("2020-11-09 23:59:00.0"),12.0, SkyState.WINDY));
        measurementList.add(new Measurement(point,Timestamp.valueOf("2020-10-09 23:59:00.0"),12.0, SkyState.WINDY));
        measurementList.add(new Measurement(point,Timestamp.valueOf("2020-09-09 23:59:00.0"),12.0, SkyState.WINDY));
        measurementList.add(new Measurement(point,Timestamp.valueOf("2020-08-09 23:59:00.0"),12.0, SkyState.WINDY));

        measurementRepository.saveAll(measurementList);
        measurementRepository.flush();
    }

    protected String mapToJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }
}
