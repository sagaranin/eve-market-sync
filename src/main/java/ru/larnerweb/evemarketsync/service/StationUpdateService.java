package ru.larnerweb.evemarketsync.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.larnerweb.evemarketsync.model.Station;
import ru.larnerweb.evemarketsync.repository.OrderRepository;
import ru.larnerweb.evemarketsync.repository.StationsRepository;

import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class StationUpdateService {

    private final WebClient client;
    private final OrderRepository orderRepository;
    private final StationsRepository stationsRepository;

//    @Scheduled(fixedDelay = 3600000L)
    public void updateTypeIds() throws InterruptedException {
        log.info("Запуск обновления станций");
        List<Long> stationIdList = orderRepository.getUnknownStationIDs();

        for (Long stationId : stationIdList) {
            log.info("Update station {}", stationId);
            Thread.sleep(200L);
            client.method(HttpMethod.GET)
                    .uri("/universe/stations/{stationId}/?datasource=tranquility", stationId)
                    .retrieve()
                    .bodyToMono(Station.class).retry()
                    .subscribe(
                            stationsRepository::save,
                            log::error
                    );
        }
        log.info("Обновление станций завершено");
    }
}
