package com.trivadis.mdm.controller;

import com.trivadis.mdm.entity.Flight;
import com.trivadis.mdm.entity.FlightDTO;

import java.util.Optional;

public class FlightConverter {
    public static Optional<FlightDTO> convert (Flight flightDO) {
        FlightDTO dto = new FlightDTO(flightDO.getId(), flightDO.getYear(), flightDO.getMonth(), flightDO.getDayOfMonth(), flightDO.getDayOfWeek(), flightDO.getDepartureTime()
                , flightDO.getUniqueCarrier(), flightDO.getFlighNumber(), flightDO.getTailNumber(), flightDO.getAirtime(), flightDO.getArrivalDelay(), flightDO.getDepartureDelay()
                , flightDO.getOriginAirportIata(), flightDO.getDestinationAirportIata(), flightDO.getDistance());
        return Optional.of(dto);
    }
}
