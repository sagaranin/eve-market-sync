package ru.larnerweb.evemarketsync.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RouteItem {
    int from_system_id;
    int to_system_id;
    double security_status;
}
