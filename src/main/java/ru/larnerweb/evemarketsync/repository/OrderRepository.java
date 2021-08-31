package ru.larnerweb.evemarketsync.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.larnerweb.evemarketsync.model.MarketOrder;

import java.util.List;

@Repository
public interface OrderRepository extends CrudRepository<MarketOrder, Long> {
    @Query(value = "select distinct type_id from orders where type_id not in (select type_id from types) order by type_id", nativeQuery = true)
    List<Long> getUnknownTypeIDs();

    @Query(value = "select distinct location_id from orders where location_id not in (select station_id from stations) order by location_id", nativeQuery = true)
    List<Long> getUnknownStationIDs();

    @Query(value = "select distinct system_id from orders where system_id not in (select system_id from systems) order by system_id", nativeQuery = true)
    List<Long> getUnknownSystemIds();

    @Query(value = "select distinct system_id from orders order by system_id", nativeQuery = true)
    List<Integer> getAllSystemIds();
}
