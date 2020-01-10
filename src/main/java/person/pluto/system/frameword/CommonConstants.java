package person.pluto.system.frameword;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 通用参数
 * </p>
 *
 * @author wangmin1994@qq.com
 * @since 2019-03-28 11:01:04
 */
@Slf4j
@Component
@ConfigurationProperties(prefix = "common.constants")
public class CommonConstants {
    /**
     * shiro 加密方式
     */
    public static String SHIRO_ALGORITHM_NAME = "md5";
    /**
     * shiro加密散列次数
     */
    public static Integer SHIRO_HASH_ITERATIONS = 1;

    /**
     * 请求时的签名key
     */
    public static String SERVER_SIGN_KEY = "serverSignKey";
    /**
     * 签名最大差异时间（毫秒）
     */
    public static Long SIGN_MAX_DIFF_MILLIS = 10000L;

    public void setSHIRO_ALGORITHM_NAME(String sHIRO_ALGORITHM_NAME) {
        SHIRO_ALGORITHM_NAME = sHIRO_ALGORITHM_NAME;
        log.trace("SHIRO_ALGORITHM_NAME[{}]", SHIRO_ALGORITHM_NAME);
    }

    public void setSHIRO_HASH_ITERATIONS(Integer sHIRO_HASH_ITERATIONS) {
        SHIRO_HASH_ITERATIONS = sHIRO_HASH_ITERATIONS;
        log.trace("SHIRO_HASH_ITERATIONS[{}]", SHIRO_HASH_ITERATIONS);
    }

}
