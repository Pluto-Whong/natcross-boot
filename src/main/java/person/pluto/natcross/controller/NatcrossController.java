package person.pluto.natcross.controller;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import person.pluto.natcross.entity.ListenPort;
import person.pluto.natcross.serveritem.ListenServerControl;
import person.pluto.natcross.serveritem.ServerListenThread;
import person.pluto.natcross.service.IListenPortService;
import person.pluto.system.model.ResultModel;
import person.pluto.system.model.enumeration.ResultEnum;
import person.pluto.system.tools.ValidatorUtils;

@RestController
@RequestMapping("/natcross")
public class NatcrossController {

    @Autowired
    private IListenPortService listenPortService;

    /**
     * 创建新的监听，并保存记录
     *
     * @author Pluto
     * @since 2019-07-22 14:20:35
     * @param listenPort
     * @return
     */
    @RequestMapping("/createListenPort")
    public ResultModel createListenPort(ListenPort listenPort) {

        if (listenPort == null || listenPort.getListenPort() == null || listenPort.getDestIp() == null
                || !ValidatorUtils.isIPv4Address(listenPort.getDestIp()) || listenPort.getDestPort() == null) {
            return ResultEnum.PARAM_FAIL.toResultModel();
        }

        // 检查以前是否有设定保存
        QueryWrapper<ListenPort> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ListenPort::getListenPort, listenPort.getListenPort());
        int count = listenPortService.count(queryWrapper);
        if (count > 0) {
            return ResultEnum.LISTEN_PORT_HAS.toResultModel();
        }

        listenPort.setGmtCreate(null);
        listenPort.setGmtModify(null);

        boolean save = listenPortService.save(listenPort);
        if (!save) {
            return ResultEnum.SAVE_NEW_LISTEN_FAIL.toResultModel();
        }

        // 如果指定不自启动，则直接返回，不启动端口
        if (listenPort.getOnStart() == null && !listenPort.getOnStart()) {
            return ResultEnum.SUCCESS.toResultModel();
        }

        // 创建监听
        ServerListenThread createNewListenServer = ListenServerControl
                .createNewListenServer(listenPort.getListenPort());
        if (createNewListenServer == null) {
            return ResultEnum.CREATE_NEW_LISTEN_FAIL.toResultModel();
        }
        return ResultEnum.SUCCESS.toResultModel();
    }

    /**
     * 更新监听记录
     *
     * @author Pluto
     * @since 2019-07-22 14:20:35
     * @param listenPort
     * @return
     */
    @RequestMapping("/updateListenPort")
    public ResultModel updateListenPort(ListenPort listenPort) {

        if (listenPort == null || listenPort.getListenPort() == null
                || (listenPort.getDestIp() != null && !ValidatorUtils.isIPv4Address(listenPort.getDestIp()))) {
            return ResultEnum.PARAM_FAIL.toResultModel();
        }

        // 检查以前是否有设定保存
        QueryWrapper<ListenPort> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ListenPort::getListenPort, listenPort.getListenPort());
        int count = listenPortService.count(queryWrapper);
        if (count < 1) {
            return ResultEnum.LISTEN_PORT_NO_HAS.toResultModel();
        }

        listenPort.setGmtCreate(null);
        listenPort.setGmtModify(null);

        if (listenPort.getOnStart() == null) {
            listenPort.setOnStart(false);
        }

        boolean save = listenPortService.updateById(listenPort);
        if (!save) {
            return ResultEnum.SAVE_NEW_LISTEN_FAIL.toResultModel();
        }

        return ResultEnum.SUCCESS.toResultModel();
    }

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
     * 停止某个端口
     *
     * @author Pluto
     * @since 2019-07-22 15:16:26
     * @param listenPort
     * @return
     */
    @RequestMapping("stopListen")
    public Object stopListen(Integer listenPort) {
        ListenServerControl.remove(listenPort);
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
        listenPortService.removeById(listenPort);
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

        QueryWrapper<ListenPort> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().orderByAsc(ListenPort::getListenPort);
        List<ListenPort> listenPortList = listenPortService.list(queryWrapper);

        Set<Integer> listenPortExist = new TreeSet<>();

        for (ListenPort model : listenPortList) {
            ServerListenThread serverListenThread = ListenServerControl.get(model.getListenPort());
            model.setServerListenThread(serverListenThread);
            listenPortExist.add(model.getListenPort());
        }

        List<ServerListenThread> serverListenList = ListenServerControl.getAll();
        List<ListenPort> independentList = new LinkedList<>();

        for (ServerListenThread model : serverListenList) {
            if (!listenPortExist.contains(model.getListenPort())) {
                ListenPort listenPort = new ListenPort();
                listenPort.setListenPort(model.getListenPort());
                listenPort.setPortDescribe("临时端口");
                listenPort.setServerListenThread(model);
                independentList.add(listenPort);
            }
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("listenPortList", listenPortList);
        jsonObject.put("independentList", independentList);

        return ResultEnum.SUCCESS.toResultModel().setData(jsonObject);
    }

}
