package com.trivadis.mdm.service;

import com.trivadis.mdm.entity.Airport;
import com.trivadis.mdm.repository.AirportRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AirportService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AirportService.class);

    private final AirportRepository airportRepository;


    public AirportService(AirportRepository airportRepository) {
        this.airportRepository = airportRepository;
    }

    /**
     * Get's the airport by iata code
     *
     * @param iata The number
     * @return The airport if exists
     */
    public Optional<Airport> getAirport(String iata) {
        LOGGER.info("getAirport({})", iata);

        return airportRepository.findById(iata);
    }

}
