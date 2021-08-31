package ru.larnerweb.evemarketsync.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.larnerweb.evemarketsync.model.Station;

@Repository
public interface StationsRepository extends CrudRepository<Station, Long> {
}
