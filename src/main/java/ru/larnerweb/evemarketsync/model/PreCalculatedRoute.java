package ru.larnerweb.evemarketsync.model;

import com.vladmihalcea.hibernate.type.array.ListArrayType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.List;

@Data
@Builder
@Entity
@Table(name = "precalculated_routes")
@IdClass(RoutePK.class)
@TypeDef(name = "list-array", typeClass = ListArrayType.class)
@NoArgsConstructor
@AllArgsConstructor
public class PreCalculatedRoute {
    @Id
    int from_system_id;
    @Id
    int to_system_id;
    @Id
    double security_status;

    int length;
    @Type(type = "list-array")
    @Column(columnDefinition = "integer[]")
    List<Integer> path;
}
