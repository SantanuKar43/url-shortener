package santanu.core.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

@Singleton
@Slf4j
public class KeyLockService {
    private static final int WAIT_TIME = 500;
    private static final int LEASE_TIME = 2000;
    private static final TimeUnit TIME_UNIT = TimeUnit.MILLISECONDS;

    private final RedissonClient redissonClient;

    @Inject
    public KeyLockService(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    public Optional<Lock> acquireLock(String key) {
        RLock lock = redissonClient.getLock(key);
        try {
            lock.tryLock(WAIT_TIME, LEASE_TIME, TIME_UNIT);
        } catch (Exception e) {
            log.error("Error occurred while trying to acquire lock", e);
            return Optional.empty();
        }
        return Optional.of(lock);
    }
}
