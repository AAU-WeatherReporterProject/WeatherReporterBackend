package at.aau.projects.weatherreporter.rest.service;

import at.aau.projects.weatherreporter.rest.aws.AWSCredentialPair;
import at.aau.projects.weatherreporter.rest.entity.Measurement;
import at.aau.projects.weatherreporter.rest.entity.TemperatureMeasurementPoint;
import at.aau.projects.weatherreporter.rest.model.MeasurementPoint;
import at.aau.projects.weatherreporter.rest.model.SkyState;
import at.aau.projects.weatherreporter.rest.model.TemperatureData;
import at.aau.projects.weatherreporter.rest.model.TemperatureMeasurement;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation for use of DynamoDB
 */
@Service("DataService")
@Profile("DynamoDB")
public class DynamoDBService implements DataService {
    @Value("${amazon.dynamodb.endpoint}")
    private String dynamoEndpoint;

    private AWSCredentialPair credentials;
    private AmazonDynamoDB ddb;

    public DynamoDBService (){
        credentials = new AWSCredentialPair();
        ddb = new AmazonDynamoDBClient(credentials.getAWSCredentials());
    }

    @PostConstruct
    public void checkDynamoDBEndpoint(){
        if (!dynamoEndpoint.isEmpty()) {
            ddb.setEndpoint(dynamoEndpoint);
        }else{
            System.out.println("Failed to setup DynamoDB-Endpoint.");
        }
    }

    // TODO -- Exceptional behaviour
    // FIXME -- Might target wrong table
    @Override
    public void ingestData(TemperatureData data) {
        DynamoDBMapper ddbm = new DynamoDBMapper(ddb);

        for(TemperatureMeasurement tm : data.getMeasurements()){
            Measurement m = new Measurement();
            m.setTemperatureMeasurementPoint(new TemperatureMeasurementPoint(data.getMetadata().getKey()));
            m.setTemperature(tm.getTemperature());
            m.setSky(tm.getSkyState());
            m.setHumidity(tm.getHumidity());
            m.setPressure(tm.getPressure());
            m.setTimestamp(new Timestamp(System.currentTimeMillis()));
            ddbm.save(m);
        }
    }

    // TODO -- Exceptional behaviour
    // TODO -- Refactor tablenames into environmental variables ?
    @Override
    public List<TemperatureMeasurement> readMeasurements(String from, String to, String location) {
        List<TemperatureMeasurement> tms = new ArrayList<>();
        DynamoDB dynamoDB = new DynamoDB(ddb);
        Table t = dynamoDB.getTable("Measurement");

        RangeKeyCondition queryCond = new RangeKeyCondition("timestamp").between(from, to);
        QuerySpec querySpec = new QuerySpec()
                .withKeyConditionExpression("location = " + location)
                .withRangeKeyCondition(queryCond);
        ItemCollection<QueryOutcome> records = t.query(querySpec);

        for(Item r : records){
            TemperatureMeasurement tm = new TemperatureMeasurement(
                    r.getDouble("temperature"),
                    r.getInt("humidity"),
                    r.getDouble("pressure"),
                    detectSkyState(r.getString("skyState")),
                    r.getString("timestamp")
            );
            tms.add(tm);
        }
        return tms;
    }

    // TODO -- Exceptional behaviour
    // TODO -- Refactor tablenames into environmental variables ?
    @Override
    public void addMeasurementPoint(MeasurementPoint measurementPoint) {
        TemperatureMeasurementPoint tmp = new TemperatureMeasurementPoint();
        tmp.setLocation(measurementPoint.getLocation());

        DynamoDB dynamoDB = new DynamoDB(ddb);
        Table t = dynamoDB.getTable("Temperature_Measurement_Point");

        Item i = new Item().withPrimaryKey("location", measurementPoint.getLocation());
        t.putItem(i);
    }

    // TODO -- Exceptional behaviour
    // TODO -- Refactor tablenames into environmental variables ?
    @Override
    public List<MeasurementPoint> getAllMeasurementPoints() {
        List<MeasurementPoint> tmps = new ArrayList<>();

        DynamoDB dynamoDB = new DynamoDB(ddb);
        Table t = dynamoDB.getTable("Temperature_Measurement_Point");

        QuerySpec querySpec = new QuerySpec().withAttributesToGet("location");
        ItemCollection<QueryOutcome> records = t.query(querySpec);

        for(Item i : records){
            MeasurementPoint mp = new MeasurementPoint(i.getString("location"));
            tmps.add(mp);
        }

        return tmps;
    }

    private SkyState detectSkyState(String value) {
        return SkyState.valueOf(value);
    }
}
