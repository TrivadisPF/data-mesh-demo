package com.trivadis.mdm.service;

import com.trivadis.mdm.entity.Flight;
import com.trivadis.mdm.repository.FlightRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FlightService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FlightService.class);

    private final FlightRepository flightRepository;


    public FlightService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    /**
     * Get's the airport by iata code
     *
     * @param originAirportIata The number
     * @return The flight if exists
     */
    public List<Flight> getFlights(String originAirportIata) {
        LOGGER.info("getFlight({})", originAirportIata);

        return flightRepository.findByOriginAirport(originAirportIata);
    }

}
