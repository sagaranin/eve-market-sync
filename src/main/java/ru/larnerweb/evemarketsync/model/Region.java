package ru.larnerweb.evemarketsync.model;

import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.Id;


@Data
@Entity(name = "regions")
public class Region {
    @Id
    Long region_id;
    String name;
    String description;
}
