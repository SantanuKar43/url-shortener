package santanu.core.storage.impl;

import com.google.inject.Singleton;
import santanu.core.entity.ShortUrl;
import santanu.core.storage.UrlStore;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Singleton
public class InMemoryStore implements UrlStore {
    private final Map<String, ShortUrl> keyMap = new HashMap<>();
    private final Map<String, ShortUrl> urlMap = new HashMap<>();

    @Override
    public Optional<ShortUrl> get(String id) {
        return Optional.ofNullable(keyMap.get(id));
    }

    @Override
    public Optional<ShortUrl> getByUrl(String url) {
        return Optional.ofNullable(urlMap.get(url));
    }

    @Override
    public ShortUrl save(ShortUrl shortUrl) {
        if (keyMap.containsKey(shortUrl.getId())) {
            throw new RuntimeException("key already present");
        }
        keyMap.put(shortUrl.getId(), shortUrl);
        urlMap.put(shortUrl.getExpandedUrl().toString(), shortUrl);
        return shortUrl;
    }

    @Override
    public boolean delete(String id) {
        if (!keyMap.containsKey(id)) return false;
        ShortUrl url = keyMap.get(id);
        keyMap.remove(id);
        urlMap.remove(url.getExpandedUrl().toString());
        return true;
    }
}
