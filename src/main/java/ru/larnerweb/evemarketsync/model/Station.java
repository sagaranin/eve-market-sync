package ru.larnerweb.evemarketsync.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.List;

@Data
@Entity
@Table(name = "stations")
public class Station {
    @Id
    int station_id;
    float max_dockable_ship_volume;
    String name;
    float office_rental_cost;
    int owner;
    int race_id;
    float reprocessing_efficiency;
    float reprocessing_stations_take;
//    List<String> services;
    int system_id;
    int type_id;
}
