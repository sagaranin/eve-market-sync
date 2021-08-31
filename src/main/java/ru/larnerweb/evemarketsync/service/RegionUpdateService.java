package ru.larnerweb.evemarketsync.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.util.retry.Retry;
import ru.larnerweb.evemarketsync.processor.RegionListProcessor;

import java.time.Duration;

@Log4j2
@Service
@RequiredArgsConstructor
public class RegionUpdateService {

    private final WebClient client;
    private final RegionListProcessor regionListProcessor;

    @Scheduled(cron = "0 0 0 * * *") // every night
    public void updateRegions(){
        log.info("Запуск обновления регионов");
        client.method(HttpMethod.GET)
                .uri("/universe/regions?datasource=tranquility")
                .retrieve()
                .bodyToMono(Long[].class).retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(5)))
                .subscribe(
                        regionListProcessor,
                        log::error
                );
    }
}
