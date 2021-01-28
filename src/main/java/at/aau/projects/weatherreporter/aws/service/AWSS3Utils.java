package at.aau.projects.weatherreporter.aws.service;


import at.aau.projects.weatherreporter.aws.model.Region;
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
    private AmazonS3 amazonS3;

    /**
     * Bucket-URL
     * @implNote https://<bucket-name>.s3.amazonaws.com/<filename>
     */
    @Value("${default.url:})")
    private String awsUrl;

    /**
     * Bucketname for direct calls
     */
    @Value("${default.name:})")
    private String bucketName;

    /**
     * Object to hold keys
     * @see CredentialPair
     */
    private CredentialPair awsCredPair;

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
        this.awsUrl = awsUrl;
        this.bucketName = bucketName;
        this.region = region;
    }

    public void launchS3Instance() {
        BasicAWSCredentials creds = new BasicAWSCredentials(awsCredPair.getAWSSecretKey(), awsCredPair.getAWSSecretKey());
        amazonS3 = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(creds)).build();

        createBucket();
    }

    public void shutdownS3Instance() {
        deleteBucket();
    }

    public void addTemperatureData(TemperatureData data) {

    }

    public List<TemperatureMeasurement> readMeasurements(String from, String to, String location) {


        return null;
    }

    public void addMeasurementPoints() {

    }

    public List<MeasurementPoint> getMeasurementPoints() {

        return null;
    }

    /**
     */
    private void createBucket() {
        try {
            amazonS3.createBucket(bucketName);
            System.out.println("Successfully created " + bucketName );
        }catch(AmazonS3Exception e) {
            e.printStackTrace();
        }
    }

    /**
     */
    private void deleteBucket() {
        try {
            amazonS3.deleteBucket(bucketName);
            System.out.println("Successfully deleted " + bucketName);
        }catch(AmazonS3Exception e) {
            e.printStackTrace();
        }
    }
}
