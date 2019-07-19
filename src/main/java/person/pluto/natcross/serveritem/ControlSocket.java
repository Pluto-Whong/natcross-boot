package person.pluto.natcross.serveritem;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import person.pluto.natcross.common.InteractiveUtil;
import person.pluto.natcross.model.InteractiveModel;
import person.pluto.natcross.model.enumeration.InteractiveTypeEnum;
import person.pluto.natcross.model.interactive.ClientWaitModel;

/**
 * 
 * <p>
 * 控制socket实例
 * </p>
 *
 * @author Pluto
 * @since 2019-07-17 11:03:56
 */
public class ControlSocket {

    private Socket controlSocket;
    private OutputStream outputStream;

    /**
     * 锁定输出资源标志
     */
    private Lock socketLock = new ReentrantLock();

    public ControlSocket(Socket socket) {
        this.controlSocket = socket;
    }

    /**
     * 是否有效
     *
     * @author Pluto
     * @since 2019-07-18 18:33:47
     * @return
     */
    public boolean isValid() {
        if (this.controlSocket == null || !this.controlSocket.isConnected() || !this.controlSocket.isClosed()) {
            return false;
        }
        return true;
    }

    /**
     * 关闭
     *
     * @author Pluto
     * @since 2019-07-18 18:33:54
     * @throws IOException
     */
    public void close() throws IOException {
        controlSocket.close();
    }

    /**
     * 获取输出接口
     *
     * @author Pluto
     * @since 2019-07-18 18:34:22
     * @return
     * @throws IOException
     */
    private OutputStream getOutputStream() throws IOException {
        if (outputStream == null) {
            outputStream = this.controlSocket.getOutputStream();
        }
        return outputStream;
    }

    /**
     * 发送新接入接口
     *
     * @author Pluto
     * @since 2019-07-18 18:34:38
     * @param socketPartKey
     * @throws IOException
     */
    public void sendClientWait(String socketPartKey) throws IOException {
        InteractiveModel model = InteractiveModel.of(InteractiveTypeEnum.CLIENT_WAIT,
                new ClientWaitModel(socketPartKey));
        send(model);
    }

    /**
     * 发送指令（单项）
     *
     * @author Pluto
     * @since 2019-07-18 18:34:56
     * @param model
     * @throws IOException
     */
    public void send(InteractiveModel model) throws IOException {
        socketLock.lock();
        OutputStream outputStreamTmp = getOutputStream();
        InteractiveUtil.send(outputStreamTmp, model);
        socketLock.unlock();
    }

}
