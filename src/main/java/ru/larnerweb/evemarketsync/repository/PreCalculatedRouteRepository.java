package ru.larnerweb.evemarketsync.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.larnerweb.evemarketsync.model.PreCalculatedRoute;
import ru.larnerweb.evemarketsync.model.RoutePK;

@Repository
public interface PreCalculatedRouteRepository extends CrudRepository<PreCalculatedRoute, RoutePK> {
}
