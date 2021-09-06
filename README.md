[![ghalib31](https://circleci.com/gh/ghalib31/temperature-sensor.svg?style=shield)](https://app.circleci.com/pipelines/github/ghalib31/temperature-sensor)

# Qardio Assignment

## Pre-requisite
Docker should be installed  
Run below to start container for influxdb
```bash
docker-compose up
```

*Note: Above command starts services on port 8086.  
If you wish to use any other port, please change it in influxdb.properties as well.*

<br>

## Building and running from code
Go to the root folder of the code i.e. temperature-sensor (in terminal/command prompt)and run (use mvnw.cmd for windows)
```bash
sh ./mvnw clean install
sh ./mvnw spring-boot:run
```

<br>

## Running from jar
Go to the location(project-root/target) of the jar (in terminal/command prompt) and run
```bash
java -jar temperature-sensor-0.0.1-SNAPSHOT.jar
```

<br>

## Test
Save data: */temperature/sendData*
```bash
curl -d '[{"time":"1629353716232", "temperature":6.010504028396402}]' -H "Content-Type: application/json" -X POST http://localhost:8080/temperature/sendData
```

Get data: */temperature/generateAggregatedData*
```bash
curl 'http://localhost:8080/temperature/generateAggregatedData?frequency=daily&startTime=0&endTime=1630829874000'
```
Please change endTime as epoch time in millisecond corresponding to your test data.

<br>

## Design decisions
Assignment says to fetch aggregated data very fast. Did some search to find out which database will suit best.
Influxdb is quite preferred to store time sequence based data: https://iot4beginners.com/top-5-databases-to-store-iot-data/.  
Please refer [Influxdb](https://www.influxdata.com/) to know more.  
Used version 8 of java as I am currently working in same version.  
SpringBoot is used as it makes it easy to create spring based applications without much fuss.
Version 2.5.4 is used as it is the latest version as of August 2021.  
Used project Lombok to avoid writing boilerplate code like getters, setters, constructors etc.

#### Story 1 : Create an endpoint to save temperature data
Created endpoint */temperature/sendData* to receive temperature sensor data and store in a database.  
Flow goes like *Client -> Controller -> Service -> Database*

#### Story 2 : Create an endpoint to retrieve the aggregated temperature data (hourly, daily).
Created endpoint */temperature/generateAggregatedData* to get aggregated data from the database.  
Flow goes like *Client -> Controller -> Service -> Database*. Database returns aggregated data back to the client.             