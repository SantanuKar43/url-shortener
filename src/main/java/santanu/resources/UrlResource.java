package santanu.resources;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import santanu.core.service.UrlService;

@Path("/url")
@Produces(MediaType.APPLICATION_JSON)
@Singleton
@Slf4j
public class UrlResource {
    private final UrlService urlService;

    @Inject
    public UrlResource(UrlService urlService) {
        this.urlService = urlService;
    }

    @POST
    public Response createShortenedUrl(String url) {
        try {
            return Response.ok(urlService.createShortenedUrl(url)).build();
        } catch (Exception e) {
            log.error("Error occurred while creating shortened url: ", e);
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @DELETE
    public Response delete(String id) {
        try {
            boolean deleted = urlService.deleteShortUrl(id);
            return deleted ? Response.ok().build() : Response.status(Response.Status.NOT_FOUND).build();
        } catch (Exception e) {
            log.error("Error occurred while deleting:", e);
            return Response.serverError().entity(e.getMessage()).build();
        }
    }
}
