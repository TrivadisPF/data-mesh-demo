package com.trivadis.mdm.controller;

import com.trivadis.mdm.entity.Airport;
import com.trivadis.mdm.entity.AirportDTO;

import java.util.Optional;

public class AirportConverter {
    public static Optional<AirportDTO> convert (Airport airportDO) {
        AirportDTO dto = new AirportDTO(airportDO.getIata(), airportDO.getAirport(), airportDO.getCity(), airportDO.getState(), airportDO.getCountry(), airportDO.getLatitude(), airportDO.getLongitude());
        return Optional.of(dto);
    }
}
