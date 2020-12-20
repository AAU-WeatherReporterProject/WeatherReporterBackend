# WeatherReporterBackend
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=AAU-WeatherReporterProject_WeatherReporterBackend&metric=bugs)](https://sonarcloud.io/dashboard?id=Kirdock_Moodle2.0)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=AAU-WeatherReporterProject_WeatherReporterBackend&metric=code_smells)](https://sonarcloud.io/dashboard?id=Kirdock_Moodle2.0)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=AAU-WeatherReporterProject_WeatherReporterBackend&metric=alert_status)](https://sonarcloud.io/dashboard?id=Kirdock_Moodle2.0)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=AAU-WeatherReporterProject_WeatherReporterBackend&metric=sqale_index)](https://sonarcloud.io/dashboard?id=Kirdock_Moodle2.0)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=AAU-WeatherReporterProject_WeatherReporterBackend&metric=vulnerabilities)](https://sonarcloud.io/dashboard?id=Kirdock_Moodle2.0)


##APIs

###Data Ingest:
post "/api/v1/ingest" 

Example for Json Body of post request
````
{
    "metadata": {
        "key": "123456789"
    },
    "measurements": [
        {         
            "temperature": "12.11",
            "skyState": "2"
        }
    ]
}
````
 Sunny: 0 , Windy: 1 , Cloudy: 2 , Rain: 3 , Clear: 4

###Add MeasurementPoint 
post "/api/v1/measurementPoint" 
````
{
	"name":"irgendwo",
	"location":"wos geht die dos on"
}
````
As result you get the measurement key 

Example : 7eb039af-9042-4972-8575-ca1b2adf753e

###Get all measurement points
get "/api/v1/measurementPoints"

Result
````
[
    {
        "name": "TestLocation",
        "location": "irgendwo",
        "measurementKey": "123456789"
    }
]
````

###Get data points
get "/api/v1/dataPoints"

Here the 3 parameters from, to, and key can be given

Example:
````
http://localhost:8098/api/v1/dataPoints?from=2020-08-08 23:59:00&to=2020-09-10 23:59:00&key=123456789
````
###Access H2 Console 
- http://localhost:8098/api/h2-console
