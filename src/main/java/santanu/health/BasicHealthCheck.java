package santanu.health;

import com.codahale.metrics.health.HealthCheck;
import com.google.inject.Singleton;
import redis.clients.jedis.JedisPool;

@Singleton
public class BasicHealthCheck extends HealthCheck {

    @Override
    protected Result check() throws Exception {
        return Result.healthy("OK");
    }
}
