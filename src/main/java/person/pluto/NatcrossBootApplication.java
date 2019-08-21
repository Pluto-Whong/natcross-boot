package person.pluto;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import person.pluto.natcross.entity.ListenPort;
import person.pluto.natcross.serveritem.ClearInvalidSocketPartThread;
import person.pluto.natcross.serveritem.ClientServiceThread;
import person.pluto.natcross.serveritem.ListenServerControl;
import person.pluto.natcross.service.IListenPortService;

@SpringBootApplication
public class NatcrossBootApplication implements ApplicationRunner {

    public static void main(String[] args) {
        SpringApplication.run(NatcrossBootApplication.class, args);
    }

    @Autowired
    private ClientServiceThread clientServiceThread;

    @Autowired
    private ClearInvalidSocketPartThread clearInvalidSocketPartThread;

    @Autowired
    private IListenPortService listenPortService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        clientServiceThread.start();

        QueryWrapper<ListenPort> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ListenPort::getOnStart, true);
        List<ListenPort> list = listenPortService.list(queryWrapper);

        for (ListenPort listenPort : list) {
            ListenServerControl.createNewListenServer(listenPort.getListenPort());
        }

        clearInvalidSocketPartThread.start();
    }

}
