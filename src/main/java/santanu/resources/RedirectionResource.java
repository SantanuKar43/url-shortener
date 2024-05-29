package santanu.resources;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import santanu.core.entity.ShortUrl;
import santanu.core.service.RedirectionService;

import java.util.Optional;

@Singleton
@Path("/")
@Slf4j
public class RedirectionResource {
    private final RedirectionService redirectionService;

    @Inject
    public RedirectionResource(RedirectionService redirectionService) {
        this.redirectionService = redirectionService;
    }
    @GET
    @Path("{short_url_id}")
    public Response get(@PathParam("short_url_id") String shortUrlId) {
        try {
            Optional<ShortUrl> shortUrl = redirectionService.get(shortUrlId);
            return shortUrl.isPresent() ?
                    Response.status(Response.Status.FOUND).location(shortUrl.get().getExpandedUrl()).build()
                    : Response.status(Response.Status.NOT_FOUND).entity("URL may be deleted or expired!").build();
        } catch (Exception e) {
            log.error("Error occurred while redirecting:", e);
            return Response.serverError().entity(e.getMessage()).build();
        }
    }
}
