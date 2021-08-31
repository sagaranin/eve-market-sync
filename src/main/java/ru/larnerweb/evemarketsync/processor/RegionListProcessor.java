package ru.larnerweb.evemarketsync.processor;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.function.Consumer;

@Log4j2
@Service
@RequiredArgsConstructor
public class RegionListProcessor implements Consumer<Long[]> {

    private final RegionDetailsProcessor regionDetailsProcessor;

    @Override
    public void accept(Long[] regionIdList) {
        Flux.just(regionIdList)
                .subscribe(
                        regionDetailsProcessor,
                        log::error
                );
    }
}
