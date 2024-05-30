package santanu.bootstrap;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import santanu.UrlShortenerConfiguration;
import santanu.core.config.RedisConfig;
import santanu.core.storage.UrlStore;
import santanu.core.storage.impl.RedisStore;

public class UrlShortenerModule extends AbstractModule {
    private final UrlShortenerConfiguration urlShortenerConfiguration;

    public UrlShortenerModule(UrlShortenerConfiguration urlShortenerConfiguration) {
        this.urlShortenerConfiguration = urlShortenerConfiguration;
    }

    @Override
    public void configure() {
        binder().bind(UrlStore.class).to(RedisStore.class);
    }

    @Singleton
    @Provides
    public UrlShortenerConfiguration providesUrlShortenerConfiguration() {
        return this.urlShortenerConfiguration;
    }

    @Singleton
    @Provides
    public RedissonClient providesRedissonClient(UrlShortenerConfiguration urlShortenerConfiguration) {
        Config config = new Config();
        config.useSingleServer().setAddress(getFormattedRedisUrl(urlShortenerConfiguration.getRedisConfig()));
        return Redisson.create(config);
    }

    private String getFormattedRedisUrl(RedisConfig redisConfig) {
        return "redis://" + redisConfig.getHostname() + ":" + redisConfig.getPort();
    }


}
