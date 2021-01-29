package at.aau.projects.weatherreporter.aws;

import at.aau.projects.weatherreporter.rest.model.MeasurementPoint;
import at.aau.projects.weatherreporter.rest.model.TemperatureData;
import at.aau.projects.weatherreporter.rest.model.TemperatureMeasurement;

import at.aau.projects.weatherreporter.rest.service.DataService;
import at.aau.projects.weatherreporter.aws.service.AWSS3Utils;


import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;

/**
 * AWS S3 Wrapper
 * TODO - Setup Rest-PROXY: https://docs.aws.amazon.com/apigateway/latest/developerguide/integrating-api-with-aws-services-s3.html
 */
public class AWSService implements DataService {

    /**
     * Manages Calls to AWS-S3
     */
    private AWSS3Utils utils;

    /**
     * Instantiation using defaults
     * @see AWSS3Utils
     */
    public AWSService(){
        utils = new AWSS3Utils();
    }

    /**
     * Instantiation overwriting default credentials
     * @see AWSS3Utils
     * @param accessKey
     * @param secretKey
     */
    public AWSService(String accessKey, String secretKey) {
        utils = new AWSS3Utils(accessKey, secretKey);
    }

    /**
     * Instantiation overwriting default values
     * @see AWSS3Utils
     * @param awsUrl
     * @param bucketName
     * @param accessKey
     * @param secretKey
     * @param region
     */
    public AWSService(String awsUrl, String bucketName, String accessKey, String secretKey, String region) {
        utils = new AWSS3Utils(awsUrl, bucketName, accessKey, secretKey, region);
    }

    @Override
    public void ingestData(TemperatureData data) {
        utils.addTemperatureData(data);
    }

    @Override
    public List<TemperatureMeasurement> readMeasurements(String from, String to, String location) {
        return utils.readMeasurements(from, to, location);
    }

    @Override
    public void addMeasurementPoint(MeasurementPoint measurementPoint) {
        utils.addMeasurementPoints();
    }

    @Override
    public List<MeasurementPoint> getAllMeasurementPoints() {
        return utils.getMeasurementPoints();
    }


    @PostConstruct
    public void initializeAWS(){
        utils.launchS3Instance();
        System.out.println("AWS Services started.");
    }

    @PreDestroy
    public void shutdownAWS(){
        utils.shutdownS3Instance();
        System.out.println("AWS Services shutdown.");
    }








}
