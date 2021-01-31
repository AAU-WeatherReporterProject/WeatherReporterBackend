package at.aau.projects.weatherreporter.rest.aws;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;

import javax.annotation.PostConstruct;
import java.util.Scanner;

@Getter
@Profile({"DynamoDB", "S3Bucket"})
public class AWSCredentialPair {
    @Value("${amazon.aws.accesskey}")
    private String accessKey;

    @Value("${amazon.aws.secretkey}")
    private String secretKey;

    public AWSCredentialPair(){}

    @PostConstruct
    public void checkAccessKey(){
        if(accessKey.equals("access")){
            System.out.println("Enter valid access-key");
            Scanner sc = new Scanner(System.in);
            accessKey = sc.nextLine();
        }
    }

    @PostConstruct
    public void checkSecretKey(){
        if(secretKey.equals("secret")){
            System.out.println("Enter valid secret-key");
            Scanner sc = new Scanner(System.in);
            secretKey = sc.nextLine();
        }
    }

    public AWSCredentials getAWSCredentials() {
        return new BasicAWSCredentials(accessKey, secretKey);
    }
}
