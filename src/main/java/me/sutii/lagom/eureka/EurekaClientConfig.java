package me.sutii.lagom.eureka;

import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.appinfo.EurekaInstanceConfig;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.appinfo.MyDataCenterInstanceConfig;
import com.netflix.appinfo.providers.EurekaConfigBasedInstanceInfoProvider;
import com.netflix.discovery.DefaultEurekaClientConfig;
import com.netflix.discovery.DiscoveryClient;
import com.netflix.discovery.EurekaClient;
import org.joda.time.DateTime;

import java.util.Arrays;
import java.util.List;

public class EurekaClientConfig {
    private final ApplicationInfoManager applicationInfoManager;
    private final EurekaClient eurekaClient;
    private final EurekaConfiguration config;

    public EurekaClientConfig(EurekaConfiguration config) {
        this.config = config;
        this.applicationInfoManager = initializeApplicationInfoManager();
        this.eurekaClient = new DiscoveryClient(applicationInfoManager,
                new MyEurekaClientConfig());
        this.applicationInfoManager.setInstanceStatus(InstanceInfo.InstanceStatus.UP);
    }

    private ApplicationInfoManager initializeApplicationInfoManager() {
        EurekaInstanceConfig instanceConfig = new MyEurekaInstanceConfig();
        InstanceInfo instanceInfo =
                new EurekaConfigBasedInstanceInfoProvider(instanceConfig).get();
        return new ApplicationInfoManager(instanceConfig, instanceInfo);
    }

    public EurekaClient getClient() {
        return eurekaClient;
    }

    private class MyEurekaClientConfig extends DefaultEurekaClientConfig {
        @Override
        public String getEurekaServerPort() {
            return "" + config.getServerPort();
        }

        @Override
        public String getEurekaServerDNSName() {
            return config.getServerHostname();
        }

        @Override
        public String[] getAvailabilityZones(String region) {
            return new String[]{"http://eureka-registry:8761/eureka/"};
        }

        @Override
        public List<String> getEurekaServerServiceUrls(String myZone) {
            return Arrays.asList("http://eureka-registry:8761/eureka/");
        }
    }

    private class MyEurekaInstanceConfig extends MyDataCenterInstanceConfig {
        @Override
        public String getInstanceId() {
            return Integer.toHexString((int)DateTime.now().getMillis()) +
                    ":" + config.getInstanceName() + ":" + config.getInstancePort();
        }

        @Override
        public String getIpAddress() {
            return config.getInstanceName();
        }

        @Override
        public String getVirtualHostName() {
            return config.getInstanceName();
        }

        @Override
        public String getSecureVirtualHostName() {
            return config.getInstanceName();
        }

        @Override
        public String getAppGroupName() {
            return null;
        }

        @Override
        public String getAppname() {
            return config.getInstanceName();
        }

        @Override
        public String getHostName(boolean refresh) {
            return config.getInstanceIp();
        }

        @Override
        public int getNonSecurePort() {
            return config.getInstancePort();
        }
    }

}
