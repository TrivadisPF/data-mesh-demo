package com.trivadis.mdm.controller;

import com.trivadis.mdm.entity.AirportDTO;
import com.trivadis.mdm.service.AirportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/airports")
public class AirportController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AirportController.class);

    private final AirportService airportService;

    public AirportController(AirportService airportService) {
        this.airportService = airportService;
    }

    @GetMapping("{iata}")
    public ResponseEntity<AirportDTO> getAirportByIata(@PathVariable String iata) {
        Optional<AirportDTO> dto = AirportConverter.convert(airportService.getAirport(iata).get());
        return ResponseEntity.of(dto);
    }


}