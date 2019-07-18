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
import person.pluto.natcross.model.ClientConnectModel;
import person.pluto.natcross.model.ClientControlModel;
import person.pluto.natcross.model.InteractiveModel;
import person.pluto.natcross.model.InteractiveTypeEnum;
import person.pluto.natcross.model.ResultModel;

/**
 * <p>
 * 监听服务
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
                e.printStackTrace();
            }
        }
    }

    public void procMethod(Socket listenSocket) {
        try {
            InputStream inputStream = listenSocket.getInputStream();
            OutputStream outputStream = listenSocket.getOutputStream();
            InteractiveModel recvInteractiveModel = InteractiveUtil.recv(inputStream);

            log.info(recvInteractiveModel.toJSONString());

            String interactiveType = recvInteractiveModel.getInteractiveType();
            JSONObject jsonObject = recvInteractiveModel.getData();

            InteractiveTypeEnum interactiveTypeEnum = InteractiveTypeEnum.getEnumByName(interactiveType);
            if (interactiveTypeEnum == null) {
                InteractiveModel sendInteractiveModel = InteractiveModel.of(recvInteractiveModel.getInteractiveSeq(),
                        InteractiveTypeEnum.COMMON_REPLY, ResultModel.ofFail());
                InteractiveUtil.send(outputStream, sendInteractiveModel);
                return;
            }
            if (interactiveTypeEnum.equals(InteractiveTypeEnum.CLIENT_CONTROL)) {
                ClientControlModel clientControlModel = jsonObject.toJavaObject(ClientControlModel.class);
                ServerListenThread serverListenThread = ListenServerControl.get(clientControlModel.getListenPort());

                if (serverListenThread == null) {
                    InteractiveModel sendInteractiveModel = InteractiveModel.of(
                            recvInteractiveModel.getInteractiveSeq(), InteractiveTypeEnum.COMMON_REPLY,
                            ResultModel.ofFail());
                    InteractiveUtil.send(outputStream, sendInteractiveModel);
                    return;
                }

                InteractiveModel sendInteractiveModel = InteractiveModel.of(recvInteractiveModel.getInteractiveSeq(),
                        InteractiveTypeEnum.COMMON_REPLY, ResultModel.ofSuccess());
                InteractiveUtil.send(outputStream, sendInteractiveModel);

                serverListenThread.setControlSocket(listenSocket);
                return;
            }
            if (interactiveTypeEnum.equals(InteractiveTypeEnum.CLIENT_CONNECT)) {
                ClientConnectModel clientControlModel = jsonObject.toJavaObject(ClientConnectModel.class);
                Integer socketPortByPartKey = CommonFormat
                        .getSocketPortByPartKey(clientControlModel.getSocketPartKey());
                ServerListenThread serverListenThread = ListenServerControl.get(socketPortByPartKey);

                if (serverListenThread == null) {
                    InteractiveModel sendInteractiveModel = InteractiveModel.of(
                            recvInteractiveModel.getInteractiveSeq(), InteractiveTypeEnum.COMMON_REPLY,
                            ResultModel.ofFail());
                    InteractiveUtil.send(outputStream, sendInteractiveModel);
                }

                InteractiveModel sendInteractiveModel = InteractiveModel.of(recvInteractiveModel.getInteractiveSeq(),
                        InteractiveTypeEnum.COMMON_REPLY, ResultModel.ofSuccess());
                InteractiveUtil.send(outputStream, sendInteractiveModel);

                serverListenThread.doSetPartClient(clientControlModel.getSocketPartKey(), listenSocket);
                return;
            }

            InteractiveModel sendInteractiveModel = InteractiveModel.of(recvInteractiveModel.getInteractiveSeq(),
                    InteractiveTypeEnum.COMMON_REPLY, ResultModel.ofFail());
            InteractiveUtil.send(outputStream, sendInteractiveModel);
            return;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start() {
        this.isAlive = true;
        if (!this.isAlive()) {
            super.start();
        }
    }

    public void cancell() {
        isAlive = false;

        if (listenServerSocket != null) {
            try {
                listenServerSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public Integer getListenPort() {
        return this.listenPort;
    }

}
