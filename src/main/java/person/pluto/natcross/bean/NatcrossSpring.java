package person.pluto.natcross.bean;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import person.pluto.natcross.common.NatcrossConstants;
import person.pluto.natcross.serveritem.ClientServiceThread;

@Configuration
public class NatcrossSpring {

    @Bean
    public ClientServiceThread getClientServiceThread() throws IOException {
        return new ClientServiceThread(NatcrossConstants.CLIENT_SERVER_PORT);
    }
}
