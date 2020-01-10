package person.pluto.natcross.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import person.pluto.natcross.entity.ListenPort;
import person.pluto.natcross.model.SercretModel;
import person.pluto.natcross2.serverside.listen.ListenServerControl;
import person.pluto.natcross2.serverside.listen.config.IListenServerConfig;
import person.pluto.natcross2.serverside.listen.config.SecretSimpleListenServerConfig;
import person.pluto.natcross2.serverside.listen.config.SimpleListenServerConfig;

/**
 * <p>
 * 内网穿透综合服务类
 * </p>
 *
 * @author Pluto
 * @since 2020-01-10 09:50:07
 */
@Service
public class NatcrossServer {

    @Autowired
    private SercretModel sercret;

    /**
     * 创建新的监听
     * 
     * @author Pluto
     * @since 2020-01-10 10:09:24
     * @param listenPortModel
     * @return
     */
    public boolean createNewListen(ListenPort listenPortModel) {
        return this.createNewListen(listenPortModel.getListenPort());
    }

    /**
     * 创建新的监听
     * 
     * @author Pluto
     * @since 2020-01-10 10:09:37
     * @param listenPort
     * @return
     */
    public boolean createNewListen(int listenPort) {
        IListenServerConfig config;

        if (sercret.isValid()) {
            SecretSimpleListenServerConfig secretConfig = new SecretSimpleListenServerConfig(listenPort);
            secretConfig.setBaseAesKey(sercret.getAeskey());
            secretConfig.setTokenKey(sercret.getTokenKey());

            config = secretConfig;
        } else {
            SimpleListenServerConfig simpleConfig = new SimpleListenServerConfig(listenPort);
            config = simpleConfig;
        }

        return ListenServerControl.createNewListenServer(config) != null;
    }

    /**
     * 移除某个监听
     * 
     * @author Pluto
     * @since 2020-01-10 10:10:52
     * @param listenPort
     * @return
     */
    public boolean removeListen(int listenPort) {
        return ListenServerControl.remove(listenPort);
    }

}
