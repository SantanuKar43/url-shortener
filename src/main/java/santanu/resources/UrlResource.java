package santanu.resources;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import santanu.core.entity.ShortUrl;
import santanu.core.service.UrlService;

import java.util.Optional;

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

    @GET
    @Path("/preview/{short_url_id}")
    public Response preview(@PathParam("short_url_id") String id) {
        try {
            Optional<ShortUrl> shortUrl = urlService.get(id);
            return shortUrl.isPresent() ?
                    Response.status(Response.Status.FOUND).entity(shortUrl.get().getExpandedUrl()).build()
                    : Response.status(Response.Status.NOT_FOUND).entity("URL may be deleted or expired!").build();
        } catch (Exception e) {
            log.error("Error occurred:", e);
            return Response.serverError().entity(e.getMessage()).build();
        }
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
