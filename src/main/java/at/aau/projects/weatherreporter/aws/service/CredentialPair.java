package at.aau.projects.weatherreporter.aws.service;

import com.amazonaws.auth.AWSCredentials;
import org.springframework.beans.factory.annotation.Value;

/**
 * Object to hold AWS-Keys.
 * TODO -- read keys from downloadable file
 */
public class CredentialPair implements AWSCredentials {
    @Value("${default.key:})")
    private String accessKey;

    @Value("${default.key:})")
    private String secretKey;

    public CredentialPair(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    @Override
    public String getAWSAccessKeyId() {
        return accessKey;
    }

    @Override
    public String getAWSSecretKey() {
        return secretKey;
    }
}
