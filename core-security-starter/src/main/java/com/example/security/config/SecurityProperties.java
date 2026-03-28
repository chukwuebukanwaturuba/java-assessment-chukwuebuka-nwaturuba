package com.example.security.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

// Configuration properties for the core-security-starter.
// All properties sit under the security.jwt prefix.
// Override them in your application.properties — at minimum change the secret.
@ConfigurationProperties(prefix = "security.jwt")
public class SecurityProperties {

    // Base64-encoded HMAC-SHA256 signing secret.
    private String secret = "Y2hhbmdlLXRoaXMtc2VjcmV0LWtleS1pbi1wcm9kdWN0aW9uLXBsZWFzZSE=";

    // Token TTL in milliseconds. Defaults to 24 hours.
    private long expiryMs = 86_400_000L;

    // The prefix expected in the Authorization header, including the trailing space.
    private String tokenPrefix = "Bearer ";

    // HTTP header that carries the token.
    private String headerName = "Authorization";

    public String getSecret() { return secret; }
    public void setSecret(String secret) { this.secret = secret; }

    public long getExpiryMs() { return expiryMs; }
    public void setExpiryMs(long expiryMs) { this.expiryMs = expiryMs; }

    public String getTokenPrefix() { return tokenPrefix; }
    public void setTokenPrefix(String tokenPrefix) { this.tokenPrefix = tokenPrefix; }

    public String getHeaderName() { return headerName; }
    public void setHeaderName(String headerName) { this.headerName = headerName; }
}
