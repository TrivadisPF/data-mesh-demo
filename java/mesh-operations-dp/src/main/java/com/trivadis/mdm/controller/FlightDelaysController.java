package com.trivadis.mdm.controller;

import com.trivadis.mdm.entity.Flight;
import com.trivadis.mdm.entity.FlightDelays;
import com.trivadis.mdm.entity.FlightDelaysDTO;
import com.trivadis.mdm.repository.FlightDelaysRepository;
import com.trivadis.mdm.service.FlightDelaysService;
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
@RequestMapping("/dp/flightDelays")
public class FlightDelaysController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FlightDelaysController.class);

    private final FlightDelaysService flightDelaysService;

    public FlightDelaysController(FlightDelaysService flightDelaysService) {
        this.flightDelaysService = flightDelaysService;
    }

    @GetMapping("{originAirportIata}")
    public ResponseEntity<List<FlightDelaysDTO>> getAirportByIata(@PathVariable String originAirportIata) {
        List<FlightDelays> flightDelays = flightDelaysService.getFlightDelays(originAirportIata);

        List<FlightDelaysDTO> dtos = new ArrayList<>();
        for (FlightDelays flightDelay : flightDelays) {
            dtos.add(FlightDelaysConverter.convert(flightDelay));
        }

        return ResponseEntity.of(Optional.of(dtos));
    }


}