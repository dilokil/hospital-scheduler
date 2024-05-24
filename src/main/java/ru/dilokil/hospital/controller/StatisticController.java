package ru.dilokil.hospital.controller;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.web.context.WebServerApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.SocketException;

@RestController
@RequestMapping("/statistic")
public class StatisticController {
    private WebServerApplicationContext serverApplicationContext;

    public StatisticController(WebServerApplicationContext serverApplicationContext) {
        this.serverApplicationContext = serverApplicationContext;
    }

    @GetMapping
    public ResponseEntity<NetworkHostAndPort> getHostAndPort() throws SocketException {
        NetworkHostAndPort stat = new NetworkHostAndPort();
        stat.setPort(serverApplicationContext.getWebServer().getPort());
        stat.setHost("localhost");

        return ResponseEntity.ok(stat);
    }

    @Data
    @NoArgsConstructor
    private class NetworkHostAndPort {
        String host;
        Integer port;
    }

}
