package person.pluto.natcross.serveritem;

import java.util.Map;
import java.util.TreeMap;

/**
 * <p>
 * 
 * </p>
 *
 * @author wangmin1994@qq.com
 * @since 2019-07-05 11:25:44
 */
public class ListenServerControl {

    private static Map<Integer, ServerListenThread> serverListenMap = new TreeMap<>();

    public static boolean add(ServerListenThread serverListen) {
        if (serverListen == null) {
            return false;
        }

        Integer listenPort = serverListen.getListenPort();
        ServerListenThread serverListenThread = serverListenMap.get(listenPort);
        if (serverListenThread != null) {
            // 必须要先remove掉才能add，讲道理如果原先的存在应该直接报错才对，也就是参数为null，所以这里不自动remove
            return false;
        }

        serverListenMap.put(listenPort, serverListen);
        return true;
    }

    public static boolean remove(Integer listenPort) {
        ServerListenThread serverListenThread = serverListenMap.get(listenPort);
        if (serverListenThread == null) {
            return true;
        }

        serverListenThread.cancell();
        serverListenMap.remove(listenPort);

        return true;
    }

    public static ServerListenThread get(Integer listenPort) {
        return serverListenMap.get(listenPort);
    }

}
