package com.example.jwt;


import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.example.openidconnect.OpenIdConnectProperties;
import java.net.URL;
import java.security.interfaces.RSAPublicKey;

@Component
public class JwtDecoder {

    private final OpenIdConnectProperties openIdConnectProperties;

    @Autowired
    public JwtDecoder(OpenIdConnectProperties openIdConnectProperties) {
        this.openIdConnectProperties = openIdConnectProperties;
    }

    public Claims decodeToken(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) throw new RuntimeException("Token format invalid");
            String headerJson = new String(java.util.Base64.getUrlDecoder().decode(parts[0]));
            String kid = com.fasterxml.jackson.databind.json.JsonMapper.builder()
                    .build().readTree(headerJson).get("kid").asText();

            JWKSet jwkSet = JWKSet.load(new URL(openIdConnectProperties.getEndpoint() + "/.well-known/jwks"));
            JWK jwk = jwkSet.getKeys().stream()
                    .filter(k -> k.getKeyID().equals(kid))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("No matching key for kid: " + kid));

            RSAPublicKey publicKey = (RSAPublicKey) jwk.toRSAKey().toPublicKey();

            // 3. Декодувати токен
            return Jwts.parserBuilder()
                    .setSigningKey(publicKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

        } catch (Exception e) {
            throw new RuntimeException("Failed to decode token", e);
        }
    }
}
