package at.aau.projects.weatherreporter.aws.model;

/**
 * For setting the region of AWSClients
 * @see https://docs.aws.amazon.com/AWSEC2/latest/UserGuide/using-regions-availability-zones.html
 */
public enum Region {
    NORTH_VIRGINIA("us-east-1"),             // N. Virginia
    NORTH_CALIFORNIA("us-west-1"),             // N. California
    CAPE_TOWN("af-south-1"),           // Cape Town
    HONG_KONG("ap-east-1"),             // Hong Kong
    MUMBAI("ap-south-1"),           // Mumbai
    SYDNEY("ap-southeast-2"),   // Sydney
    CANADA("ca-central-1"),       // Canada
    FRANKFURT("eu-central-1"),       // Frankfurt
    LONDON("eu-west-2"),             // London
    BAHRAIN("me-south-1"),           // Bahrain
    SAO_PAULO("sa-east-1");             // Sao Paulo

    // Contains Region of created enum
    String region;

    Region(String region){
        this.region = region;
    }

    public String getRegion(){
        return this.region;
    }
}
