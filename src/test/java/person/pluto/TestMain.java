package person.pluto;

import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

import lombok.extern.slf4j.Slf4j;
import person.pluto.system.frameword.CommonConstants;

/**
 * <p>
 * 自定义测试（不使用junit）
 * </p>
 *
 * @author wangmin1994@qq.com
 * @since 2019-04-01 12:58:54
 */
@Slf4j
@SuppressWarnings("unused")
public class TestMain {

    public static void main(String[] args) throws Exception {
        String string = new SimpleHash(CommonConstants.SHIRO_ALGORITHM_NAME, "admin",
                ByteSource.Util.bytes("8d78869f470951332959580424d4bf4f"), CommonConstants.SHIRO_HASH_ITERATIONS)
                        .toHex();
        System.err.println(string);
    }

}
