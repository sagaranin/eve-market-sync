package ru.larnerweb.evemarketsync.model;

import com.vladmihalcea.hibernate.type.array.ListArrayType;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.List;

@Data
@Entity
@Table(name = "systems")
@TypeDef(
        name = "list-array",
        typeClass = ListArrayType.class
)
public class PlanetSystem {
    @Id
    int system_id;
    int constellation_id;
    String name;
    String security_class;
    float security_status;
    int star_id;
    @Type(type = "list-array")
    @Column(columnDefinition = "integer[]")
    List<Integer> stargates;

}
