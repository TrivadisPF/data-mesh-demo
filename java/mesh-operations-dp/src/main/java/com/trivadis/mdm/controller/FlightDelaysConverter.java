package com.trivadis.mdm.controller;

import com.trivadis.mdm.entity.FlightDelays;
import com.trivadis.mdm.entity.FlightDelaysDTO;

public class FlightDelaysConverter {
    public static FlightDelaysDTO convert (FlightDelays flightDelaysDO) {
        FlightDelaysDTO dto = new FlightDelaysDTO(flightDelaysDO.getArrivalDelay(), flightDelaysDO.getOriginAirportIata(), flightDelaysDO.getDestinationAirportIata(), flightDelaysDO.getFlightDelays());
        return dto;
    }
}
