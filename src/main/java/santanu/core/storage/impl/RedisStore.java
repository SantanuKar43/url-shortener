package santanu.core.storage.impl;

import com.google.inject.Inject;
import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import santanu.core.entity.ShortUrl;
import santanu.core.storage.UrlStore;

import java.net.URI;
import java.util.Optional;

public class RedisStore implements UrlStore {
    private static final String ID_PREFIX = "URL_ID_";
    private static final String URL_PREFIX = "URL_FULL_";
    private final JedisPool jedisPool;

    @Inject
    public RedisStore(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    @Override
    public Optional<ShortUrl> get(String id) {
        try (Jedis jedis = jedisPool.getResource()) {
            String url = jedis.get(formatKey(id, ID_PREFIX));
            if (StringUtils.isBlank(url)) return Optional.empty();
            return Optional.of(new ShortUrl(id, new URI(url)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<ShortUrl> getByUrl(String url) {
        try (Jedis jedis = jedisPool.getResource()) {
            String id = jedis.get(formatKey(url, URL_PREFIX));
            if (StringUtils.isBlank(id)) return Optional.empty();
            return Optional.of(new ShortUrl(id, new URI(url)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ShortUrl save(ShortUrl shortUrl) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.set(formatKey(shortUrl.getId(), ID_PREFIX), shortUrl.getExpandedUrl().toString());
            jedis.set(formatKey(shortUrl.getExpandedUrl().toString(), URL_PREFIX), shortUrl.getId());
            return shortUrl;
        }
    }

    @Override
    public boolean delete(String id) {
        try (Jedis jedis = jedisPool.getResource()) {
            Optional<ShortUrl> shortUrl = get(id);
            if (shortUrl.isPresent()) {
                jedis.del(formatKey(shortUrl.get().getId(), ID_PREFIX));
                jedis.del(formatKey(shortUrl.get().getExpandedUrl().toString(), URL_PREFIX));
                return true;
            }
            return false;
        }
    }

    private String formatKey(String id, String prefix) {
        return prefix + id;
    }
}
