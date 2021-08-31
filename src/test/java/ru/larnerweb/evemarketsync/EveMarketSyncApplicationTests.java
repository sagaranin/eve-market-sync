package ru.larnerweb.evemarketsync;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.larnerweb.evemarketsync.service.RouteCalculateService;

import java.util.List;

@Log4j2
@SpringBootTest
class EveMarketSyncApplicationTests {

	@Autowired
	RouteCalculateService routeCalculateService;

	@Test
	void contextLoads() {
	}

	@Test
	void findPath_0hop() {
		List<Integer> result = routeCalculateService.findPath(30001374, 30001374, 0.6);
	}

	@Test
	void findPath_1hop() {
		List<Integer> result = routeCalculateService.findPath(30001374, 30001375, 0.6);
	}

	@Test
	void findPath_2hop() {
		List<Integer> result = routeCalculateService.findPath(30001374, 30001404, 0.4);
	}

	@Test
	void findPath_xhop() {
		List<Integer> result = routeCalculateService.findPath(30001374, 30004090, 0.1);
	}

	@Test
	void findPath_Ahop() {
		List<Integer> result = routeCalculateService.findPath(30003831, 30002992, 0.1);
	}

}
