package ru.larnerweb.evemarketsync.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.util.retry.Retry;
import ru.larnerweb.evemarketsync.model.db.Stargate;
import ru.larnerweb.evemarketsync.model.rest.StargateResponse;
import ru.larnerweb.evemarketsync.repository.PlanetSystemRepository;
import ru.larnerweb.evemarketsync.repository.StargateRepository;

import java.time.Duration;
import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class StarGatesUpdateService {

    private final WebClient client;
    private final PlanetSystemRepository systemRepository;
    private final StargateRepository stargateRepository;

    public void updateStarGates() throws InterruptedException {
        log.info("Запуск обновления звездных врат");
        List<Integer> stargateIdList = systemRepository.getUnknownStargateIds();

        for (Integer stargateId : stargateIdList) {
            log.info("Update stargate {}", stargateId);
            Thread.sleep(200L);
            client.method(HttpMethod.GET)
                    .uri("/universe/stargates/{stargateId}/?datasource=tranquility", stargateId)
                    .retrieve()
                    .bodyToMono(StargateResponse.class)
                    .doOnError(log::error)
                    .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(5)))
                    .map(Stargate::new)
                    .subscribe(
                            stargateRepository::save,
                            log::error
                    );
        }
        log.info("Обновление звездных врат завершено");
    }
}
