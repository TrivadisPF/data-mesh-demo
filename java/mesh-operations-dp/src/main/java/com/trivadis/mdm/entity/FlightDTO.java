package com.trivadis.mdm.entity;

public record FlightDTO(Integer year, Integer month, Integer dayOfMonth, Integer dayOfWeek, Integer departureTime, Integer scheduledDepartureTime, Integer arrivalTime, Integer scheduledArrivalTime
        , String uniqueCarrier, String flightNumber, String tailNumber, Integer actualElapsedTime, Integer estimatedElapsedTime
        , Integer airtime, Integer arrivalDelay, Integer departureDelay, String originAirportIata, String originAirport, String originCity, String destinationAirportIata, String destinationAirport, String destinationCity
        , Integer distance, Integer taxiIn, Integer taxiOut, Integer cancelled, String cancellationCode, String cancellationReason, String diverted) {

}

