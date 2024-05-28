package santanu.core.entity;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.net.URI;

@Getter
public class ShortUrl {
    @NotNull
    @NotEmpty
    private final String id;
    @NotNull
    private final URI expandedUrl;

    public ShortUrl(String id, URI url) {
        this.id = id;
        this.expandedUrl = url;
    }
}
