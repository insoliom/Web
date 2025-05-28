package com.example.api;

import com.example.jwt.JwtDecoder;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
public class UserInfoController {

    private final JwtDecoder jwtDecoder;

    public UserInfoController(JwtDecoder jwtDecoder) {
        this.jwtDecoder = jwtDecoder;
    }

    @GetMapping("/userinfo")
    public ResponseEntity<?> userInfo(
            @RequestHeader(value = "Authorization", required = false) String auth,
            @CookieValue(value = "access_token", required = false) String cookieToken) {

        String token = null;
        if (auth != null && auth.startsWith("Bearer ")) {
            token = auth.substring(7);
        } else if (cookieToken != null && !cookieToken.isBlank()) {
            token = cookieToken;
        }
        if (token == null || token.isBlank()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("no token");
        }

        Map<String, Object> claims;
        try {
            claims = jwtDecoder.decodeToken(token);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body("invalid token: " + ex.getMessage());
        }

        Map<String, Object> body = new HashMap<>();
        body.put("id",   claims.get("id"));
        body.put("name", claims.getOrDefault("displayName", claims.get("name")));

        @SuppressWarnings("unchecked")
        Map<String,Object> props = (Map<String,Object>) claims.get("properties");
        if (props != null && props.get("group") != null) {
            body.put("group", props.get("group"));
        }

        return ResponseEntity.ok(body);
    }
}

