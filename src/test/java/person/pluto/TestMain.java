package person.pluto;

import org.apache.shiro.authz.UnauthenticatedException;

import lombok.extern.slf4j.Slf4j;

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
        Exception exception = new UnauthenticatedException();
        if (exception instanceof UnauthenticatedException) {
            System.err.println(123);
        }
    }

}
