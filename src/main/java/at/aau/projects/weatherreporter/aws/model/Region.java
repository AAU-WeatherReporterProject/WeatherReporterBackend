package at.aau.projects.weatherreporter.aws.model;

/**
 * For setting the region of AWSClients
 * @see https://docs.aws.amazon.com/AWSEC2/latest/UserGuide/using-regions-availability-zones.html
 */
public enum Region {
    NORTH_VIRGINIA("us-east-1"),
    NORTH_CALIFORNIA("us-west-1"),
    CAPE_TOWN("af-south-1"),
    HONG_KONG("ap-east-1"),
    MUMBAI("ap-south-1"),
    SYDNEY("ap-southeast-2"),
    CANADA("ca-central-1"),
    FRANKFURT("eu-central-1"),
    LONDON("eu-west-2"),
    BAHRAIN("me-south-1"),
    SAO_PAULO("sa-east-1");

    String region;

    Region(String region){
        this.region = region;
    }

    public String getRegion(){
        return this.region;
    }
}
