package person.pluto.system.common;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

import person.pluto.system.frameword.CommonConstants;

/**
 * <p>
 * common tools
 * </p>
 *
 * @author wangmin1994@qq.com
 * @since 2019-03-28 11:05:01
 */
public final class SystemTools {

    /**
     * 获取加密后的密码(shiro)
     *
     * @author wangmin1994@qq.com
     * @since 2019-05-10 13:36:19
     * @param password     明文密码
     * @param passwordSalt 盐值
     * @return
     */
    public static String getEncryPassword(String password, String passwordSalt) {
        return new SimpleHash(CommonConstants.SHIRO_ALGORITHM_NAME, password, ByteSource.Util.bytes(passwordSalt),
                CommonConstants.SHIRO_HASH_ITERATIONS).toHex();
    }

}
