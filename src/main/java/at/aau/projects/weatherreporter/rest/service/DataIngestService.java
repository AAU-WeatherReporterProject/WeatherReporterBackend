package at.aau.projects.weatherreporter.rest.service;

import at.aau.projects.weatherreporter.rest.model.TemperatureData;

public interface DataIngestService {

    void ingestData(TemperatureData data);
}
