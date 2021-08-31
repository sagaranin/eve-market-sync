package ru.larnerweb.evemarketsync.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.larnerweb.evemarketsync.model.MarketType;

@Repository
public interface TypeRepository extends CrudRepository<MarketType, Long> {
}
