package person.pluto.natcross.serveritem;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import com.alibaba.fastjson.JSONObject;

import lombok.extern.slf4j.Slf4j;
import person.pluto.natcross.common.CommonFormat;
import person.pluto.natcross.common.InteractiveUtil;
import person.pluto.natcross.model.InteractiveModel;
import person.pluto.natcross.model.NatcrossResultModel;
import person.pluto.natcross.model.enumeration.InteractiveTypeEnum;
import person.pluto.natcross.model.enumeration.NatcrossResultEnum;
import person.pluto.natcross.model.interactive.ClientConnectModel;
import person.pluto.natcross.model.interactive.ClientControlModel;

/**
 * <p>
 * 客户端服务进程
 * </p>
 *
 * @author wangmin1994@qq.com
 * @since 2019-07-05 10:53:33
 */
@Slf4j
public class ClientServiceThread extends Thread {

    private boolean isAlive = false;
    private Integer listenPort;
    private ServerSocket listenServerSocket;

    public ClientServiceThread(Integer port) throws IOException {
        this.listenPort = port;
        listenServerSocket = new ServerSocket(port);
    }

    @Override
    public void run() {
        while (isAlive) {
            try {
                Socket listenSocket = listenServerSocket.accept();

                procMethod(listenSocket);

            } catch (IOException e) {
                log.warn("客户端服务进程 轮询等待出现异常", e);
            }
        }
    }

    /**
     * 处理方法
     *
     * @author Pluto
     * @since 2019-07-18 18:18:29
     * @param listenSocket
     */
    public void procMethod(Socket listenSocket) {
        try {
            InputStream inputStream = listenSocket.getInputStream();
            OutputStream outputStream = listenSocket.getOutputStream();
            InteractiveModel recvInteractiveModel = InteractiveUtil.recv(inputStream);

            log.info("客户端发来消息: {} ", recvInteractiveModel.toJSONString());

            String interactiveType = recvInteractiveModel.getInteractiveType();
            JSONObject jsonObject = recvInteractiveModel.getData();

            InteractiveTypeEnum interactiveTypeEnum = InteractiveTypeEnum.getEnumByName(interactiveType);
            if (interactiveTypeEnum == null) {
                // 没有已知的交互类型，返回异常
                InteractiveModel sendInteractiveModel = InteractiveModel.of(recvInteractiveModel.getInteractiveSeq(),
                        InteractiveTypeEnum.COMMON_REPLY, NatcrossResultEnum.UNKNOW_INTERACTIVE_TYPE.toResultModel());
                InteractiveUtil.send(outputStream, sendInteractiveModel);
                return;
            }
            if (interactiveTypeEnum.equals(InteractiveTypeEnum.CLIENT_CONTROL)) {
                // 客户端连入服务端
                ClientControlModel clientControlModel = jsonObject.toJavaObject(ClientControlModel.class);
                ServerListenThread serverListenThread = ListenServerControl.get(clientControlModel.getListenPort());

                if (serverListenThread == null) {
                    InteractiveModel sendInteractiveModel = InteractiveModel.of(
                            recvInteractiveModel.getInteractiveSeq(), InteractiveTypeEnum.COMMON_REPLY,
                            NatcrossResultEnum.NO_HAS_SERVER_LISTEN.toResultModel());
                    InteractiveUtil.send(outputStream, sendInteractiveModel);
                    return;
                }

                InteractiveModel sendInteractiveModel = InteractiveModel.of(recvInteractiveModel.getInteractiveSeq(),
                        InteractiveTypeEnum.COMMON_REPLY, NatcrossResultModel.ofSuccess());
                InteractiveUtil.send(outputStream, sendInteractiveModel);

                serverListenThread.setControlSocket(listenSocket);
                return;
            }
            if (interactiveTypeEnum.equals(InteractiveTypeEnum.CLIENT_CONNECT)) {
                // 新连接接入
                ClientConnectModel clientControlModel = jsonObject.toJavaObject(ClientConnectModel.class);
                Integer socketPortByPartKey = CommonFormat
                        .getSocketPortByPartKey(clientControlModel.getSocketPartKey());
                ServerListenThread serverListenThread = ListenServerControl.get(socketPortByPartKey);

                if (serverListenThread == null) {
                    InteractiveModel sendInteractiveModel = InteractiveModel.of(
                            recvInteractiveModel.getInteractiveSeq(), InteractiveTypeEnum.COMMON_REPLY,
                            NatcrossResultEnum.NO_HAS_SERVER_LISTEN.toResultModel());
                    InteractiveUtil.send(outputStream, sendInteractiveModel);
                }

                InteractiveModel sendInteractiveModel = InteractiveModel.of(recvInteractiveModel.getInteractiveSeq(),
                        InteractiveTypeEnum.COMMON_REPLY, NatcrossResultModel.ofSuccess());
                InteractiveUtil.send(outputStream, sendInteractiveModel);

                boolean doSetPartClient = serverListenThread.doSetPartClient(clientControlModel.getSocketPartKey(),
                        listenSocket);
                if (!doSetPartClient) {
                    listenSocket.close();
                }
                return;
            }

            // 前面检测了，但是没有，忘了写也是系统错误！！！
            InteractiveModel sendInteractiveModel = InteractiveModel.of(recvInteractiveModel.getInteractiveSeq(),
                    InteractiveTypeEnum.COMMON_REPLY, NatcrossResultModel.ofFail());
            InteractiveUtil.send(outputStream, sendInteractiveModel);
            return;

        } catch (IOException e) {
            log.warn("客户端新接入连接，处理时发生异常", e);
        }
    }

    /**
     * 启动
     */
    @Override
    public void start() {
        this.isAlive = true;
        if (!this.isAlive()) {
            super.start();
        }
    }

    /**
     * 退出
     *
     * @author Pluto
     * @since 2019-07-18 18:32:03
     */
    public void cancell() {
        isAlive = false;

        if (listenServerSocket != null) {
            try {
                listenServerSocket.close();
            } catch (IOException e) {
                log.warn("监听端口关闭异常", e);
            }
        }

    }

    /**
     * 获取监听端口
     *
     * @author Pluto
     * @since 2019-07-18 18:32:40
     * @return
     */
    public Integer getListenPort() {
        return this.listenPort;
    }

}
