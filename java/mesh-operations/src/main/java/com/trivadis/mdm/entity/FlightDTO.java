package com.trivadis.mdm.entity;

public record FlightDTO(Integer id, Integer year, Integer month, Integer dayOfMonth, Integer dayOfWeek, Integer departureTime, String uniqueCarrier, String flightNumber, String tailNumber
        , Integer airtime, Integer arrivalDelay, Integer departureDelay, String originAirportIata, String desinationAirportIata, Integer distance) {

}

