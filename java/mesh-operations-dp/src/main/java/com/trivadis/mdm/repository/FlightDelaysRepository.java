package com.trivadis.mdm.repository;

import com.trivadis.mdm.entity.FlightDelays;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FlightDelaysRepository {

    @Select("SELECT arrival_delay, origin_airport_iata, destination_airport_iata, flight_delays" +
            " FROM dp_flight_delays_v1_t WHERE origin_airport_iata = #{iata}")
    @Results({
        @Result(property = "arrivalDelay", column = "arrival_delay"),
            @Result(property = "originAirportIata", column = "origin_airport_iata"),
            @Result(property = "destinationAirportIata", column = "destination_airport_iata"),
            @Result(property = "flightDelays", column = "flight_delays"),
    })
    List<FlightDelays> findDelaysByOriginAirport(@Param("iata") String iata);
    
}
