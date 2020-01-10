package person.pluto.natcross.bean;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import person.pluto.natcross.model.SercretModel;
import person.pluto.natcross2.model.InteractiveModel;
import person.pluto.natcross2.serverside.client.ClientServiceThread;
import person.pluto.natcross2.serverside.client.config.IClientServiceConfig;
import person.pluto.natcross2.serverside.client.config.SecretSimpleClientServiceConfig;
import person.pluto.natcross2.serverside.client.config.SimpleClientServiceConfig;

@Configuration
public class NatcrossSpring {

    @Bean("sercret")
    @Primary
    @ConfigurationProperties(prefix = "natcross")
    public SercretModel getSercret() {
        return new SercretModel();
    }

    @Bean("clientServiceConfig")
    @Primary
    @ConfigurationProperties(prefix = "natcross.client")
    public IClientServiceConfig<InteractiveModel, InteractiveModel> getClientServiceConfig(
            @Qualifier("sercret") SercretModel sercret) {

        if (sercret.isValid()) {
            SecretSimpleClientServiceConfig secretSimpleClientServiceConfig = new SecretSimpleClientServiceConfig();
            secretSimpleClientServiceConfig.setBaseAesKey(sercret.getAeskey());
            secretSimpleClientServiceConfig.setTokenKey(sercret.getTokenKey());
            return secretSimpleClientServiceConfig;
        } else {
            return new SimpleClientServiceConfig();
        }

    }

    @Bean("clientServiceThread")
    @Primary
    public ClientServiceThread getClientServiceThread(
            @Qualifier("clientServiceConfig") IClientServiceConfig<InteractiveModel, InteractiveModel> config)
            throws IOException {
        return new ClientServiceThread(config);
    }

}
