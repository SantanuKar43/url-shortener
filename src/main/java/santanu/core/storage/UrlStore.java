package santanu.core.storage;

import santanu.core.entity.ShortUrl;

import java.util.Optional;

public interface UrlStore {
    Optional<ShortUrl> get(String id);
    Optional<ShortUrl> getByUrl(String url);
    ShortUrl save(ShortUrl shortUrl);
    boolean delete(String id);
}
