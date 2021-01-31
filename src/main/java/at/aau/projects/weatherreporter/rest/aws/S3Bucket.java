package at.aau.projects.weatherreporter.rest.aws;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;

import java.io.File;


@Getter
@Setter
@Profile("S3Bucket")
/**
 * @Deprecated - Simple Setup for creating/deleting buckets and uploading/downloading files
 * TODO -- Refactor to fit s3 implementation
 */
public class S3Bucket {
    /**
     * Bucketname for direct calls
     */
    @Value("${default.bucketname:WeatherReporterAWS})")
    private String bucketName;

    @Value("${default.bucketfilename:Weatherdata.json}")
    private String bucketFilename;

    public S3Bucket(String bucketName, String bucketFilename) {
        this.bucketName = bucketName;
        this.bucketFilename = bucketFilename;
    }

    /**
     */
    public void createBucket() {
        try {
            //AWSService.amazonS3.createBucket(bucketName);
            System.out.println("Successfully created " + bucketName );
        }catch(AmazonS3Exception e) {
            e.printStackTrace();
        }
    }

    /**
     */
    public void deleteBucket() {
        try {
            //AWSService.amazonS3.deleteBucket(bucketName);
            System.out.println("Successfully deleted " + bucketName);
        }catch(AmazonS3Exception e) {
            e.printStackTrace();
        }
    }

    public void createFile(){
        try {
            //AWSService.amazonS3.deleteObject(bucketName, bucketFilename);
            System.out.println("Deleted File " + bucketName + "/" + bucketFilename);
        }catch(AmazonServiceException e) {
            e.printStackTrace();
        }

    }

    public void deleteFile(){
        try {
            //AWSService.amazonS3.deleteObject(bucketName, bucketFilename);
            System.out.println("Deleted File " + bucketName + "/" + bucketFilename);
        }catch(AmazonServiceException e) {
            e.printStackTrace();
        }
    }

    public File downloadFile(){
        File downloadFile = new File("TmpDownload.json");
        //ObjectMetadata object = AWSService.amazonS3.getObject(new GetObjectRequest(bucketName, bucketFilename), downloadFile);
        if(downloadFile.exists() && downloadFile.canRead()){
            return downloadFile;
        }else{
            return null;
        }

    }

    public void uploadFile(String bucketFilePath, String localFilePath){
        PutObjectRequest request = new PutObjectRequest(bucketName, bucketFilePath, new File(localFilePath));
        // TODO Describe metadata on file?
        //ObjectMetadata metadata = new ObjectMetadata();
        //metadata.setContentType("application/json");
        //metadata.addUserMetadata("title", "myfiletitle");
        //request.setMetadata(metadata);

        //AWSService.amazonS3.putObject(request);
    }

    /**
     * File-URL
     * @implNote https://<bucket-name>.s3.amazonaws.com/<filename>
     */
    public String getFileURL(){
        return "https://"+bucketName+".s3.amazonaws.com/"+bucketFilename;
    }

    /**
     * Bucket-URL
     * @implNote https://<bucket-name>.s3.amazonaws.com/<filename>
     */
    public String getBucketURL(){
        return "https://"+bucketName+".s3.amazonaws.com/";
    }

}
