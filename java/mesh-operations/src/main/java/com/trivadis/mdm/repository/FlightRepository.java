package com.trivadis.mdm.repository;

import com.trivadis.mdm.entity.Flight;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FlightRepository extends JpaRepository<Flight, String> {

    //public List<Flight> findByOriginAirportIataAndDestinatinoAirportIata(String origin, String destination);

    Optional<Flight> findById(Integer id);
}
