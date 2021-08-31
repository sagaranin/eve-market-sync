package ru.larnerweb.evemarketsync.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.larnerweb.evemarketsync.model.db.Stargate;


@Repository
public interface StargateRepository extends JpaRepository<Stargate, Long> {
}
