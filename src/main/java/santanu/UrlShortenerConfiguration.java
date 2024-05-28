package santanu;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.core.Configuration;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import santanu.core.config.RedisConfig;

public class UrlShortenerConfiguration extends Configuration {
    @NotEmpty
    private String shortUrlPrefix;

    @NotNull
    private RedisConfig redisConfig;

    @JsonProperty
    public void setHostName(String shortUrlPrefix) {
        this.shortUrlPrefix = shortUrlPrefix;
    }

    @JsonProperty
    public String getShortUrlPrefix() {
        return shortUrlPrefix;
    }

    @JsonProperty
    public RedisConfig getRedisConfig() {
        return redisConfig;
    }

    @JsonProperty
    public void setRedisConfig(RedisConfig redisConfig) {
        this.redisConfig = redisConfig;
    }
}
