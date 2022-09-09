package com.trivadis.mdm.service;

import com.trivadis.mdm.entity.Flight;
import com.trivadis.mdm.entity.FlightDelays;
import com.trivadis.mdm.repository.FlightDelaysRepository;
import com.trivadis.mdm.repository.FlightRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FlightDelaysService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FlightDelaysService.class);

    private final FlightDelaysRepository flightDelaysRepository;


    public FlightDelaysService(FlightDelaysRepository flightDelaysRepository) {
        this.flightDelaysRepository = flightDelaysRepository;
    }

    /**
     * Get's the flight delays by iata code
     *
     * @param originAirportIata The number
     * @return The flight if exists
     */
    public List<FlightDelays> getFlightDelays(String originAirportIata) {
        LOGGER.info("getFlightDelays({})", originAirportIata);

        return flightDelaysRepository.findDelaysByOriginAirport(originAirportIata);
    }

}
