package person.pluto;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import person.pluto.natcross.entity.ListenPort;
import person.pluto.natcross.server.NatcrossServer;
import person.pluto.natcross.service.IListenPortService;
import person.pluto.natcross2.serverside.client.ClientServiceThread;

@SpringBootApplication
public class NatcrossBootApplication implements ApplicationRunner {

    public static void main(String[] args) {
        SpringApplication.run(NatcrossBootApplication.class, args);
    }

    @Autowired
    private ClientServiceThread clientServiceThread;

    @Autowired
    private IListenPortService listenPortService;

    @Autowired
    private NatcrossServer natcrossServer;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        clientServiceThread.start();

        QueryWrapper<ListenPort> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ListenPort::getOnStart, true);
        List<ListenPort> list = listenPortService.list(queryWrapper);

        for (ListenPort listenPort : list) {
            natcrossServer.createNewListen(listenPort);
        }

    }

}
