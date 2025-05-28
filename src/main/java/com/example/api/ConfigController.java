package com.example.api;

import com.example.openidconnect.OpenIdConnectProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/config")
public class ConfigController {

    private final OpenIdConnectProperties oidcProps;

    @Value("${websocket.endpoint}")
    private String websocketEndpoint;

    public ConfigController(OpenIdConnectProperties oidcProps) {
        this.oidcProps = oidcProps;
    }

    @GetMapping
    public ResponseEntity<Map<String, String>> getConfig() {
        return ResponseEntity.ok(Map.of(
                "oidcEndpoint",     oidcProps.getEndpoint(),
                "clientId",         oidcProps.getClientId(),
                "websocketEndpoint", websocketEndpoint
        ));
    }

public static class ConfigDto {
        private String oidcEndpoint;
        private String clientId;
        private String websocketEndpoint;

        public ConfigDto(String oidcEndpoint, String clientId, String websocketEndpoint) {
            this.oidcEndpoint      = oidcEndpoint;
            this.clientId          = clientId;
            this.websocketEndpoint = websocketEndpoint;
        }
        public String getOidcEndpoint()     { return oidcEndpoint; }
        public String getClientId()         { return clientId; }
        public String getWebsocketEndpoint(){ return websocketEndpoint; }
    }
}
