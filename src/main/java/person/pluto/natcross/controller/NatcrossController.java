package person.pluto.natcross.controller;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import person.pluto.natcross.entity.ListenPort;
import person.pluto.natcross.enumeration.PortTypeEnum;
import person.pluto.natcross.server.FileServer;
import person.pluto.natcross.server.NatcrossServer;
import person.pluto.natcross.service.IListenPortService;
import person.pluto.natcross2.serverside.listen.ListenServerControl;
import person.pluto.natcross2.serverside.listen.ServerListenThread;
import person.pluto.system.model.ResultModel;
import person.pluto.system.model.enumeration.ResultEnum;
import person.pluto.system.tools.ValidatorUtils;

@RestController
@RequestMapping("/natcross")
public class NatcrossController {

    @Autowired
    private IListenPortService listenPortService;

    @Autowired
    private NatcrossServer natcrossServer;

    @Autowired
    private FileServer fileServer;

    /**
     * 获取可用的端口类型
     *
     * @return
     * @author Pluto
     * @since 2020-01-10 13:57:07
     */
    @RequestMapping("/usablePortTypes")
    public ResultModel usablePortTypes() {
        PortTypeEnum[] values = PortTypeEnum.values();
        return ResultModel.ofSuccess(values);
    }

    /**
     * 创建新的监听，并保存记录
     *
     * @param listenPort
     * @return
     * @throws Exception
     * @author Pluto
     * @since 2019-07-22 14:20:35
     */
    @RequestMapping("/createListenPort")
    public ResultModel createListenPort(ListenPort listenPort,
            @RequestParam(name = "certFile", required = false) MultipartFile certFile) throws Exception {

        if (listenPort == null || listenPort.getListenPort() == null || listenPort.getDestIp() == null ||
                listenPort.getDestPort() == null) {
            return ResultEnum.PARAM_FAIL.toResultModel();
        }

        // 检查以前是否有设定保存
        QueryWrapper<ListenPort> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ListenPort::getListenPort, listenPort.getListenPort());
        int count = listenPortService.count(queryWrapper);
        if (count > 0) {
            return ResultEnum.LISTEN_PORT_HAS.toResultModel();
        }

        if (certFile != null && certFile.getSize() > 0L) {
            if (StringUtils.isBlank(listenPort.getCertPassword())) {
                return ResultEnum.PARAM_FAIL.toResultModel().setRetMsg("需要设置证书密码");
            }
            String saveCertFile = fileServer.saveCertFile(certFile, listenPort);
            listenPort.setCertPath(saveCertFile);
        } else {
            listenPort.setCertPath(null);
            listenPort.setCertPassword(null);
        }

        listenPort.setGmtCreate(null);
        listenPort.setGmtModify(null);

        boolean save = listenPortService.save(listenPort);
        if (!save) {
            return ResultEnum.SAVE_NEW_LISTEN_FAIL.toResultModel();
        }

        // 如果指定不自启动，则直接返回，不启动端口
        if (listenPort.getOnStart() == null || !listenPort.getOnStart()) {
            return ResultEnum.SUCCESS.toResultModel();
        }

        // 创建监听
        boolean createNewListen = natcrossServer.createNewListen(listenPort);
        if (!createNewListen) {
            return ResultEnum.CREATE_NEW_LISTEN_FAIL.toResultModel();
        }
        return ResultEnum.SUCCESS.toResultModel();
    }

    /**
     * 更新监听记录
     *
     * @param listenPort
     * @return
     * @throws Exception
     * @author Pluto
     * @since 2019-07-22 14:20:35
     */
    @RequestMapping("/updateListenPort")
    public ResultModel updateListenPort(ListenPort listenPort,
            @RequestParam(name = "certFile", required = false) MultipartFile certFile) throws Exception {

        if (listenPort == null || listenPort.getListenPort() == null || (listenPort.getDestIp() != null)) {
            return ResultEnum.PARAM_FAIL.toResultModel();
        }

        // 检查以前是否有设定保存
        QueryWrapper<ListenPort> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ListenPort::getListenPort, listenPort.getListenPort());
        int count = listenPortService.count(queryWrapper);
        if (count < 1) {
            return ResultEnum.LISTEN_PORT_NO_HAS.toResultModel();
        }

        if (certFile != null && certFile.getSize() > 0L) {
            if (StringUtils.isBlank(listenPort.getCertPassword())) {
                return ResultEnum.PARAM_FAIL.toResultModel().setRetMsg("需要设置证书密码");
            }
            String saveCertFile = fileServer.saveCertFile(certFile, listenPort);
            listenPort.setCertPath(saveCertFile);
        } else {
            listenPort.setCertPath(null);
            listenPort.setCertPassword(null);
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
     * @param listenPort
     * @return
     * @author Pluto
     * @since 2019-07-19 16:29:18
     */
    @RequestMapping("createNewListen")
    public ResultModel createNewListen(Integer listenPort) {

        ListenPort model = listenPortService.getById(listenPort);
        boolean createNewListen = false;
        if (model == null) {
            createNewListen = natcrossServer.createNewListen(listenPort);
        } else {
            createNewListen = natcrossServer.createNewListen(model);
        }

        if (!createNewListen) {
            return ResultEnum.CREATE_NEW_LISTEN_FAIL.toResultModel();
        }
        return ResultEnum.SUCCESS.toResultModel();
    }

    /**
     * 停止某个端口
     *
     * @param listenPort
     * @return
     * @author Pluto
     * @since 2019-07-22 15:16:26
     */
    @RequestMapping("stopListen")
    public ResultModel stopListen(Integer listenPort) {
        natcrossServer.removeListen(listenPort);
        return ResultEnum.SUCCESS.toResultModel();
    }

    /**
     * 移除某个监听
     *
     * @param listenPort
     * @return
     * @author Pluto
     * @since 2019-07-19 16:29:26
     */
    @RequestMapping("removeListen")
    public ResultModel removeListen(Integer listenPort) {
        natcrossServer.removeListen(listenPort);
        listenPortService.removeById(listenPort);
        return ResultEnum.SUCCESS.toResultModel();
    }

    /**
     * 获取全部监听信息
     *
     * @return
     * @author Pluto
     * @since 2019-07-19 16:29:33
     */
    @RequestMapping("getAllListenServer")
    public ResultModel getAllListenServer() {

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

    /**
     * 通过签名的方式进行获取全部接口信息
     *
     * @return
     * @author Pluto
     * @since 2020-01-10 10:14:09
     */
    @RequestMapping("/projectSign/getAllListenServer")
    public ResultModel getAllListenServerProjectSign() {
        return this.getAllListenServer();
    }

}
