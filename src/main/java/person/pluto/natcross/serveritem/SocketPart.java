package person.pluto.natcross.serveritem;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import lombok.Data;
import person.pluto.natcross.common.IBelongControl;
import person.pluto.natcross.common.InputToOutputThread;

/**
 * 
 * <p>
 * socket匹配对
 * </p>
 *
 * @author Pluto
 * @since 2019-07-12 08:36:30
 */
@Data
public class SocketPart implements IBelongControl {

    private String socketPartKey;
    private Socket listenSocket;
    private Socket sendSocket;

    private InputToOutputThread serverToClientThread;
    private InputToOutputThread clientToServerThread;

    /**
     * 所属监听类
     */
    private IBelongControl belongThread;

    /**
     * 停止，并告知上层处理掉
     *
     * @author Pluto
     * @since 2019-07-11 17:04:52
     */
    public void stop() {
        if (belongThread != null) {
            belongThread.stopSocketPart(socketPartKey);
        }
        belongThread = null;
        this.cancell();
    }

    /**
     * 退出
     *
     * @author Pluto
     * @since 2019-07-11 17:04:39
     */
    public void cancell() {
        if (listenSocket != null) {
            try {
                listenSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            listenSocket = null;
        }

        if (sendSocket != null) {
            try {
                sendSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            sendSocket = null;
        }

        if (serverToClientThread != null) {
            serverToClientThread.cancell();
            serverToClientThread = null;
        }
        if (clientToServerThread != null) {
            clientToServerThread.cancell();
            clientToServerThread = null;
        }
    }

    /**
     * 建立隧道
     *
     * @author Pluto
     * @since 2019-07-11 16:36:08
     * @return
     */
    public boolean createPassWay() {

        try {
            InputStream listInputStream = listenSocket.getInputStream();
            OutputStream lisOutputStream = listenSocket.getOutputStream();

            InputStream sendInputStream = sendSocket.getInputStream();
            OutputStream sendOutputStream = sendSocket.getOutputStream();

            serverToClientThread = new InputToOutputThread(listInputStream, sendOutputStream, this);
            clientToServerThread = new InputToOutputThread(sendInputStream, lisOutputStream, this);

            serverToClientThread.start();
            clientToServerThread.start();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public void noticeStop() {
        this.stop();
    }

}
