package santanu.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class CreateShortenedUrlResponse {
    @JsonProperty
    private final String shortUrl;
    @JsonProperty
    private final String expandedUrl;

    public CreateShortenedUrlResponse(String shortUrl, String expandedUrl) {
        this.shortUrl = shortUrl;
        this.expandedUrl = expandedUrl;
    }
}
