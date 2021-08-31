package ru.larnerweb.evemarketsync.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "types")
public class MarketType {
    @Id
    int type_id;
    String name;
    float capacity;
    String description;
    int group_id;
    float mass;
    float volume;
    float packaged_volume;
    int portion_size;
    String published;
    float radius;

}
