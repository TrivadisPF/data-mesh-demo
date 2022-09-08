package com.trivadis.mdm.repository;

import com.trivadis.mdm.entity.Airport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AirportRepository extends JpaRepository<Airport, String> {
}
