package com.trivadis.mdm.controller;

import com.trivadis.mdm.entity.FlightDTO;
import com.trivadis.mdm.service.FlightService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/flights")
public class FlightController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FlightController.class);

    private final FlightService flightService;

    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    @GetMapping("{id}")
    public ResponseEntity<FlightDTO> getAirportByIata(@PathVariable Integer id) {
        Optional<FlightDTO> dto = FlightConverter.convert(flightService.getFlight(id).get());
        return ResponseEntity.of(dto);
    }


}