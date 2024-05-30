package santanu.health;

import com.codahale.metrics.health.HealthCheck;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.redisson.api.RedissonClient;
import org.redisson.api.redisnode.RedisNodes;

@Singleton
public class RedisHealth extends HealthCheck {
    private final RedissonClient redissonClient;

    @Inject
    public RedisHealth(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Override
    protected Result check() {
        try {
            return redissonClient.getRedisNodes(RedisNodes.SINGLE).pingAll() ?
                    Result.healthy("OK") : Result.unhealthy("redis connection failed");
        } catch (Exception e) {
            return Result.unhealthy("redis connection failed");
        }
    }
}
