package ru.larnerweb.evemarketsync.processor;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.util.retry.Retry;
import ru.larnerweb.evemarketsync.model.Region;
import ru.larnerweb.evemarketsync.repository.RegionRepository;

import java.time.Duration;
import java.util.function.Consumer;

@Log4j2
@Service
@RequiredArgsConstructor
public class RegionDetailsProcessor implements Consumer<Long> {

    private final WebClient client;
    private final RegionRepository regionRepository;

    @Override
    public void accept(Long regionId) {
        client.method(HttpMethod.GET)
                .uri("/universe/regions/{region}/?datasource=tranquility&language=en", regionId)
                .retrieve()
                .bodyToMono(Region.class)
                .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(1)))
                .subscribe(
                        regionRepository::save,
                        log::error
                );
    }
}
