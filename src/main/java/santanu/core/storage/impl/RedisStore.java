package santanu.core.storage.impl;

import com.google.inject.Inject;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import santanu.core.entity.ShortUrl;
import santanu.core.storage.UrlStore;

import java.net.URI;
import java.util.Optional;

public class RedisStore implements UrlStore {
    private static final String ID_PREFIX = "URL_ID_";
    private static final String URL_PREFIX = "URL_FULL_";
    private final RedissonClient redisson;

    @Inject
    public RedisStore(RedissonClient redisson) {
        this.redisson = redisson;
    }

    @Override
    public Optional<ShortUrl> get(String id) {
        try {
            RBucket<String> bucket = redisson.getBucket(formatKey(id, ID_PREFIX));
            if (!bucket.isExists()) {
                return Optional.empty();
            }
            String url = bucket.get();
            return Optional.of(new ShortUrl(id, new URI(url)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<ShortUrl> getByUrl(String url) {
        try {
            RBucket<String> bucket = redisson.getBucket(formatKey(url, URL_PREFIX));
            if (!bucket.isExists()) {
                return Optional.empty();
            }
            String id = bucket.get();
            return Optional.of(new ShortUrl(id, new URI(url)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ShortUrl save(ShortUrl shortUrl) {
        try {
            RBucket<Object> idBucket = redisson.getBucket(formatKey(shortUrl.getId(), ID_PREFIX));
            idBucket.set(shortUrl.getExpandedUrl().toString());

            RBucket<Object> urlBucket = redisson.getBucket(formatKey(shortUrl.getExpandedUrl().toString(), URL_PREFIX));
            urlBucket.set(shortUrl.getId());
            return shortUrl;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean delete(String id) {
        try {
            Optional<ShortUrl> shortUrl = get(id);
            if (shortUrl.isPresent()) {
                redisson.getBucket(formatKey(shortUrl.get().getId(), ID_PREFIX)).delete();
                redisson.getBucket(formatKey(shortUrl.get().getExpandedUrl().toString(), URL_PREFIX)).delete();
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String formatKey(String id, String prefix) {
        return prefix + id;
    }
}
