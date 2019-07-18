package person.pluto.system.common;

import org.apache.commons.codec.digest.DigestUtils;

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
     * 获取加密后的密码
     *
     * @author wangmin1994@qq.com
     * @since 2019-05-10 13:36:19
     * @param password     明文密码
     * @param passwordSalt 盐值
     * @return
     */
    public static String getEncryPassword(String password, String passwordSalt) {
        password = password == null ? "" : password;
        passwordSalt = passwordSalt == null ? "" : passwordSalt;
        return DigestUtils.md5Hex(password + passwordSalt);
    }

}
