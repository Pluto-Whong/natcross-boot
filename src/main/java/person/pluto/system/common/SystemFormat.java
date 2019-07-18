package person.pluto.system.common;

import person.pluto.system.frameword.CommonConstants;

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
     * 用户token在redis中的key
     *
     * @param usrId
     * @return
     */
    public static String getTokenKey(Long id) {
        return CommonConstants.MOUDLE_NAME + ".token." + id.toString();
    }

    /**
     * 用户websocket在redis中的key
     *
     * @param usrId
     * @return
     */
    public static String getWebsocketAdress(Long id) {
        return CommonConstants.MOUDLE_NAME + ".websocket." + id.toString();
    }

}
