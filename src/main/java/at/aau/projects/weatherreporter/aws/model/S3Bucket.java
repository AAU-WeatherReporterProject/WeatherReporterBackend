package at.aau.projects.weatherreporter.aws.model;

import at.aau.projects.weatherreporter.aws.service.AWSS3Utils;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;


@Getter
public class S3Bucket {
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

    public S3Bucket(String awsUrl, String bucketName) {
        this.awsUrl = awsUrl;
        this.bucketName = bucketName;
    }

    /**
     */
    public void createBucket() {
        try {
            AWSS3Utils.amazonS3.createBucket(bucketName);
            System.out.println("Successfully created " + bucketName );
        }catch(AmazonS3Exception e) {
            e.printStackTrace();
        }
    }

    /**
     */
    public void deleteBucket() {
        try {
            AWSS3Utils.amazonS3.deleteBucket(bucketName);
            System.out.println("Successfully deleted " + bucketName);
        }catch(AmazonS3Exception e) {
            e.printStackTrace();
        }
    }
}
