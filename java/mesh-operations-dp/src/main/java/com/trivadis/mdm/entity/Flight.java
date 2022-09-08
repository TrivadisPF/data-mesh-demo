package com.trivadis.mdm.entity;

import lombok.*;


@Data
@ToString
@EqualsAndHashCode
@Builder
@AllArgsConstructor
public class Flight {

    private Integer year;
    private Integer month;
    private Integer dayOfMonth;
    private Integer dayOfWeek;
    private Integer departureTime;
    private Integer scheduledDepartureTime;
    private Integer arrivalTime;
    private Integer scheduledArrivalTime;
    private String uniqueCarrier;
    private String flightNumber;
    private String tailNumber;
    private Integer actualElapsedTime;
    private Integer estimatedElapsedTime;
    private Integer airtime;
    private Integer arrivalDelay;
    private Integer departureDelay;
    private String originAirportIata;
    private String originAirport;
    private String originCity;
    private String destinationAirportIata;
    private String destinationAirport;
    private String destinationCity;
    private Integer distance;
    private Integer taxiIn;
    private Integer taxiOut;
    private Integer cancelled;
    private String cancellationCode;
    private String cancellationReason;
    private String diverted;


}
