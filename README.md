# A Service Locator for Eureka written in Java

**This work is inspired from the Consul client, [java
version](https://github.com/olapetersson-lagom-consul-client/lagom-consul-client)
and [scala version](https://raw.githubusercontent.com/jboner/lagom-service-locator-consul).

## Usage

### Prereq:
You need to have an [eureka instance](https://github.com/Netflix/eureka)
running which your services can register to.

Your application needs to enable the module in
`src/main/resources/application.conf` and add configuration for your eureka
server.

As the lib is currently not on maven central you need to clone it and perform a
`mvn clean install`

### Configuration

Add the mvn dependency in the pom of your service

```
<dependency>
  <groupId>me.sutii.lagom</groupId>
  <artifactId>eureka-client</artifactId>
  <version>0.0.1-ALPHA</version>
</dependency>
```

```
play.modules.enabled += me.sutii.lagom.eureka.EurekaServiceLocatorModule

lagom {
  discovery {
    eureka {
      server-hostname = "eureka-registry"  # hostname of the eureka server
      server-port     = 8761               # port of the eureka server
      uri-scheme      = "http"             # http/https
      instance-name   = "myservice"        # the name of the service you are registering
    }
  }
}
```

### Register a service

It will register the service automatically. You need to specify the service
name in instance-name above. It will pick up the IP address and port
automatically from Lagom's configuration.
