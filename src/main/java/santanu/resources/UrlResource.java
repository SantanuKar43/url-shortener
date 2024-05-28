package santanu.resources;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import santanu.core.service.UrlService;

@Path("/url")
@Produces(MediaType.APPLICATION_JSON)
@Singleton

public class UrlResource {
    private final UrlService urlService;

    @Inject
    public UrlResource(UrlService urlService) {
        this.urlService = urlService;
    }

    @POST

    public Response createShortenedUrl(String url) {
        return Response.ok(urlService.createShortenedUrl(url)).build();
    }

    @DELETE
    public Response delete(String id) {
        boolean deleted = urlService.deleteShortUrl(id);
        return deleted ? Response.ok().build() : Response.status(Response.Status.NOT_FOUND).build();
    }
}
