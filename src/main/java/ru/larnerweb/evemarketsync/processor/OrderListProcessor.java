package ru.larnerweb.evemarketsync.processor;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ru.larnerweb.evemarketsync.model.MarketOrder;
import ru.larnerweb.evemarketsync.repository.OrderRepository;

import java.util.List;
import java.util.function.Consumer;

@Log4j2
@Service
@RequiredArgsConstructor
public class OrderListProcessor implements Consumer<MarketOrder[]> {

    private final OrderRepository orderRepository;

    @Override
    public void accept(MarketOrder[] orderList) {
        log.info("Обработка списка ордеров...");
        orderRepository.saveAll(List.of(orderList));
        log.info("Oрдера сохранены!");
    }
}
