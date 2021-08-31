package ru.larnerweb.evemarketsync.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ru.larnerweb.evemarketsync.model.PlanetSystem;
import ru.larnerweb.evemarketsync.model.PreCalculatedRoute;
import ru.larnerweb.evemarketsync.model.RouteItem;
import ru.larnerweb.evemarketsync.model.RoutePK;
import ru.larnerweb.evemarketsync.repository.OrderRepository;
import ru.larnerweb.evemarketsync.repository.PlanetSystemRepository;
import ru.larnerweb.evemarketsync.repository.PreCalculatedRouteRepository;
import ru.larnerweb.evemarketsync.repository.StargateRepository;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Log4j2
@Service
@RequiredArgsConstructor
public class RouteCalculateService {

    private final StargateRepository stargateRepository;
    private final PlanetSystemRepository systemRepository;
    private final OrderRepository orderRepository;
    private final PreCalculatedRouteRepository routeRepository;
    private final Map<Integer, Map<Integer, Double>> graph = new ConcurrentHashMap<>();

    public void calculateRouteLengths(Double minSecStatus) {
        log.info("Starting route lengths calculation");
        List<Integer> systemIds = orderRepository.getAllSystemIds();
        List<RoutePK> allRoutes = new LinkedList<>();

        for (int from : systemIds) {
            for (int to : systemIds) {
                allRoutes.add(new RoutePK(from, to, minSecStatus));
            }
        }
        log.info("generated {} routes to find", allRoutes.size());
        allRoutes.stream().parallel()
                .forEach(
                        r -> {
                            if (!routeRepository.existsById(r)) {
                                List<Integer> path = findPath(r.getFrom_system_id(), r.getTo_system_id(), minSecStatus);
                                if (path.size() > 0)
                                    routeRepository.save(new PreCalculatedRoute(
                                            r.getFrom_system_id(),
                                            r.getTo_system_id(),
                                            minSecStatus,
                                            path.size() - 1,
                                            path)
                                    );
                            }
                        }
                );
        log.info("generated {} routes to find", allRoutes.size());
    }


    public List<Integer> findPath(Integer from, Integer to, Double minSecStatus) {

        int pathLenLimit = 100;
        Set<Integer> visitedNodes = new HashSet<>();
        LinkedList<LinkedList<Integer>> queue = new LinkedList<>();
        Set<Integer> queueFilterSet = new HashSet<>();

        queue.add(new LinkedList<>(List.of(from)));

        while (queue.size() > 0) {
            LinkedList<Integer> currentPath = queue.pollFirst();
            visitedNodes.add(currentPath.getLast());

            if (currentPath.getLast().equals(to)) {
                return currentPath;
            } else {
                for (Integer child : Optional.ofNullable(graph.get(currentPath.getLast())).orElse(Map.of()).keySet()) {
                    if (!visitedNodes.contains(child)
                            && !queueFilterSet.contains(child)
                            && graph.get(currentPath.getLast()).get(child) >= minSecStatus ) {

                        LinkedList<Integer> currentPathCopy = new LinkedList<>(currentPath);
                        currentPathCopy.add(child);

                        if (currentPathCopy.size() <= pathLenLimit) {
                            queue.add(currentPathCopy);
                            queueFilterSet.add(child);
                        } else {
                            return List.of();
                        }
                    }
                }
            }
        }
        return List.of();
    }

    @PostConstruct
    private void init() {
        stargateRepository.findAll().stream()
                .map(
                    stargate -> {
                        Optional<PlanetSystem> ps = systemRepository.findById(stargate.getSystem_id());
                        if (ps.isPresent()) {
                            return RouteItem.builder()
                                    .from_system_id(stargate.getSystem_id())
                                    .to_system_id(stargate.getDestination_system_id())
                                    .security_status(ps.get().getSecurity_status())
                                    .build();
                        } else {
                            return RouteItem.builder()
                                    .from_system_id(stargate.getSystem_id())
                                    .to_system_id(stargate.getDestination_system_id()).build();
                        }
                    }
                ).forEach(
                        i -> {
                            if (graph.containsKey(i.getFrom_system_id())) {
                                graph.get(i.getFrom_system_id()).put(i.getTo_system_id(), i.getSecurity_status());
                            } else {
                                log.info("Graph node size {}", graph.size());
                                Map<Integer, Double> item = new ConcurrentHashMap<>();
                                item.put(i.getTo_system_id(), i.getSecurity_status());
                                graph.put(i.getFrom_system_id(), item);
                            }
                        }
                );
    }
}
