package santanu;

import com.google.inject.Guice;
import com.google.inject.Injector;
import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;
import santanu.bootstrap.UrlShortenerModule;
import santanu.health.BasicHealthCheck;
import santanu.health.RedisHealth;
import santanu.resources.HelloWorld;
import santanu.resources.RedirectionResource;
import santanu.resources.UrlResource;

public class UrlShortenerApplication extends Application<UrlShortenerConfiguration> {

    public static void main(final String[] args) throws Exception {
        new UrlShortenerApplication().run(args);
    }

    @Override
    public String getName() {
        return "url-shortener";
    }

    @Override
    public void initialize(final Bootstrap<UrlShortenerConfiguration> bootstrap) {
        // TODO: application initialization
    }

    @Override
    public void run(final UrlShortenerConfiguration configuration,
                    final Environment environment) {

        Injector injector = Guice.createInjector(new UrlShortenerModule(configuration));
        environment.healthChecks().register("basic", injector.getInstance(BasicHealthCheck.class));
        environment.healthChecks().register("redis", injector.getInstance(RedisHealth.class));
        environment.jersey().register(new HelloWorld());
        environment.jersey().register(injector.getInstance(RedirectionResource.class));
        environment.jersey().register(injector.getInstance(UrlResource.class));
    }

}
