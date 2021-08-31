package ru.larnerweb.evemarketsync.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoutePK implements Serializable {
    int from_system_id;
    int to_system_id;
    double security_status;
}
