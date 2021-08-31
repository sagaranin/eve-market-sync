package ru.larnerweb.evemarketsync.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Entity
@Table(name = "orders")
public class MarketOrder {
    @Id
    long order_id;
    long duration;
    String is_buy_order;
    Date issued;
    long location_id;
    long min_volume;
    double price;
    String range;
    long system_id;
    long type_id;
    long volume_remain;
    long volume_total;
    Date last_update = new Date();
}
