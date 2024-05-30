package santanu.core.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.commons.validator.routines.UrlValidator;
import santanu.UrlShortenerConfiguration;
import santanu.api.CreateShortenedUrlResponse;
import santanu.core.IdGenerator;
import santanu.core.entity.ShortUrl;
import santanu.core.storage.UrlStore;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

@Singleton
public class UrlService {
    private static final int MAX_URL_LENGTH = 4000;
    private static final int MAX_RETRIES = 5;

    private final UrlStore urlStore;
    private final IdGenerator idGenerator;
    private final UrlShortenerConfiguration urlShortenerConfiguration;
    private final KeyLockService lockService;

    @Inject
    public UrlService(UrlStore urlStore, IdGenerator idGenerator, UrlShortenerConfiguration urlShortenerConfiguration, KeyLockService lockService) {
        this.urlStore = urlStore;
        this.idGenerator = idGenerator;
        this.urlShortenerConfiguration = urlShortenerConfiguration;
        this.lockService = lockService;
    }

    public CreateShortenedUrlResponse createShortenedUrl(String url) {
        validate(url);
        URI uri = null;
        try {
            uri = new URI(url);
        } catch (URISyntaxException | NullPointerException e) {
            throw new RuntimeException("invalid URL!");
        }

        Optional<Lock> lock = lockService.acquireLock(url);
        if (lock.isEmpty()) {
            throw new RuntimeException("Unable to shorten URL, please try again later.");
        }
        try {
            Optional<ShortUrl> existing = urlStore.getByUrl(url);
            if (existing.isPresent()) {
                return new CreateShortenedUrlResponse(createFullShortUrl(existing.get().getId()),
                        existing.get().getExpandedUrl().toString());
            }
            String id = idGenerator.generate(url);
            Optional<Lock> idLock = lockService.acquireLock(id);
            if (idLock.isEmpty()) {
                throw new RuntimeException("Unable to shorten URL, please try again later.");
            }
            try {
                int retry = MAX_RETRIES;
                while (urlStore.get(id).isPresent() && retry > 0) {
                    id = idGenerator.generateWithBuffer(url);
                    retry--;
                }
                if (retry == 0) {
                    throw new RuntimeException("Unable to shorten URL, please try again later.");
                }
                ShortUrl shortUrl = new ShortUrl(id, uri);
                urlStore.save(shortUrl);
                return new CreateShortenedUrlResponse(createFullShortUrl(shortUrl.getId()), url);
            } finally {
                idLock.get().unlock();
            }
        } catch (Exception e) {
            throw new RuntimeException("Unable to shorten URL, please try again later.");
        } finally {
            lock.get().unlock();
        }
    }

    private void validate(String url) {
        if (!UrlValidator.getInstance().isValid(url)) {
            throw new RuntimeException("invalid URL!");
        }
        if (url.length() > MAX_URL_LENGTH) {
            throw new RuntimeException("URL is too long!");
        }
    }

    private String createFullShortUrl(String id) {
        return urlShortenerConfiguration.getShortUrlPrefix() + id;
    }

    public boolean deleteShortUrl(String id) {
        Optional<Lock> idLock = lockService.acquireLock(id);
        if (idLock.isEmpty()) {
            throw new RuntimeException("Unable to delete, please try again later.");
        }
        try {
            return urlStore.delete(id);
        } finally {
            idLock.get().unlock();
        }
    }

}
