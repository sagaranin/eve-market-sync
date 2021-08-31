package ru.larnerweb.evemarketsync.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.larnerweb.evemarketsync.model.PlanetSystem;

import java.util.List;

@Repository
public interface PlanetSystemRepository extends CrudRepository<PlanetSystem, Integer> {
    @Query(value = "select distinct unnest(stargates) from systems order by 1", nativeQuery = true)
    List<Integer> getUnknownStargateIds();

    @Query(value = "select distinct system_id from systems order by 1", nativeQuery = true)
    List<Integer> getSystemIdDistinct();
}
