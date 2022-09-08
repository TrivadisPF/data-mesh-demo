package com.trivadis.mdm.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="airport_t")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Airport {

    @Id
    private String iata;
    private String airport;
    private String city;
    private String state;
    private String country;
    @Column(name="lat")
    private Double latitude;
    @Column(name="long")
    private Double longitude;

}
