package at.aau.projects.weatherreporter.aws;

import at.aau.projects.weatherreporter.aws.model.S3Bucket;
import at.aau.projects.weatherreporter.aws.service.S3Filemanager;
import at.aau.projects.weatherreporter.rest.model.MeasurementPoint;
import at.aau.projects.weatherreporter.rest.model.TemperatureData;
import at.aau.projects.weatherreporter.rest.model.TemperatureMeasurement;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;

/**
 * AWS S3 Wrapper
 * TODO - Setup Rest-PROXY: https://docs.aws.amazon.com/apigateway/latest/developerguide/integrating-api-with-aws-services-s3.html
 * FIXME - If Rest-proxy implementation too different, @depracate and set to AWSS3Utils as main bean
 */
public class AWSService{

    /**
     * Client initiatior
     */
    public static AmazonS3 amazonS3;
    /**
     * Object to hold keys
     */
    private BasicAWSCredentials credentials;

    /**
     * Object holding specific bucket
     */
    private S3Bucket s3Bucket;

    private S3Filemanager s3Filemanager = new S3Filemanager();

    /**
     * Region specifying where bucket will be created
     */
    @Value("${default.region:eu-central-1})")
    private String region;


    public AWSService(String accessKey, String secretKey) {
        credentials = new BasicAWSCredentials(accessKey, secretKey);
    }

    public AWSService(String accessKey, String secretKey, String region, String bucketName, String fileName) {
        credentials = new BasicAWSCredentials(accessKey, secretKey);
        s3Bucket = new S3Bucket(bucketName,fileName);
        this.region = region;
    }

    @PostConstruct
    public void initializeAWS(){
        amazonS3 = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials)).build();
        s3Bucket.createBucket();
        System.out.println("AWS Services started.");
    }


    @PreDestroy
    public void shutdownAWS(){
        s3Bucket.deleteBucket();
        System.out.println("AWS Services shutdown.");
    }



    // FIXME Note sure yet how to use this

    @PostMapping(value = "/ingest")
    public void ingest(@RequestBody @Nonnull TemperatureData data) {
        s3Filemanager.ingestData(data);
    }

    @PostMapping(value = "/measurementPoint")
    public ResponseEntity<String> addMeasurementPoint(@RequestBody @Nonnull MeasurementPoint measurementPoint) {
        s3Filemanager.addMeasurementPoint(measurementPoint);
        return ResponseEntity.ok("Measurement Point was successfully created!");
    }

    @GetMapping(value = "/measurementPoints")
    public List<MeasurementPoint> getMeasurementPoints() {
        return s3Filemanager.getAllMeasurementPoints();
    }

    @GetMapping(value = "/dataPoints")
    public List<TemperatureMeasurement> getDataPoints(
            @Param("from") String from, @Param("to") String to, @Param("key") String key) {
        return s3Filemanager.readMeasurements(from, to, key);
    }

}
