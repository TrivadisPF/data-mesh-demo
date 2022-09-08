package com.trivadis.mdm.entity;

import lombok.*;


@Data
@ToString
@EqualsAndHashCode
@Builder
@AllArgsConstructor
public class FlightDelays {

    private Integer arrivalDelay;
    private String originAirportIata;
    private String destinationAirportIata;
    private String flightDelays;
}
