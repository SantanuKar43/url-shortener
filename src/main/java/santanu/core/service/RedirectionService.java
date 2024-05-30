package santanu.core.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import santanu.core.entity.ShortUrl;

import java.util.Optional;

@Singleton
public class RedirectionService {
    private final UrlService urlService;

    @Inject
    public RedirectionService(UrlService urlService) {
        this.urlService = urlService;
    }

    public Optional<ShortUrl> get(String id) {
        return urlService.get(id);
    }
}
