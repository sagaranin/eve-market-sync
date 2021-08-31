package ru.larnerweb.evemarketsync.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.util.retry.Retry;
import ru.larnerweb.evemarketsync.model.MarketType;
import ru.larnerweb.evemarketsync.repository.OrderRepository;
import ru.larnerweb.evemarketsync.repository.TypeRepository;

import java.time.Duration;
import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class TypeUpdateService {

    private final WebClient client;
    private final OrderRepository orderRepository;
    private final TypeRepository typeRepository;

//    @Scheduled(fixedDelay = 3600000L)
    public void updateTypeIds() throws InterruptedException {
        log.info("Запуск обновления типов");
        List<Long> typeIdList = orderRepository.getUnknownTypeIDs();

        for (Long typeId : typeIdList) {
            log.info("Update type {}", typeId);
            Thread.sleep(200L);
            client.method(HttpMethod.GET)
                    .uri("/universe/types/{typeId}/?datasource=tranquility", typeId)
                    .retrieve()
                    .bodyToMono(MarketType.class).retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(5)))
                    .subscribe(
                            typeRepository::save,
                            log::error
                    );
        }
        log.info("Обновление типов завершено");
    }
}
