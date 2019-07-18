package person.pluto.system.tools;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

/**
 * @Author: xu.dm
 * @Date: 2018/9/25 23:01
 * @Description:
 */
public class EncryptUtils {

    public static String encrypt(String str, String salt, String algorithmName, int hashIterations) {
        return new SimpleHash(algorithmName, str, ByteSource.Util.bytes(salt), hashIterations).toString();
    }

}
