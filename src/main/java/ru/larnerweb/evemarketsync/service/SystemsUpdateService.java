package ru.larnerweb.evemarketsync.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.util.retry.Retry;
import ru.larnerweb.evemarketsync.model.MarketType;
import ru.larnerweb.evemarketsync.model.PlanetSystem;
import ru.larnerweb.evemarketsync.repository.OrderRepository;
import ru.larnerweb.evemarketsync.repository.PlanetSystemRepository;
import ru.larnerweb.evemarketsync.repository.TypeRepository;

import java.time.Duration;
import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class SystemsUpdateService {

    private final WebClient client;
    private final OrderRepository orderRepository;
    private final PlanetSystemRepository systemRepository;

//    @Scheduled(fixedDelay = 3600000L)
    public void updateTypeIds() throws InterruptedException {
        log.info("Запуск обновления систем");
        Integer[] systems = client.method(HttpMethod.GET)
                .uri("/universe/systems/?datasource=tranquility")
                .retrieve()
                .bodyToMono(Integer[].class)
                .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(5))).block();

        assert systems != null;
        for (Integer systemId : systems) {
            log.info("Update system {}", systemId);
            Thread.sleep(200L);
            client.method(HttpMethod.GET)
                    .uri("/universe/systems/{systemId}/?datasource=tranquility", systemId)
                    .retrieve()
                    .bodyToMono(PlanetSystem.class)
                    .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(5)))
                    .subscribe(
                            systemRepository::save,
                            log::error
                    );
        }
        log.info("Обновление систем завершено");
    }
}
