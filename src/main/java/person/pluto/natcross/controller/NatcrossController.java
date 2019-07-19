package person.pluto.natcross.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import person.pluto.natcross.serveritem.ListenServerControl;
import person.pluto.natcross.serveritem.ServerListenThread;
import person.pluto.system.model.enumeration.ResultEnum;

@RestController
@RequestMapping("/natcross")
public class NatcrossController {

    /**
     * 创建新的监听
     *
     * @author Pluto
     * @since 2019-07-19 16:29:18
     * @param listenPort
     * @return
     */
    @RequestMapping("createNewListen")
    public Object createNewListen(Integer listenPort) {
        ServerListenThread createNewListenServer = ListenServerControl.createNewListenServer(listenPort);
        if (createNewListenServer == null) {
            return ResultEnum.CREATE_NEW_LISTEN_FAIL.toResultModel();
        }
        return ResultEnum.SUCCESS.toResultModel();
    }

    /**
     * 移除某个监听
     *
     * @author Pluto
     * @since 2019-07-19 16:29:26
     * @param listenPort
     * @return
     */
    @RequestMapping("removeListen")
    public Object removeListen(Integer listenPort) {
        ListenServerControl.remove(listenPort);
        return ResultEnum.SUCCESS.toResultModel();
    }

    /**
     * 获取全部监听信息
     *
     * @author Pluto
     * @since 2019-07-19 16:29:33
     * @return
     */
    @RequestMapping("getAllListenServer")
    public Object getAllListenServer() {
        List<ServerListenThread> all = ListenServerControl.getAll();
        return ResultEnum.SUCCESS.toResultModel().setData(all);
    }

}
