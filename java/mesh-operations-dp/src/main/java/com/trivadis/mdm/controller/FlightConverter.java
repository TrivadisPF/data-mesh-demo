package com.trivadis.mdm.controller;

import com.trivadis.mdm.entity.Flight;
import com.trivadis.mdm.entity.FlightDTO;

public class FlightConverter {
    public static FlightDTO convert (Flight flightDO) {
        FlightDTO dto = new FlightDTO(flightDO.getYear(), flightDO.getMonth(), flightDO.getDayOfMonth(), flightDO.getDayOfWeek(), flightDO.getDepartureTime(), flightDO.getScheduledDepartureTime()
                , flightDO.getArrivalTime(), flightDO.getScheduledArrivalTime(), flightDO.getUniqueCarrier(), flightDO.getFlightNumber(), flightDO.getTailNumber(), flightDO.getActualElapsedTime(), flightDO.getEstimatedElapsedTime()
                , flightDO.getAirtime(), flightDO.getArrivalDelay(), flightDO.getDepartureDelay(), flightDO.getOriginAirportIata(), flightDO.getOriginAirport(), flightDO.getOriginCity(), flightDO.getDestinationAirportIata(), flightDO.getDestinationAirport()
                , flightDO.getDestinationCity(), flightDO.getDistance(), flightDO.getTaxiIn(), flightDO.getTaxiOut(), flightDO.getCancelled(), flightDO.getCancellationCode(), flightDO.getCancellationReason(), flightDO.getDiverted());
        return dto;
    }
}
