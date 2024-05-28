package santanu.core.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import santanu.UrlShortenerConfiguration;
import santanu.api.CreateShortenedUrlResponse;
import santanu.core.IdGenerator;
import santanu.core.entity.ShortUrl;
import santanu.core.storage.UrlStore;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

@Singleton
public class UrlService {
    private final UrlStore urlStore;
    private final IdGenerator idGenerator;
    private final UrlShortenerConfiguration urlShortenerConfiguration;

    @Inject
    public UrlService(UrlStore urlStore, IdGenerator idGenerator, UrlShortenerConfiguration urlShortenerConfiguration) {
        this.urlStore = urlStore;
        this.idGenerator = idGenerator;
        this.urlShortenerConfiguration = urlShortenerConfiguration;
    }

    public CreateShortenedUrlResponse createShortenedUrl(String url) {
        validate(url);
        URI uri = null;
        try {
            uri = new URI(url);
        } catch (URISyntaxException e) {
            throw new RuntimeException("invalid url");
        }
        Optional<ShortUrl> existing = urlStore.getByUrl(url);
        if (existing.isPresent()) {
            return new CreateShortenedUrlResponse(createFullShortUrl(existing.get().getId()),
                    existing.get().getExpandedUrl().toString());
        }
        String id = idGenerator.generate(url);
        int retry = 5;
        while (urlStore.get(id).isPresent() && retry > 0) {
            id = idGenerator.generateWithBuffer(url);
            retry--;
        }
        if (retry == 0) {
            throw new RuntimeException("Unable to shorten URL, please try again later");
        }
        ShortUrl shortUrl = new ShortUrl(id, uri);
        urlStore.save(shortUrl);
        return new CreateShortenedUrlResponse(createFullShortUrl(shortUrl.getId()), url);
    }

    private void validate(String url) {
        // TODO
    }

    private String createFullShortUrl(String id) {
        return urlShortenerConfiguration.getShortUrlPrefix() + id;
    }

    public boolean deleteShortUrl(String id) {
        return urlStore.delete(id);
    }

}
