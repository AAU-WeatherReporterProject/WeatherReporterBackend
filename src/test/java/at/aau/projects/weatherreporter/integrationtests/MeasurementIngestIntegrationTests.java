package at.aau.projects.weatherreporter.integrationtests;

import at.aau.projects.weatherreporter.rest.entity.Measurement;
import at.aau.projects.weatherreporter.rest.entity.TemperatureMeasurementPoint;
import at.aau.projects.weatherreporter.rest.model.*;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class MeasurementIngestIntegrationTests {

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
    void ingestData_empty_Body() throws Exception {
        performDataIngest("").andExpect(status().isBadRequest());
    }

    @Test
    void ingestData_empty_json() throws Exception {
        performDataIngest("{}")
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasItem("no metadata given")))
                .andExpect(jsonPath("$.errors", hasItem("no measurements given")));
    }

    @Test
    void ingestData_no_meta_data_and_measurements() throws Exception {
        performDataIngest(mapToJson(new TemperatureData()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasItem("no metadata given")))
                .andExpect(jsonPath("$.errors", hasItem("no measurements given")));
    }

    @Test
    void ingestData_no_metadata() throws Exception {
        TemperatureMeasurement temperatureMeasurement = new TemperatureMeasurement( 10.0,20,900.0, SkyState.CLEAR,null);
        List<TemperatureMeasurement> temperatureMeasurements = new ArrayList<>();
        temperatureMeasurements.add(temperatureMeasurement);

        performDataIngest(mapToJson(new TemperatureData(null, temperatureMeasurements)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasItem("no metadata given")));
    }

    @Test
    void ingestData_no_location() throws Exception {
        TemperatureData temperatureData = createTemperatureData(null, 20, 900.0, SkyState.CLEAR, null);

        performDataIngest(mapToJson(temperatureData))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasItem("no location given")));
    }
    @Test
    void ingestData_no_temperature() throws Exception {
        TemperatureData temperatureData = createTemperatureData(null, 20, 900.0, SkyState.CLEAR, MEASUREMENT_POINT_1);

        performDataIngest(mapToJson(temperatureData))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasItem("no temperature for measurement given")));
    }

    @Test
    void ingestData_too_low_temperature() throws Exception {
        TemperatureData temperatureData = createTemperatureData(-61.0, 20, 900.0, SkyState.CLEAR, MEASUREMENT_POINT_1);

        performDataIngest(mapToJson(temperatureData))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasItem("temperature must be greater or equal -60")));
    }

    @Test
    void ingestData_too_high_temperature() throws Exception {
        TemperatureData temperatureData = createTemperatureData(101.0, 20, 900.0, SkyState.CLEAR, MEASUREMENT_POINT_1);
        performDataIngest(mapToJson(temperatureData))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasItem("temperature must be smaller or equal 100")));
    }


    @Test
    void ingestData_too_low_humidity() throws Exception {
        TemperatureData temperatureData = createTemperatureData(60.0, -10, 900.0, SkyState.CLEAR, MEASUREMENT_POINT_1);
        performDataIngest(mapToJson(temperatureData))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasItem("humidity must be greater or equal 0")));
    }

    @Test
    void ingestData_too_high_humidity() throws Exception {
        TemperatureData temperatureData = createTemperatureData(60.0, 110, 900.0, SkyState.CLEAR, MEASUREMENT_POINT_1);
        performDataIngest(mapToJson(temperatureData))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasItem("humidity must be smaller or equal 100")));
    }

    @Test
    void ingestData_too_low_pressure() throws Exception {
        TemperatureData temperatureData = createTemperatureData(60.0, 50, 500.0, SkyState.CLEAR, MEASUREMENT_POINT_1);
        performDataIngest(mapToJson(temperatureData))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasItem("pressure must be greater or equal 800")));
    }

    @Test
    void ingestData_too_high_pressure() throws Exception {
        TemperatureData temperatureData = createTemperatureData(60.0, 50, 1200.0, SkyState.CLEAR, MEASUREMENT_POINT_1);
        performDataIngest(mapToJson(temperatureData))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasItem("pressure must be smaller or equal 1100")));
    }

    @Test
    void ingestData_skystate_null() throws Exception {
        TemperatureData temperatureData = createTemperatureData(60.0, 50, 1200.0, null, MEASUREMENT_POINT_1);
        performDataIngest(mapToJson(temperatureData))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasItem("pressure must be smaller or equal 1100")));
    }

    private ResultActions performDataIngest(String content) throws Exception {
        return this.mvc.perform(post("/v1/ingest").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content));
    }

    private void addTestValues() {
        TemperatureMeasurementPoint point = new TemperatureMeasurementPoint(MEASUREMENT_POINT_1);
        temperatureMeasurementPointRepository.saveAndFlush(point);
    }

    protected String mapToJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }

    private TemperatureData createTemperatureData(Double temperature, Integer humidity,Double pressure, SkyState skyState, String location)
    {
        TemperatureMeasurement temperatureMeasurement = new TemperatureMeasurement( temperature,humidity,pressure, skyState,null);
        List<TemperatureMeasurement> temperatureMeasurements = new ArrayList<>();
        temperatureMeasurements.add(temperatureMeasurement);

        return new TemperatureData(new Metadata(location), temperatureMeasurements);
    }

}
