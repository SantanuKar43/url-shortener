package santanu.core.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import santanu.core.IdGenerator;
import santanu.core.entity.ShortUrl;
import santanu.core.storage.UrlStore;

import java.util.Optional;

@Singleton
public class RedirectionService {
    private final UrlStore urlStore;
    private final IdGenerator idGenerator;

    @Inject
    public RedirectionService(UrlStore urlStore, IdGenerator idGenerator) {
        this.urlStore = urlStore;
        this.idGenerator = idGenerator;
    }

    public Optional<ShortUrl> get(String id) {
        if (!idGenerator.isValid(id)) return Optional.empty();
        return urlStore.get(id);
    }
}
