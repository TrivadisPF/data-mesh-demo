## Initialize Platform

```bash
docker-compose up -d
```

## Prepare Data

### Minio (S3)

```bash
docker exec -ti awscli s3cmd mb s3://flight-bucket

docker exec -ti awscli s3cmd put /data-transfer/flight-data/airports.csv s3://flight-bucket/raw/airports/airports.csv

docker exec -ti awscli s3cmd put /data-transfer/flight-data/plane-data.csv s3://flight-bucket/raw/planes/plane-data.csv

docker exec -ti awscli s3cmd put /data-transfer/flight-data/carriers.json s3://flight-bucket/raw/carriers/carriers.json

docker exec -ti awscli s3cmd put /data-transfer/flight-data/flights-small/flights_2008_4_1.csv s3://flight-bucket/raw/flights/ &&
   docker exec -ti awscli s3cmd put /data-transfer/flight-data/flights-small/flights_2008_4_2.csv s3://flight-bucket/raw/flights/ &&
   docker exec -ti awscli s3cmd put /data-transfer/flight-data/flights-small/flights_2008_5_1.csv s3://flight-bucket/raw/flights/ &&
   docker exec -ti awscli s3cmd put /data-transfer/flight-data/flights-small/flights_2008_5_2.csv s3://flight-bucket/raw/flights/ &&
   docker exec -ti awscli s3cmd put /data-transfer/flight-data/flights-small/flights_2008_5_3.csv s3://flight-bucket/raw/flights/
```

<http://dataplatform:9000>

`V42FCGRVMK24JJ8DHUYG` and `bKhWxVF3kQoLY9kFmt91l+tDrEoZjqnWXzY9Eza`


### PostgreSQL

```bash
docker exec -ti postgresql psql -d postgres -U postgres
```

```sql
CREATE SCHEMA mdm;

DROP TABLE IF EXISTS mdm.airport_t;

CREATE TABLE mdm.airport_t
(
  iata character varying(50) NOT NULL,
  airport character varying(50),
  city character varying(50),
  state character varying(50),
  country character varying(50),
  lat float,
  long float,
  CONSTRAINT airport_pk PRIMARY KEY (iata)
);


COPY mdm.airport_t(iata,airport,city,state,country,lat,long) 
FROM '/data-transfer/flight-data/airports.csv' DELIMITER ',' CSV HEADER;
```


```
create table postgresql.operations.flight_t 
AS SELECT 
    year ,
    month ,
    day_of_month ,
    day_of_week ,
    departure_time AS dep_time ,
    scheduled_departure_time AS sched_dep_time ,
    arrival_time AS arr_time ,
    scheduled_arrival_time AS sched_arr_time ,
    unique_carrier  ,
    flight_number  ,
    tail_number  ,
    actual_elapsed_time AS act_elapsed_time ,
    estimated_elapsed_time AS est_elapsed_time ,
    airtime ,
    arrival_delay AS arr_delay ,
    departure_delay AS dep_delay ,
    origin_airport_iata AS orig_airport  ,
    destination_airport_iata AS dest_airport  ,
    distance AS dist ,
    taxi_in ,
    taxi_out ,
    cancelled ,
    cancellation_code AS cancellation_cd  ,
    diverted  ,
    carrier_delay ,
    weather_delay ,
    nas_delay ,
    security_delay ,
    late_aircraft_delay  
FROM int_flight_t;
```

## Materialized

### Airport Domain

#### Raw Zone

```sql
%sql
CREATE TABLE raw_airport_t
USING CSV
OPTIONS (sep=",", inferSchema="true", header="true")
LOCATION "s3a://flight-bucket/raw/airports"
```

#### Integration Zone


```scala
val airportIntDf = spark.sql("""
		SELECT a.iata
		,      a.airport
		,      a.city
		,      a.state
		,      a.country
		,      a.lat         AS latitude
		,      a.long        AS longitude
		FROM raw_airport_t  a;
""")
```

```scala
airportIntDf.write.parquet("s3a://flight-bucket/integration/airports")

airportIntDf.createOrReplaceTempView("int_airport_t")
```

#### Data Product Zone

```scala
val airportDpDf = spark.sql("""
    SELECT a.iata
    ,      a.airport
    ,      a.city
    ,      a.state
    ,      a.country
    ,      a.latitude
    ,      a.longitude
    FROM int_airport_t  a;
""")
```

```scala
airportDpDf.write.parquet("s3a://flight-bucket/dp/v1/airports")

airportDpDf.createOrReplaceTempView("dp_airport_v1_t")
```

### Carriers Domain

#### Raw Zone

```sql
DROP TABLE raw_carriers_t;

CREATE TABLE raw_carriers_t
USING JSON
LOCATION "s3a://flight-bucket/raw/carriers"
```

### Flights Domain

#### Raw Zone

```sql
CREATE TABLE raw_flight_t
USING CSV
OPTIONS (sep=",", inferSchema="true", header="false")
LOCATION "s3a://flight-bucket/raw/flights"
```

#### Integration Zone

```scala
val fligthIntDf = spark.sql("""
		SELECT INT (_c0)    AS year
		,   INT(_c1)        AS month
		,   INT(_c2)        AS day_of_month
		,   INT(_c3)        AS day_of_week
		,   INT(_c4)        AS departure_time
		,   INT(_c5)        AS scheduled_departure_time
		,   INT(_c6)        AS arrival_time
		,   INT(_c7)        AS scheduled_arrival_time
		,   STRING(_c8)     AS unique_carrier
		,   STRING(_c9)     AS flight_number
		,   STRING(_c10)    AS tail_number
		,   INT(_c11)       AS actual_elapsed_time
		,   INT(_c12)       AS estimated_elapsed_time
		,   INT(_c13)       AS airtime
		,   INT(_c14)       AS arrival_delay
		,   INT(_c15)       AS departure_delay
		,   STRING(_c16)    AS origin_airport_iata
		,   STRING(_c17)    AS destination_airport_iata
		,   INT(_c18)       AS distance
		,   INT(_c19)       AS taxi_in
		,   INT(_c20)       AS taxi_out
		,   INT(_c21)       AS cancelled
		,   STRING(_c22)    AS cancellation_code
		,   CASE 
		         WHEN _c22 = 'A' THEN 'carrier'
		         WHEN _c22 = 'B' THEN 'weather'
		         WHEN _c22 = 'C' THEN 'NAS'
		         WHEN _c22 = 'D' THEN 'security'
		    END             AS cancellation_reason
		,   STRING(_c23)    AS diverted
		,   INT(_c24)       AS carrier_delay
		,   INT(_c25)       AS weather_delay
		,   INT(_c26)       AS nas_delay
		,   INT(_c27)       AS security_delay
		,   INT(_c28)       AS late_aircraft_delay
		FROM raw_flight_t
""")
```

```scala
fligthIntDf.write.parquet("s3a://flight-bucket/integration/flights")

fligthIntDf.createOrReplaceTempView("int_flight_t")
```

#### Refined Zone

##### Flight Enriched

```scala
val flightRefinedDf = spark.sql("""
		SELECT f.*
		,      orig.airport          AS origin_airport
		,      orig.city             AS origin_city
		,      dest.airport          AS destination_airport
		,      dest.city             AS destination_city
		FROM int_flight_t    f
		LEFT JOIN dp_airport_v1_t     orig
		    ON (f.origin_airport_iata = orig.iata)
		LEFT JOIN dp_airport_v1_t     dest
		    ON (f.destination_airport_iata = dest.iata)
""")
```

```scala
flightRefinedDf.write.parquet("s3a://flight-bucket/refined/flights")

flightRefinedDf.createOrReplaceTempView("ref_flight_t")
```

##### Flight Delays

```
val flightDelayRefinedDf = spark.sql("""
		SELECT arrival_delay, origin_airport_iata, destination_airport_iata,
		    CASE
		         WHEN arrival_delay > 360 THEN 'Very Long Delays'
		         WHEN arrival_delay > 120 AND arrival_delay < 360 THEN 'Long Delays'
		         WHEN arrival_delay > 60 AND arrival_delay < 120 THEN 'Short Delays'
		         WHEN arrival_delay > 0 and arrival_delay < 60 THEN 'Tolerable Delays'
		         WHEN arrival_delay = 0 THEN 'No Delays'
		         ELSE 'Early'
		    END AS flight_delays
		         FROM int_flight_t
		         ORDER BY arrival_delay DESC
""")
```

```scala
flightDelayRefinedDf.write.parquet("s3a://flight-bucket/refined/flight_delays")

flightDelayRefinedDf.createOrReplaceTempView("ref_flight_delay_t")
```

#### Data Product Zone

##### Flight Enriched

```scala
val flightDpDf = spark.sql("""
		SELECT f.year
		,   f.month
		,   f.day_of_month
		,   f.day_of_week
		,   f.departure_time
		,   f.scheduled_departure_time
		,   f.arrival_time
		,   f.scheduled_arrival_time
		,   f.unique_carrier
		,   f.flight_number
		,   f.tail_number
		,   f.actual_elapsed_time
		,   f.estimated_elapsed_time
		,   f.airtime
		,   f.arrival_delay
		,   f.departure_delay
		,   f.origin_airport_iata
		,   f.destination_airport_iata
		,   f.distance
		,   f.taxi_in
		,   f.taxi_out
		,   f.cancelled
		,   f.cancellation_code
		,   f.cancellation_reason
		,   f.diverted
		,   f.carrier_delay
		,   f.weather_delay
		,   f.nas_delay
		,   f.security_delay
		,   f.late_aircraft_delay
		FROM ref_flight_t    f
""")
```

```scala
flightDpDf.write.parquet("s3a://flight-bucket/dp/v1/flights")

flightDpDf.createOrReplaceTempView("dp_flight_v1_t")
```

##### Flight Delays

```scala
val flightDelayDpDf = spark.sql("""
		SELECT fd.arrival_delay
		,   fd.origin_airport_iata
		,   fd.destination_airport_iata
		,   fd.flight_delays
		FROM ref_flight_delay_t    fd
""")
```

```scala
flightDelayDpDf.write.parquet("s3a://flight-bucket/dp/v1/flight_delays")

flightDelayDpDf.createOrReplaceTempView("dp_flight_delays_v1_t")
```

## Virtualized (Partial)

```
docker exec -ti hive-metastore hive
```

```
CREATE DATABASE flight_db;
USE flight_db;
```

### Airport Domain

#### Data Product Zone

Create a Hive table on the `S3` objects in the Data Product Zone

```sql
DROP TABLE dp_airport_v1_t;
CREATE EXTERNAL TABLE dp_airport_v1_t ( iata string
                             , airport string
                             , city string
                             , state string
                             , country string
                             , latitude double
                             , longitude double)
STORED AS parquet
LOCATION 's3a://flight-bucket/dp/v1/airports';
```

### Flight Domain

#### Integration Zone

Create a Hive table on the `S3` objects in the Integration Zone

```sql
DROP TABLE int_flight_t;
CREATE EXTERNAL TABLE int_flight_t ( year integer
											, month integer
											, day_of_month integer
											, day_of_week integer
											, departure_time integer
											, scheduled_departure_time integer
											, arrival_time integer
											, scheduled_arrival_time integer
											, unique_carrier string
											, flight_number string
											, tail_number string
											, actual_elapsed_time integer
											, estimated_elapsed_time integer
											, airtime integer
											, arrival_delay integer
											, departure_delay integer
											, origin_airport_iata string
											, destination_airport_iata string
											, distance integer
											, taxi_in integer
											, taxi_out integer
											, cancelled integer
											, cancellation_code string
											, cancellation_reason string
											, diverted string
											, carrier_delay integer
											, weather_delay integer
											, nas_delay integer
											, security_delay integer
											, late_aircraft_delay integer)
STORED AS parquet
LOCATION 's3a://flight-bucket/integration/flights';
```

#### Refined Zone

##### Flight Enriched

```bash
docker exec -it trino-cli trino --server trino-1:8080
```

```bash
use minio.flight_db;
```

```sql
CREATE VIEW ref_flight
AS 
SELECT f.*
,      orig.airport          AS origin_airport
,      orig.city             AS origin_city
,      dest.airport          AS destination_airport
,      dest.city             AS destination_city
FROM int_flight_t    f
LEFT JOIN dp_airport_v1_t     orig
    ON (f.origin_airport_iata = orig.iata)
LEFT JOIN dp_airport_v1_t     dest
    ON (f.destination_airport_iata = dest.iata);
```

##### Flight Delays

```sql
CREATE VIEW ref_flight_delays
AS 
SELECT arrival_delay, origin_airport_iata, destination_airport_iata,
    CASE
         WHEN arrival_delay > 360 THEN 'Very Long Delays'
         WHEN arrival_delay > 120 AND arrival_delay < 360 THEN 'Long Delays'
         WHEN arrival_delay > 60 AND arrival_delay < 120 THEN 'Short Delays'
         WHEN arrival_delay > 0 and arrival_delay < 60 THEN 'Tolerable Delays'
         WHEN arrival_delay = 0 THEN 'No Delays'
         ELSE 'Early'
    END AS flight_delays
         FROM int_flight_t
         ORDER BY arrival_delay DESC;
```

#### Data Product Zone

##### Flight Enriched

```sql
CREATE VIEW dp_flight_v1
AS
SELECT f.year
,   f.month
,   f.day_of_month
,   f.day_of_week
,   f.departure_time
,   f.scheduled_departure_time
,   f.arrival_time
,   f.scheduled_arrival_time
,   f.unique_carrier
,   f.flight_number
,   f.tail_number
,   f.actual_elapsed_time
,   f.estimated_elapsed_time
,   f.airtime
,   f.arrival_delay
,   f.departure_delay
,   f.origin_airport_iata
,   f.destination_airport_iata
,   f.distance
,   f.taxi_in
,   f.taxi_out
,   f.cancelled
,   f.cancellation_code
,   f.cancellation_reason
,   f.diverted
,   f.carrier_delay
,   f.weather_delay
,   f.nas_delay
,   f.security_delay
,   f.late_aircraft_delay
FROM ref_flight  f;
```

##### Flight Delays

```sql
CREATE OR REPLACE VIEW dp_flight_delays_v1
AS
SELECT fd.arrival_delay
,   fd.origin_airport_iata
,   fd.destination_airport_iata
,   fd.flight_delays
FROM ref_flight_delays fd;
```

## Virtualized (Full)

### Airport Domain

```bash
docker exec -it trino-cli trino --server trino-1:8080
```

#### Integration Zone

```sql
use minio.flight_db;

CREATE VIEW int_airport_t
AS
SELECT a.iata
,      a.airport
,      a.city
,      a.state
,      a.country
,      a.lat         AS latitude
,      a.long        AS longitude
FROM postgresql.mdm.airport_t  a;
```

#### Data Product Zone

```sql
DROP VIEW IF EXISTS dp_airport_v1_v;
CREATE VIEW dp_airport_v1_v
AS
SELECT a.iata
,      a.airport
,      a.city
,      a.state
,      a.country
,      a.latitude
,      a.longitude
FROM int_airport_t  a;
```

### Flight Domain

```bash
docker exec -it trino-cli trino --server trino-1:8080
```

#### Integration Zone

```sql
use minio.flight_db;

CREATE VIEW int_flight_t
AS
SELECT year
,	month
,  day_of_month
, 	day_of_week
,  departure_time
,  scheduled_departure_time
,  arr_time	AS arrival_time

FROM postgresql.operations.flight_t  f;
```

#### Data Product Zone

```sql
DROP VIEW IF EXISTS dp_airport_v1_v;
CREATE VIEW dp_airport_v1_v
AS
SELECT a.iata
,      a.airport
,      a.city
,      a.state
,      a.country
,      a.latitude
,      a.longitude
FROM int_airport_t  a;
```