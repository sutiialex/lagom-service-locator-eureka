package me.sutii.lagom.eureka;

import com.lightbend.lagom.javadsl.api.Descriptor;
import com.lightbend.lagom.javadsl.api.ServiceLocator;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import io.jsonwebtoken.lang.Assert;
import org.apache.commons.lang3.NotImplementedException;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

import static java.util.concurrent.CompletableFuture.supplyAsync;
import static play.Logger.debug;
import static play.Logger.info;

public class EurekaServiceLocator implements ServiceLocator {
    private final EurekaClient eurekaClient;
    private final String serviceProtocol;

    @Inject
    public EurekaServiceLocator(EurekaConfiguration config) {
        this.eurekaClient = new EurekaClientConfig(config).getClient();
        serviceProtocol = config.getServiceProtocol();
    }

    @Override
    public CompletionStage<Optional<URI>> locate(String serviceName) {
        debug("Locating {}", serviceName);
        return supplyAsync(() -> Optional.of(getRandomServiceUri(serviceName)));
    }

    @Override
    public CompletionStage<Optional<URI>> locate(String s, Descriptor.Call<?, ?> call) {
        throw new NotImplementedException("locate isn't implemented");
    }

    @Override
    public <T> CompletionStage<Optional<T>> doWithService(
            String serviceName, Descriptor.Call<?, ?> serviceCall,
            Function<URI, CompletionStage<T>> function) {
        info("doingWithService {}", serviceName);
        URI randomServiceUri = getRandomServiceUri(serviceName);
        info("Got a serviceUri {} ", randomServiceUri.getHost());
        return function.apply(randomServiceUri).thenApply(Optional::of);
    }

    private URI getRandomServiceUri(String serviceName) {
        List<InstanceInfo> services =
                eurekaClient.getInstancesByVipAddress(serviceName, false);
        Assert.notEmpty(services, "No services registered for " + serviceName);
        InstanceInfo service = services.get(
                ThreadLocalRandom.current().nextInt(0, services.size()));
        String serviceAddress = service.getVIPAddress();
        int servicePort = service.getPort();
        try {
            return new URI(String.format("%s://%s:%s", serviceProtocol, serviceAddress,
                    servicePort));
        } catch (URISyntaxException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(
                    String.format("Could not create URI from" + " %s:%s",
                            serviceAddress, servicePort));
        }
    }
}
