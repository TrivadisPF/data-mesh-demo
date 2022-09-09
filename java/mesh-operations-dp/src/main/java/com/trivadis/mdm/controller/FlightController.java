package com.trivadis.mdm.controller;

import com.trivadis.mdm.entity.Flight;
import com.trivadis.mdm.entity.FlightDTO;
import com.trivadis.mdm.service.FlightService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/dp/flights")
public class FlightController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FlightController.class);

    private final FlightService flightService;

    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    @GetMapping("{originAirportIata}")
    public ResponseEntity<List<FlightDTO>> getAirportByIata(@PathVariable String originAirportIata) {
        List<Flight> flights = flightService.getFlights(originAirportIata);

        List<FlightDTO> dtos = new ArrayList<>();
        for (Flight flight : flights) {
            dtos.add(FlightConverter.convert(flight));
        }

        return ResponseEntity.of(Optional.of(dtos));
    }


}