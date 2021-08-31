package ru.larnerweb.evemarketsync.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.larnerweb.evemarketsync.model.Region;

@Repository
public interface RegionRepository extends CrudRepository<Region, Long> {
}
