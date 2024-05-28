package santanu.core.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import santanu.core.entity.ShortUrl;
import santanu.core.storage.UrlStore;

import java.util.Optional;

@Singleton
public class RedirectionService {
    private final UrlStore urlStore;

    @Inject
    public RedirectionService(UrlStore urlStore) {
        this.urlStore = urlStore;
    }

    public Optional<ShortUrl> get(String id) {
        return urlStore.get(id);
    }
}
