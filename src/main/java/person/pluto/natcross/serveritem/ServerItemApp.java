package person.pluto.natcross.serveritem;

import java.io.IOException;

public class ServerItemApp {

    public static void main(String[] args) throws IOException {
        // 客户端连接
        ClientServiceThread clientServiceThread = new ClientServiceThread(Integer.valueOf(args[0]));
        clientServiceThread.start();

        // 监听端口
        ServerListenThread serverListenThread = new ServerListenThread(Integer.valueOf(args[1]));
        ListenServerControl.add(serverListenThread);
        serverListenThread.start();
    }

}
