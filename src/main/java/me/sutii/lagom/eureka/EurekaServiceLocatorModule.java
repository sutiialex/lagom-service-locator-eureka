package me.sutii.lagom.eureka;

import com.lightbend.lagom.javadsl.api.ServiceLocator;
import play.api.Configuration;
import play.api.Environment;
import play.api.inject.Binding;
import play.api.inject.Module;
import scala.collection.Seq;

import javax.inject.Singleton;

public class EurekaServiceLocatorModule extends Module {

    @Override
    public Seq<Binding<?>> bindings(Environment environment, Configuration configuration) {
        return seq(
                bind(ServiceLocator.class).to(EurekaServiceLocator.class)
                        .in(Singleton.class)
        );
    }
}
