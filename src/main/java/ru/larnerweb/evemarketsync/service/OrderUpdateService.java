package ru.larnerweb.evemarketsync.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.util.retry.Retry;
import ru.larnerweb.evemarketsync.model.MarketOrder;
import ru.larnerweb.evemarketsync.model.Region;
import ru.larnerweb.evemarketsync.processor.OrderListProcessor;
import ru.larnerweb.evemarketsync.repository.RegionRepository;

import java.time.Duration;
import java.util.Objects;
import java.util.Random;

@Log4j2
@Service
@RequiredArgsConstructor
public class OrderUpdateService {

    private final WebClient client;
    private final RegionRepository regionRepository;
    private final OrderListProcessor orderListProcessor;

//    @Scheduled(cron = "0 0 * * * *") // every hour
    @Scheduled(fixedDelay = 3600000L) // every hour
    public void updateOrders() throws InterruptedException {
        Random random = new Random();
        int pause = random.nextInt(2);
        log.info("Запуск обновления ордеров на покупку/продажу через {} секунд", pause);
        Thread.sleep(pause * 1000);

        for (Region region : regionRepository.findAll()) {
            log.info("Обработка региона '{}'", region.getName());
            int pageCount;

            ResponseEntity<?> responseEntityMono = client
                    .method(HttpMethod.HEAD)
                    .uri("/markets/{region}/orders/?datasource=tranquility&order_type=all",
                            region.getRegion_id())
                    .retrieve()
                    .toEntity(MarketOrder[].class)
                    .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(5)))
                    .block();

            if (responseEntityMono != null) {

                pageCount = Integer.parseInt(
                        Objects.requireNonNullElse(
                                responseEntityMono.getHeaders().getFirst("X-Pages"),
                                "1"));

                for (int page = 1; page < pageCount + 1; page++) {
                    log.info("Обработка региона '{}' страница {}", region.getName(), page);

                    client.method(HttpMethod.GET)
                            .uri("/markets/{region}/orders/?datasource=tranquility&order_type=all&page={page}",
                                    region.getRegion_id(), page)
                            .retrieve()
                            .bodyToMono(MarketOrder[].class)
                            .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(5)))
                            .subscribe(
                                    orderListProcessor,
                                    log::error
                            );
                    Thread.sleep(500L);
                }
            }
            Thread.sleep(1000L);
        }
    }
}
