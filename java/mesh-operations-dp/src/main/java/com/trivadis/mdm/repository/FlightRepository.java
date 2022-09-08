package com.trivadis.mdm.repository;

import com.trivadis.mdm.entity.Flight;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FlightRepository  {

    @Select("SELECT year, month, day_of_month, day_of_week, departure_time, scheduled_departure_time, arrival_time, scheduled_arrival_time, unique_carrier, flight_number" +
            ", tail_number, actual_elapsed_time, estimated_elapsed_time, airtime, arrival_delay, departure_delay, origin_airport_iata, origin_airport, origin_city" +
            ", destination_airport_iata, destination_airport, destination_city, distance, taxi_in, taxi_out, cancelled, cancellation_code, cancellation_reason, diverted" +
            " FROM dp_flight_v1_t WHERE origin_airport_iata = #{iata}")
    @Results({
        @Result(property = "year", column = "year"),
            @Result(property = "month", column = "month"),
            @Result(property = "dayOfMonth", column = "day_of_month"),
            @Result(property = "dayOfWeek", column = "day_of_week"),
            @Result(property = "departureTime", column = "departure_time"),
            @Result(property = "scheduledDepartureTime", column = "scheduled_departure_time"),
            @Result(property = "arrivalTime", column = "arrival_time"),
            @Result(property = "scheduledArrivalTime", column = "scheduled_arrival_time"),
            @Result(property = "uniqueCarrier", column = "unique_carrier"),
            @Result(property = "flightNumber", column = "flight_number"),
            @Result(property = "tailNumber", column = "tail_number"),
            @Result(property = "actualElapsedTime", column = "actual_elapsed_time"),
            @Result(property = "estimatedElapsedTime", column = "estimated_elapsed_time"),
            @Result(property = "airtime", column = "airtime"),
            @Result(property = "arrivalDelay", column = "arrival_delay"),
            @Result(property = "departureDelay", column = "departure_delay"),
            @Result(property = "originAirportIata", column = "origin_airport_iata"),
            @Result(property = "originAirport", column = "origin_airport"),
            @Result(property = "originCity", column = "origin_city"),
            @Result(property = "destinationAirportIata", column = "destination_airport_iata"),
            @Result(property = "destinationAirport", column = "destination_airport"),
            @Result(property = "destinationCity", column = "destination_city"),
            @Result(property = "distance", column = "distance"),
            @Result(property = "taxiIn", column = "taxi_in"),
            @Result(property = "taxiOut", column = "taxi_out"),
            @Result(property = "cancelled", column = "cancelled"),
            @Result(property = "cancellationCode", column = "cancellation_code"),
            @Result(property = "cancellationReason", column = "cancellation_reason"),
            @Result(property = "diverted", column = "diverted"),
    })
    List<Flight> findByOriginAirport(@Param("iata") String iata);

}
