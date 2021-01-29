package at.aau.projects.weatherreporter.aws.service;


import at.aau.projects.weatherreporter.aws.model.CredentialPair;
import at.aau.projects.weatherreporter.aws.model.Region;
import at.aau.projects.weatherreporter.aws.model.S3Bucket;
import at.aau.projects.weatherreporter.rest.model.MeasurementPoint;
import at.aau.projects.weatherreporter.rest.model.TemperatureData;
import at.aau.projects.weatherreporter.rest.model.TemperatureMeasurement;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

/**
 * CLass managing AWS-S3 Calls
 */
@Getter
public class AWSS3Utils {
    /**
     * Client initiatior
     */
    public static AmazonS3 amazonS3;

    /**
     * Object to hold keys
     * @see CredentialPair
     */
    private CredentialPair awsCredPair;

    /**
     * Object holding specific bucket
     */
    private S3Bucket s3Bucket;

    /**
     * Region specifying where bucket will be created
     * @see Region
     */
    @Value("${default.region:eu-central-1})")
    private String region;



    public AWSS3Utils(){}

    public AWSS3Utils(String accessKey, String secretKey) {
        awsCredPair = new CredentialPair(accessKey, secretKey);
    }

    public AWSS3Utils(String awsUrl, String bucketName, String accessKey, String secretKey, String region) {
        awsCredPair = new CredentialPair(accessKey, secretKey);
        s3Bucket = new S3Bucket(awsUrl, bucketName);
        this.region = region;
    }

    public void launchS3Instance() {
        BasicAWSCredentials creds = new BasicAWSCredentials(awsCredPair.getAWSSecretKey(), awsCredPair.getAWSSecretKey());
        amazonS3 = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(creds)).build();
        s3Bucket.createBucket();
    }

    public void shutdownS3Instance() {
        s3Bucket.deleteBucket();
    }

    public void addTemperatureData(TemperatureData data) {
        // TODO
    }

    public List<TemperatureMeasurement> readMeasurements(String from, String to, String location) {
        // TODO
        return null;
    }

    public void addMeasurementPoints() {
        // TODO
    }

    public List<MeasurementPoint> getMeasurementPoints() {
        // TODO
        return null;
    }
}
