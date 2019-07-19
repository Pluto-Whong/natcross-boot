package person.pluto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import person.pluto.natcross.serveritem.ClientServiceThread;

@SpringBootApplication
public class NatcrossBootApplication implements ApplicationRunner {

    public static void main(String[] args) {
        SpringApplication.run(NatcrossBootApplication.class, args);
    }

    @Autowired
    private ClientServiceThread clientServiceThread;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        clientServiceThread.start();
    }

}
