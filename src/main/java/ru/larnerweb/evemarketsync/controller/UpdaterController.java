package ru.larnerweb.evemarketsync.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.larnerweb.evemarketsync.service.RouteCalculateService;

@Controller
@RequestMapping("/update")
@RequiredArgsConstructor
public class UpdaterController {

    private final RouteCalculateService routeCalculateService;

    @RequestMapping(path = "/routes/{sec}")
    public void computeRoutes(@PathVariable Double sec){
        routeCalculateService.calculateRouteLengths(sec);
    }
}
