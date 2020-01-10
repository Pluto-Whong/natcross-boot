package person.pluto.system.common;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import person.pluto.natcross.entity.ListenPort;

/**
 * <p>
 * 格式化类
 * </p>
 *
 * @author wangmin1994@qq.com
 * @since 2019-03-28 11:16:26
 */
public final class SystemFormat {

    /**
     * 
     * 获取某个端口此时上传的证书文件，保存的文件名
     * 
     * @author Pluto
     * @since 2020-01-10 13:21:16
     * @param originName
     * @param listenPort
     * @return
     */
    public static String getListenCertFilename(String originName, ListenPort listenPort) {
        return String.format("P%05d.%s.%s", listenPort.getListenPort(),
                DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now()), originName);
    }

}
