package ru.larnerweb.evemarketsync.model.db;

import lombok.Data;
import ru.larnerweb.evemarketsync.model.rest.StargateResponse;

import javax.persistence.*;

@Data
@Entity
@Table(name = "stargates")
public class Stargate {
    @Id
    int stargate_id;
    String name;
    int system_id;
    int type_id;
    int destination_stargate_id;
    int destination_system_id;

    public Stargate() {
    }

    public Stargate(StargateResponse stargateResponse) {
        this.stargate_id = stargateResponse.getStargate_id();
        this.name = stargateResponse.getName();
        this.system_id = stargateResponse.getSystem_id();
        this.type_id = stargateResponse.getType_id();
        this.destination_stargate_id = stargateResponse.getDestination().getStargate_id();
        this.destination_system_id = stargateResponse.getDestination().getSystem_id();
    }
}
