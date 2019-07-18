package person.pluto.natcross.clientitem;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;

import lombok.extern.slf4j.Slf4j;
import person.pluto.natcross.common.IBelongControl;
import person.pluto.natcross.common.InteractiveUtil;
import person.pluto.natcross.model.ClientControlModel;
import person.pluto.natcross.model.ClientWaitModel;
import person.pluto.natcross.model.InteractiveModel;
import person.pluto.natcross.model.InteractiveTypeEnum;
import person.pluto.natcross.model.ResultEnum;
import person.pluto.natcross.model.ResultModel;
import person.pluto.natcross.serveritem.SocketPart;

/**
 * <p>
 * 客户端控制服务
 * </p>
 *
 * @author wangmin1994@qq.com
 * @since 2019-07-05 10:53:33
 */
@Slf4j
public class ClientControlThread extends Thread implements IBelongControl {

    private boolean isAlive = false;

    private String clientServiceIp;
    private Integer clientServicePort;
    private Integer listenServerPort;
    private String destIp;
    private Integer destPort;

    private Socket client;
    private OutputStream outputStream;
    private InputStream inputStream;

    private Map<String, SocketPart> socketPartMap = new TreeMap<>();

    public ClientControlThread(String clientServiceIp, Integer clientServicePort, Integer listenServerPort,
            String destIp, Integer destPort) throws IOException {
        this.setClientServiceIp(clientServiceIp);
        this.setClientServicePort(clientServicePort);
        this.setListenServerPort(listenServerPort);
        this.setDestIp(destIp);
        this.setDestPort(destPort);
    }

    public boolean createControl() throws UnknownHostException, IOException {
        this.client = new Socket(this.clientServiceIp, this.clientServicePort);
        this.outputStream = client.getOutputStream();
        this.inputStream = client.getInputStream();

        InteractiveModel interactiveModel = InteractiveModel.of(InteractiveTypeEnum.CLIENT_CONTROL,
                new ClientControlModel(this.listenServerPort));
        InteractiveUtil.send(this.outputStream, interactiveModel);

        InteractiveModel recv = InteractiveUtil.recv(inputStream);
        log.info(recv.toJSONString());

        ResultModel javaObject = recv.getData().toJavaObject(ResultModel.class);

        if (StringUtils.equals(ResultEnum.SUCCESS.getCode(), javaObject.getRetCod())) {
            this.start();
            return true;
        }
        return false;
    }

    @Override
    public void run() {
        while (isAlive) {
            try {
                InteractiveModel recvInteractiveModel = InteractiveUtil.recv(this.inputStream);
                procMethod(recvInteractiveModel);
            } catch (IOException e) {
                e.printStackTrace();
                this.cancell();
            }
        }
    }

    public void procMethod(InteractiveModel recvInteractiveModel) throws IOException {
        log.info(recvInteractiveModel.toJSONString());

        String interactiveType = recvInteractiveModel.getInteractiveType();
        JSONObject jsonObject = recvInteractiveModel.getData();

        InteractiveTypeEnum interactiveTypeEnum = InteractiveTypeEnum.getEnumByName(interactiveType);
        if (interactiveTypeEnum == null) {
            return;
        }
        if (interactiveTypeEnum.equals(InteractiveTypeEnum.CLIENT_WAIT)) {
            ClientWaitModel clientWaitModel = jsonObject.toJavaObject(ClientWaitModel.class);
            clientConnect(clientWaitModel);
            return;
        }

        return;
    }

    private void clientConnect(ClientWaitModel clientWaitModel) {
        Socket destSocket = null;
        try {
            destSocket = new Socket(this.destIp, this.destPort);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        Socket clientSocket = null;
        try {
            clientSocket = new Socket(this.clientServiceIp, this.clientServicePort);
            InteractiveModel model = InteractiveModel.of(InteractiveTypeEnum.CLIENT_CONNECT,
                    new ClientWaitModel(clientWaitModel.getSocketPartKey()));

            OutputStream outputStream2 = clientSocket.getOutputStream();
            InputStream inputStream2 = clientSocket.getInputStream();

            InteractiveUtil.send(outputStream2, model);

            InteractiveModel recv = InteractiveUtil.recv(inputStream2);
            log.info(recv.toJSONString());

            ResultModel javaObject = recv.getData().toJavaObject(ResultModel.class);

            if (!StringUtils.equals(ResultEnum.SUCCESS.getCode(), javaObject.getRetCod())) {
                throw new RuntimeException("绑定失败");
            }

        } catch (Exception e) {
            e.printStackTrace();
            try {
                destSocket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            if (clientSocket != null) {
                try {
                    clientSocket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            return;
        }

        SocketPart socketPart = new SocketPart();
        socketPart.setSocketPartKey(clientWaitModel.getSocketPartKey());
        socketPart.setListenSocket(destSocket);
        socketPart.setSendSocket(clientSocket);
        socketPartMap.put(clientWaitModel.getSocketPartKey(), socketPart);
        socketPart.createPassWay();
    }

    @Override
    public boolean stopSocketPart(String socketPartKey) {
        log.info("stopSocketPart[{}]", socketPartKey);
        SocketPart socketPart = socketPartMap.remove(socketPartKey);
        if (socketPart == null) {
            return false;
        }
        socketPart.cancell();
        return true;
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

        if (client != null) {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public Integer getClientServicePort() {
        return clientServicePort;
    }

    public void setClientServicePort(Integer clientServicePort) {
        this.clientServicePort = clientServicePort;
    }

    public Integer getListenServerPort() {
        return listenServerPort;
    }

    public void setListenServerPort(Integer listenServerPort) {
        this.listenServerPort = listenServerPort;
    }

    public String getDestIp() {
        return destIp;
    }

    public void setDestIp(String destIp) {
        this.destIp = destIp;
    }

    public Integer getDestPort() {
        return destPort;
    }

    public void setDestPort(Integer destPort) {
        this.destPort = destPort;
    }

    public String getClientServiceIp() {
        return clientServiceIp;
    }

    public void setClientServiceIp(String clientServiceIp) {
        this.clientServiceIp = clientServiceIp;
    }

}
