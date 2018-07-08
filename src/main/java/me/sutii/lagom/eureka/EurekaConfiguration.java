package me.sutii.lagom.eureka;

import io.jsonwebtoken.lang.Assert;
import play.Configuration;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Singleton
public class EurekaConfiguration {
    private final String serverHostname;
    private final int serverPort;
    private String serviceProtocol;

    private final String instanceIp;
    private final int instancePort;
    private final String instanceName;

    @Inject
    public EurekaConfiguration(Configuration configuration) throws UnknownHostException {
        String hostnameKey = "lagom.discovery.eureka.server-hostname";
        serverHostname = configuration.getString(hostnameKey, null);
        Assert.hasText(serverHostname, String.format("You need to configure key %s with " +
                "a eureka server hostname", hostnameKey));

        String portKey = "lagom.discovery.eureka.server-port";
        serverPort = configuration.getInt(portKey);
        Assert.state(serverPort > 0, String.format(
                "You need to configure %s with a eureka server port", portKey));

        String serviceProtocolKey = "lagom.discovery.eureka.uri-scheme";
        this.serviceProtocol = configuration.getString(serviceProtocolKey, null);
        Assert.hasText(serviceProtocol, String.format(
                "You need to configure %s with a service protocol (http/https)",
                serviceProtocolKey));

        String instanceNameKey = "lagom.discovery.eureka.instance-name";
        instanceName = configuration.getString(instanceNameKey, null);
        Assert.hasText(instanceName, String.format("You need to configure key %s with " +
                "a name for your service", instanceNameKey));

        instanceIp = InetAddress.getLocalHost().getHostAddress();
        instancePort = configuration.getInt("play.server.http.port");
    }

    String getServerHostname() {
        return serverHostname;
    }

    int getServerPort() {
        return serverPort;
    }

    String getServiceProtocol() {
        return serviceProtocol;
    }

    String getInstanceIp() {
        return instanceIp;
    }

    int getInstancePort() {
        return instancePort;
    }

    public String getInstanceName() {
        return instanceName;
    }
}
