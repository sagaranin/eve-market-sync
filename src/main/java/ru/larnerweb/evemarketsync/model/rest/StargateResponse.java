package ru.larnerweb.evemarketsync.model.rest;

import lombok.Data;

@Data
public class StargateResponse {
    int stargate_id;
    String name;
    int system_id;
    int type_id;
    StargateDestination destination;
}

