package santanu.health;

import com.codahale.metrics.health.HealthCheck;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Singleton
public class RedisHealth extends HealthCheck {
    private final JedisPool jedisPool;

    @Inject
    public RedisHealth(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    @Override
    protected Result check() throws Exception {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.isConnected() ? Result.healthy("OK") : Result.unhealthy("redis connection failed");
        } catch (Exception e) {
            return Result.unhealthy("redis connection failed");
        }
    }
}
