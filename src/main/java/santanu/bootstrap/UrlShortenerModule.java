package santanu.bootstrap;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import redis.clients.jedis.JedisPool;
import santanu.UrlShortenerConfiguration;
import santanu.core.storage.UrlStore;
import santanu.core.storage.impl.InMemoryStore;
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
    public JedisPool providesJedisPool(UrlShortenerConfiguration urlShortenerConfiguration) {
        return new JedisPool(urlShortenerConfiguration.getRedisConfig().getHostname(),
                urlShortenerConfiguration.getRedisConfig().getPort());
    }


}
