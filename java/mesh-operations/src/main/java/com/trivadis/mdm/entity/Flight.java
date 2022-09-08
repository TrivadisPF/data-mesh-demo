package com.trivadis.mdm.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="flight_t")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Flight {

    @Id
    private Integer id;

    private Integer year;
    private Integer month;
    private Integer dayOfMonth;
    private Integer dayOfWeek;
    @Column(name="depTime")
    private Integer departureTime;
    @Column(name="schedDepTime")
    private Integer scheduledDepartureTime;
    private String uniqueCarrier;
    private String flighNumber;
    private String tailNumber;
    @Column(name="act_elapsed_time")
    private Integer actualElapsedTime;
    @Column(name="est_elapsed_time")
    private Integer estimatedElapsedTime;
    @Column(name="airtime")
    private Integer airtime;
    @Column(name="arr_delay")
    private Integer arrivalDelay;
    @Column(name="dep_delay")
    private Integer departureDelay;
    @Column(name="origAirport")
    private String originAirportIata;
    @Column(name="destAirport")
    private String destinationAirportIata;
    @Column(name="dist")
    private Integer distance;
    @Column(name="taxi_in")
    private Integer taxiIn;
    @Column(name="taxi_out")
    private Integer taxiOut;
    @Column(name="cancelled")
    private Integer cancelled;
    @Column(name="cancellation_cd")
    private String cancellationCode;
    @Column(name="diverted")
    private String diverted;
    @Column(name="carrier_delay")
    private Integer carrierDelay;
    @Column(name="weather_delay")
    private Integer weatherDelay;
    @Column(name="nas_delay")
    private Integer masDelay;
    @Column(name="security_delay")
    private Integer securityDelay;
    @Column(name="late_aircraft_delay")
    private Integer lateAircraftDelay;


}
