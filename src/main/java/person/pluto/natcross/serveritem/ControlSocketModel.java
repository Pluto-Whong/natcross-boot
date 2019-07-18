package person.pluto.natcross.serveritem;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import person.pluto.natcross.common.InteractiveUtil;
import person.pluto.natcross.model.ClientWaitModel;
import person.pluto.natcross.model.InteractiveModel;
import person.pluto.natcross.model.InteractiveTypeEnum;

/**
 * 
 * <p>
 * 控制socket实例
 * </p>
 *
 * @author Pluto
 * @since 2019-07-17 11:03:56
 */
public class ControlSocketModel {

    private Socket controlSocket;
    private OutputStream outputStream;

    private Lock socketLock = new ReentrantLock();

    public ControlSocketModel(Socket socket) {
        this.controlSocket = socket;
    }

    public boolean isValid() {
        if (this.controlSocket == null || !this.controlSocket.isConnected() || !this.controlSocket.isClosed()) {
            // 保证control为置空状态
            return false;
        }
        return true;
    }

    public void close() throws IOException {
        controlSocket.close();
    }

    public OutputStream getOutputStream() throws IOException {
        if (outputStream == null) {
            outputStream = this.controlSocket.getOutputStream();
        }
        return outputStream;
    }

    public void sendClientWait(String socketPartKey) throws IOException {
        InteractiveModel model = InteractiveModel.of(InteractiveTypeEnum.CLIENT_WAIT,
                new ClientWaitModel(socketPartKey));
        send(model);
    }

    public void send(InteractiveModel model) throws IOException {
        socketLock.lock();
        OutputStream outputStreamTmp = getOutputStream();
        InteractiveUtil.send(outputStreamTmp, model);
        socketLock.unlock();
    }

}
